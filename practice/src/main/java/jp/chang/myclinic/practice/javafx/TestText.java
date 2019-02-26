package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.practice.javafx.parts.drawerpreview.DrawerPreviewDialog;
import jp.chang.myclinic.practice.javafx.shohousen.ShohousenLib;
import jp.chang.myclinic.practice.javafx.shohousen.ShohousenLibMock;
import jp.chang.myclinic.practice.javafx.text.TextDisp;
import jp.chang.myclinic.practice.javafx.text.TextEditForm;
import jp.chang.myclinic.practice.javafx.text.TextEnterForm;
import jp.chang.myclinic.practice.javafx.text.TextLib;
import jp.chang.myclinic.practice.testgui.ExtensionWaiter;
import jp.chang.myclinic.practice.testgui.TestGroup;
import jp.chang.myclinic.practice.testgui.TestHelper;
import jp.chang.myclinic.utilfx.ConfirmDialog;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class TestText extends TestGroup implements TestHelper {

    private Stage stage;
    private Pane main;

    {
        addTestProc("enter-form-disp", this::testEnterFormDisp);
        addTestProc("enter-form-cancel", this::testEnterFormCancel);
        addTestProc("enter-form-enter", this::testEnterFormEnter);
        addTestProc("texts-pane-disp", this::testTextsPaneDisp);
        addTestProc("texts-pane-cancel", this::testTextsPaneCancel);
        addTestProc("texts-pane-enter", this::testTextsPaneEnter);
        addTestProc("record-text-disp", this::testRecordTextDisp);
        addTestProc("record-text-update", this::testRecordTextUpdate);
        addTestProc("record-text-cancel", this::testRecordTextCancel);
        addTestProc("record-text-delete", this::testRecordTextDelete);
        addTestProc("record-text-delete-cancel", this::testRecordTextDeleteCancel);
        addTestProc("record-text-shohousen", this::testRecordTextShohousen);
    }

    public TestText(Stage stage, Pane main) {
        this.stage = stage;
        this.main = main;
    }

    private void testEnterFormDisp() {
        confirm(!Platform.isFxApplicationThread());
        TextEnterForm form = new TextEnterForm(1, new TextLib() {
            @Override
            public CompletableFuture<Integer> enterText(TextDTO text) {
                return CompletableFuture.completedFuture(1);
            }
        });
        form.setOnEntered(entered -> System.out.printf("entered: %s\n", entered.toString()));
        form.setOnCancel(() -> System.out.println("cancel"));
        gui(() -> {
            form.setPrefWidth(329);
            main.getChildren().setAll(form);
            stage.sizeToScene();
        });
    }

    private void testEnterFormCancel() {
        TextEnterForm form = new TextEnterForm(1, null);
        class State {
            private boolean canceled;
        }
        State state = new State();
        form.setOnCancel(() -> {
            confirm(Platform.isFxApplicationThread());
            state.canceled = true;
        });
        gui(() -> {
            form.setPrefWidth(329);
            main.getChildren().setAll(form);
            stage.sizeToScene();
            form.simulateClickCancelButton();
        });
        waitForTrue(3, () -> state.canceled);
    }

    private void testEnterFormEnter() {
        TextEnterForm form = new TextEnterForm(1, new TextLib() {
            @Override
            public CompletableFuture<Integer> enterText(TextDTO text) {
                return CompletableFuture.completedFuture(10);
            }
        });
        class State {
            private TextDTO entered;
        }
        State state = new State();
        form.setOnEntered(entered -> {
            confirm(Platform.isFxApplicationThread());
            state.entered = entered;
        });
        String content = "昨日から、頭痛がある。";
        gui(() -> {
            form.setPrefWidth(329);
            main.getChildren().setAll(form);
            stage.sizeToScene();
            form.simulateSetText(content);
            form.simulateClickEnterButton();
        });
        waitForTrue(3, () -> state.entered != null);
        confirm(state.entered.visitId == 1);
        confirm(state.entered.textId == 10);
        confirm(state.entered.content.equals(content));
    }

    private void testTextsPaneDisp() {
        RecordTextsPane textsPane = new RecordTextsPane(Collections.emptyList(), 1);
        TextLib textLib = new TextLib() {
            @Override
            public CompletableFuture<Integer> enterText(TextDTO text) {
                return CompletableFuture.completedFuture(10);
            }
        };
        textsPane.setTextLib(textLib);
        gui(() -> {
            textsPane.setPrefWidth(329);
            textsPane.setPrefHeight(400);
            main.getChildren().setAll(textsPane);
            stage.sizeToScene();
        });
    }

    private void testTextsPaneCancel() {
        RecordTextsPane textsPane = new RecordTextsPane(Collections.emptyList(), 1);
        gui(() -> {
            textsPane.setPrefWidth(329);
            textsPane.setPrefHeight(400);
            main.getChildren().setAll(textsPane);
            stage.sizeToScene();
            textsPane.simulateNewTextButtonClick();
        });
        TextEnterForm enterForm = waitFor(3, textsPane::findTextEnterForm);
        gui(enterForm::simulateClickCancelButton);
        waitForFail(2, textsPane::findTextEnterForm);
    }

    private void testTextsPaneEnter() {
        RecordTextsPane textsPane = new RecordTextsPane(Collections.emptyList(), 1);
        TextLib textLib = new TextLib() {
            @Override
            public CompletableFuture<Integer> enterText(TextDTO text) {
                return CompletableFuture.completedFuture(10);
            }
        };
        textsPane.setTextLib(textLib);
        gui(() -> {
            textsPane.setPrefWidth(329);
            textsPane.setPrefHeight(400);
            main.getChildren().setAll(textsPane);
            stage.sizeToScene();
            textsPane.simulateNewTextButtonClick();
        });
        TextEnterForm enterForm = waitFor(3, textsPane::findTextEnterForm);
        String content = "鼻水がでるようになった。";
        ExtensionWaiter<Integer> textEnteredWaiter = new ExtensionWaiter<>(textsPane::listTextId);
        gui(() -> enterForm.simulateSetText(content));
        gui(enterForm::simulateClickEnterButton);
        waitForFail(3, textsPane::findTextEnterForm);
        int enteredTextId = textEnteredWaiter.waitForExtension(2);
        RecordText recordText = textsPane.findRecordText(enteredTextId).orElseThrow(
                () -> new RuntimeException("cannot find record text")
        );
        confirm(recordText.isDisplaying());
        TextDisp textDisp = recordText.findTextDisp().orElseThrow(
                () -> new RuntimeException("cannot find text disp")
        );
        confirm(textDisp.getContent().equals(content));
    }

    private void testRecordTextDisp() {
        TextDTO textDTO = new TextDTO();
        textDTO.visitId = 1;
        textDTO.textId = 10;
        textDTO.content = "昨日から、頭痛がある。";
        TextLib textLib = new TextLib() {
            @Override
            public CompletableFuture<Boolean> updateText(TextDTO update) {
                confirm(update.visitId == textDTO.visitId);
                confirm(update.textId == textDTO.textId);
                return CompletableFuture.completedFuture(true);
            }
        };
        RecordText recordText = new RecordText(textDTO);
        recordText.setTextLib(textLib);
        gui(() -> {
            recordText.setPrefWidth(329);
            recordText.setPrefHeight(400);
            main.getChildren().setAll(recordText);
            stage.sizeToScene();
        });
    }

    private void testRecordTextUpdate() {
        TextDTO textDTO = new TextDTO();
        textDTO.visitId = 1;
        textDTO.textId = 10;
        textDTO.content = "昨日から、頭痛がある。";
        TextLib textLib = new TextLib() {
            @Override
            public CompletableFuture<Boolean> updateText(TextDTO textDTO) {
                return CompletableFuture.completedFuture(true);
            }
        };
        RecordText recordText = new RecordText(textDTO);
        recordText.setTextLib(textLib);
        gui(() -> {
            recordText.setPrefWidth(329);
            recordText.setPrefHeight(400);
            main.getChildren().setAll(recordText);
            stage.sizeToScene();
        });
        TextDisp disp = waitFor(3, recordText::findTextDisp);
        gui(() -> disp.simulateMouseEvent(createMouseClickedEvent(disp)));
        TextEditForm editForm = waitFor(3, recordText::findTextEditForm);
        String editedText = textDTO.content + " edited\n\n";
        gui(() -> editForm.simulateSetText(editedText));
        gui(editForm::simulateClickEnterButton);
        TextDisp disp2 = waitFor(3, recordText::findTextDisp);
        confirm(disp2.getRep().equals(editedText.trim()));
    }

    private void testRecordTextCancel() {
        TextDTO textDTO = new TextDTO();
        textDTO.visitId = 1;
        textDTO.textId = 10;
        textDTO.content = "昨日から、頭痛がある。";
        RecordText recordText = new RecordText(textDTO);
        gui(() -> {
            recordText.setPrefWidth(329);
            recordText.setPrefHeight(400);
            main.getChildren().setAll(recordText);
            stage.sizeToScene();
        });
        TextDisp disp = waitFor(3, recordText::findTextDisp);
        gui(() -> disp.simulateMouseEvent(createMouseClickedEvent(disp)));
        TextEditForm editForm = waitFor(3, recordText::findTextEditForm);
        gui(() -> editForm.simulateSetText("edited"));
        gui(editForm::simulateClickCancelButton);
        waitForFail(3, recordText::findTextEditForm);
        TextDisp disp2 = waitFor(3, recordText::findTextDisp);
        confirm(disp2.getContent().equals(disp.getContent()));
        confirm(disp2.getRep().equals(disp.getRep()));
    }

    private void testRecordTextDelete() {
        TextDTO textDTO = new TextDTO();
        textDTO.visitId = 1;
        textDTO.textId = 10;
        textDTO.content = "昨日から、頭痛がある。";
        RecordText recordText = new RecordText(textDTO);
        recordText.setTextLib(new TextLib(){
            @Override
            public CompletableFuture<Boolean> deleteText(int textId) {
                confirm(textId == textDTO.textId);
                return CompletableFuture.completedFuture(true);
            }
        });
        gui(() -> {
            recordText.setPrefWidth(329);
            recordText.setPrefHeight(400);
            main.getChildren().setAll(recordText);
            stage.sizeToScene();
        });
        TextDisp disp = waitFor(3, recordText::findTextDisp);
        gui(() -> disp.simulateMouseEvent(createMouseClickedEvent(disp)));
        TextEditForm editForm = waitFor(3, recordText::findTextEditForm);
        class State {
            private boolean deleted;
        }
        State state = new State();
        editForm.setOnDeleted(() -> state.deleted = true);
        gui(editForm::simulateClickDeleteButton);
        ConfirmDialog confirmDialog = waitForWindow(ConfirmDialog.class);
        gui(confirmDialog::simulateClickOkButton);
        waitForWindowDisappear(confirmDialog);
        waitForTrue(() -> state.deleted);
    }

    private void testRecordTextDeleteCancel() {
        TextDTO textDTO = new TextDTO();
        textDTO.visitId = 1;
        textDTO.textId = 10;
        textDTO.content = "昨日から、頭痛がある。";
        RecordText recordText = new RecordText(textDTO);
        recordText.setTextLib(new TextLib(){
            @Override
            public CompletableFuture<Boolean> deleteText(int textId) {
                throw new RuntimeException("delete callback invoked.");
            }
        });
        gui(() -> {
            recordText.setPrefWidth(329);
            recordText.setPrefHeight(400);
            main.getChildren().setAll(recordText);
            stage.sizeToScene();
        });
        TextDisp disp = waitFor(recordText::findTextDisp);
        gui(() -> disp.simulateMouseEvent(createMouseClickedEvent(disp)));
        TextEditForm editForm = waitFor(3, recordText::findTextEditForm);
        gui(editForm::simulateClickDeleteButton);
        ConfirmDialog confirmDialog = waitForWindow(ConfirmDialog.class);
        gui(confirmDialog::simulateClickNoButton);
        waitForWindowDisappear(confirmDialog);
        TextEditForm editForm2 = waitFor(recordText::findTextEditForm);
        confirm(editForm == editForm2);
    }

    private void testRecordTextShohousen() {
        TextDTO textDTO = new TextDTO();
        textDTO.visitId = 1;
        textDTO.textId = 10;
        textDTO.content = "昨日から、頭痛がある。";
        RecordText recordText = new RecordText(textDTO);
        recordText.setTextLib(new TextLib(){
            @Override
            public int getCurrentVisitId() {
                return 1;
            }

            @Override
            public int getTempVisitId() {
                return 0;
            }

            @Override
            public ShohousenLib getShohousenLib() {
                return ShohousenLibMock.create();
            }
        });
        gui(() -> {
            recordText.setPrefWidth(329);
            recordText.setPrefHeight(400);
            main.getChildren().setAll(recordText);
            stage.sizeToScene();
        });
        TextDisp disp = waitFor(recordText::findTextDisp);
        gui(() -> disp.simulateMouseEvent(createMouseClickedEvent(disp)));
        TextEditForm editForm = waitFor(recordText::findTextEditForm);
        class State {
            private boolean done;
        }
        State state = new State();
        editForm.setOnDone(() -> {
            state.done = true;
        });
        gui(editForm::simulateClickShohousenButton);
        DrawerPreviewDialog preview = waitForWindow(DrawerPreviewDialog.class);
        gui(preview::close);
        waitForTrue(() -> state.done);
    }

}
