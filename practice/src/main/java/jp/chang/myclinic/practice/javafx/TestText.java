package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jp.chang.myclinic.backendasync.BackendAsync;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.PracticeConfigService;
import jp.chang.myclinic.practice.javafx.text.TextDisp;
import jp.chang.myclinic.practice.javafx.text.TextEditForm;
import jp.chang.myclinic.practice.javafx.text.TextEnterForm;
import jp.chang.myclinic.practice.testgui.ExtensionWaiter;
import jp.chang.myclinic.practice.testgui.TestEnv;
import jp.chang.myclinic.practice.testgui.TestGroup;
import jp.chang.myclinic.practice.testgui.TestHelper;
import jp.chang.myclinic.utilfx.ConfirmDialog;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class TestText extends TestGroup implements TestHelper {

    private Stage stage;
    private Pane main;
    private BackendAsync restService;
    private PracticeConfigService configService;

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
//        addTestProc("record-text-delete-cancel", this::testRecordTextDeleteCancel);
//        addTestProc("record-text-shohousen", this::testRecordTextShohousen);
//        addTestProc("record-text-shohousen-check-modified", this::testRecordTextShohousenCheckModified);
//        addTestProc("record-text-shohousen-confirm-current-ok", this::testRecordTextShohousenConfirmCurrentOk);
//        addTestProc("record-text-shohousen-confirm-current-no", this::testRecordTextShohousenConfirmCurrentNo);
//        addTestProc("edit-form-copy", this::testEditFormCopy);
//        addTestProc("pane-disp", this::testPaneDisp);
    }

    public TestText(TestEnv env) {
        this.stage = env.stage;
        this.main = env.main;
        this.restService = env.restService;
        this.configService = env.configService;
    }

    private void testEnterFormDisp() {
        confirm(!Platform.isFxApplicationThread());
        ExecEnv execEnv = new ExecEnv(restService, null, null);
        TextEnterForm form = new TextEnterForm(1, execEnv);
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
        int visitId = 1;
        ExecEnv execEnv = new ExecEnv(restService, null, null);
        TextEnterForm form = new TextEnterForm(visitId, execEnv);
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
        confirm(state.entered.visitId == visitId);
        confirm(state.entered.textId != 0);
        confirm(state.entered.content.equals(content));
    }

    private void testTextsPaneDisp() {
        ExecEnv execEnv = new ExecEnv(restService, null, null);
        RecordTextsPane textsPane = new RecordTextsPane(Collections.emptyList(), 1);
        textsPane.setExecEnv(execEnv);
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
        int visitId = 1;
        ExecEnv execEnv = new ExecEnv(restService, null, null);
        RecordTextsPane textsPane = new RecordTextsPane(Collections.emptyList(), visitId);
        textsPane.setExecEnv(execEnv);
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
        RecordText recordText = new RecordText(textDTO);
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
        textDTO.content = "昨日から、頭痛がある。";
        textDTO.textId = restService.enterText(textDTO).join();
        RecordText recordText = new RecordText(textDTO);
        ExecEnv execEnv = new ExecEnv(restService, null, null);
        recordText.setExecEnv(execEnv);
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
//        ExecEnv execEnv = new ExecEnv(restService, null, null);
//        TextDTO textDTO = new TextDTO();
//        textDTO.visitId = 1;
//        textDTO.content = "昨日から、頭痛がある。";
//        textDTO.textId = restService.enterText(textDTO).join();
//        RecordText recordText = new RecordText(textDTO);
//        tr.restService = new TextRequirement.RestServiceDelegate(tr.restService) {
//            @Override
//            public CompletableFuture<Boolean> deleteText(int textId) {
//                confirm(textId == textDTO.textId);
//                return super.deleteText(textId);
//            }
//        };
//        recordText.setExecEnv(tr);
//        gui(() -> {
//            recordText.setPrefWidth(329);
//            recordText.setPrefHeight(400);
//            main.getChildren().setAll(recordText);
//            stage.sizeToScene();
//        });
//        TextDisp disp = waitFor(3, recordText::findTextDisp);
//        gui(() -> disp.simulateMouseEvent(createMouseClickedEvent(disp)));
//        TextEditForm editForm = waitFor(3, recordText::findTextEditForm);
//        class State {
//            private boolean deleted;
//        }
//        State state = new State();
//        editForm.setOnDeleted(() -> state.deleted = true);
//        gui(editForm::simulateClickDeleteButton);
//        ConfirmDialog confirmDialog = waitForWindow(ConfirmDialog.class);
//        gui(confirmDialog::simulateClickOkButton);
//        waitForWindowDisappear(confirmDialog);
//        waitForTrue(() -> state.deleted);
    }

//    private void testRecordTextDeleteCancel() {
//        TextDTO textDTO = new TextDTO();
//        textDTO.visitId = 1;
//        textDTO.textId = 10;
//        textDTO.content = "昨日から、頭痛がある。";
//        RecordText recordText = new RecordText(textDTO);
//        recordText.setExecEnv(execEnv);
//        gui(() -> {
//            recordText.setPrefWidth(329);
//            recordText.setPrefHeight(400);
//            main.getChildren().setAll(recordText);
//            stage.sizeToScene();
//        });
//        TextDisp disp = waitFor(recordText::findTextDisp);
//        gui(() -> disp.simulateMouseEvent(createMouseClickedEvent(disp)));
//        TextEditForm editForm = waitFor(3, recordText::findTextEditForm);
//        gui(editForm::simulateClickDeleteButton);
//        ConfirmDialog confirmDialog = waitForWindow(ConfirmDialog.class);
//        gui(confirmDialog::simulateClickNoButton);
//        waitForWindowDisappear(confirmDialog);
//        TextEditForm editForm2 = waitFor(recordText::findTextEditForm);
//        confirm(editForm == editForm2);
//    }
//
//    private void testRecordTextShohousen() {
//        TextDTO textDTO = new TextDTO();
//        textDTO.visitId = 1;
//        textDTO.textId = 10;
//        textDTO.content = "昨日から、頭痛がある。";
//        RecordText recordText = new RecordText(textDTO);
//        mainPaneService.setCurrent(patient, textDTO.visitId);
//        recordText.setExecEnv(execEnv);
//        gui(() -> {
//            recordText.setPrefWidth(329);
//            recordText.setPrefHeight(400);
//            main.getChildren().setAll(recordText);
//            stage.sizeToScene();
//        });
//        TextDisp disp = waitFor(recordText::findTextDisp);
//        gui(() -> disp.simulateMouseEvent(createMouseClickedEvent(disp)));
//        TextEditForm editForm = waitFor(recordText::findTextEditForm);
//        class State {
//            private boolean done;
//        }
//        State state = new State();
//        editForm.setOnDone(() -> state.done = true);
//        gui(editForm::simulateClickShohousenButton);
//        DrawerPreviewDialog preview = waitForWindow(DrawerPreviewDialog.class);
//        gui(preview::close);
//        waitForTrue(() -> state.done);
//    }
//
//    private void testRecordTextShohousenCheckModified() {
//        TextDTO textDTO = new TextDTO();
//        textDTO.visitId = 1;
//        textDTO.textId = 10;
//        textDTO.content = "昨日から、頭痛がある。";
//        mainPaneService.setCurrent(patient, textDTO.visitId);
//        RecordText recordText = new RecordText(textDTO);
//        recordText.setExecEnv(execEnv);
//        gui(() -> {
//            recordText.setPrefWidth(329);
//            recordText.setPrefHeight(400);
//            main.getChildren().setAll(recordText);
//            stage.sizeToScene();
//        });
//        TextDisp disp = waitFor(recordText::findTextDisp);
//        gui(() -> disp.simulateMouseEvent(createMouseClickedEvent(disp)));
//        TextEditForm editForm = waitFor(recordText::findTextEditForm);
//        gui(() -> editForm.simulateSetText(textDTO.content + " modified"));
//        gui(editForm::simulateClickShohousenButton);
//        AlertDialog alertDialog = waitForWindow(AlertDialog.class);
//        gui(alertDialog::simulateClickOkButton);
//        waitForWindowDisappear(alertDialog);
//    }
//
//    private void testRecordTextShohousenConfirmCurrentOk() {
//        TextDTO textDTO = new TextDTO();
//        textDTO.visitId = 2;
//        textDTO.textId = 10;
//        textDTO.content = "昨日から、頭痛がある。";
//        mainPaneService.setCurrent(patient, 0);
//        RecordText recordText = new RecordText(textDTO);
//        recordText.setExecEnv(execEnv);
//        gui(() -> {
//            recordText.setPrefWidth(329);
//            recordText.setPrefHeight(400);
//            main.getChildren().setAll(recordText);
//            stage.sizeToScene();
//        });
//        TextDisp disp = waitFor(recordText::findTextDisp);
//        gui(() -> disp.simulateMouseEvent(createMouseClickedEvent(disp)));
//        TextEditForm editForm = waitFor(recordText::findTextEditForm);
//        gui(editForm::simulateClickShohousenButton);
//        ConfirmDialog confirmDialog = waitForWindow(ConfirmDialog.class);
//        gui(confirmDialog::simulateClickOkButton);
//        waitForWindowDisappear(confirmDialog);
//        DrawerPreviewDialog preview = waitForWindow(DrawerPreviewDialog.class);
//        gui(preview::close);
//        waitForWindowDisappear(preview);
//    }
//
//    private void testRecordTextShohousenConfirmCurrentNo() {
//        TextDTO textDTO = new TextDTO();
//        textDTO.visitId = 2;
//        textDTO.textId = 10;
//        textDTO.content = "昨日から、頭痛がある。";
//        mainPaneService.setCurrent(patient, 0);
//        RecordText recordText = new RecordText(textDTO);
//        recordText.setExecEnv(execEnv);
//        gui(() -> {
//            recordText.setPrefWidth(329);
//            recordText.setPrefHeight(400);
//            main.getChildren().setAll(recordText);
//            stage.sizeToScene();
//        });
//        TextDisp disp = waitFor(recordText::findTextDisp);
//        gui(() -> disp.simulateMouseEvent(createMouseClickedEvent(disp)));
//        TextEditForm editForm = waitFor(recordText::findTextEditForm);
//        gui(editForm::simulateClickShohousenButton);
//        ConfirmDialog confirmDialog = waitForWindow(ConfirmDialog.class);
//        gui(confirmDialog::simulateClickNoButton);
//        waitForWindowDisappear(confirmDialog);
//    }
//
//    private void testEditFormCopy() {
//        TextDTO textDTO = new TextDTO();
//        textDTO.visitId = 3;
//        textDTO.textId = 10;
//        textDTO.content = "昨日から、頭痛がある。";
//        class State {
//            private boolean done;
//            private boolean enterTextInvoked;
//            private boolean broadcastNewTextInvoked;
//        }
//        State state = new State();
//        TextRequirement tr = req.copy();
//        tr.restService = new TextRequirement.RestServiceDelegate(req.restService) {
//            @Override
//            public CompletableFuture<Integer> enterText(TextDTO text) {
//                confirm(text.visitId == 1);
//                confirm(text.content.equals(textDTO.content));
//                state.enterTextInvoked = true;
//                return CompletableFuture.completedFuture(11);
//            }
//
//
//        };
//        MainPaneServiceMock mainPaneService = new MainPaneServiceMock();
//        mainPaneService.setCurrent(patient, 1);
//        tr.mainPaneService = new TextRequirement.MainPaneServiceDelegate(
//                mainPaneService
//        ) {
//            @Override
//            public void broadcastNewText(TextDTO newText) {
//                confirm(newText.visitId == 1);
//                confirm(newText.content.equals(textDTO.content));
//                state.broadcastNewTextInvoked = true;
//            }
//        };
//        TextEditForm editForm = new TextEditForm(textDTO, tr);
//        editForm.setOnDone(() -> state.done = true);
//        gui(() -> {
//            editForm.setPrefWidth(329);
//            editForm.setPrefHeight(400);
//            main.getChildren().setAll(editForm);
//            stage.sizeToScene();
//        });
//        gui(editForm::simulateClickCopyButton);
//        waitForTrue(() -> state.done);
//        waitForTrue(() -> state.enterTextInvoked);
//        waitForTrue(() -> state.broadcastNewTextInvoked);
//    }
//
//    private void testPaneDisp() {
//        TextDTO textDTO = new TextDTO();
//        textDTO.visitId = 3;
//        textDTO.textId = 10;
//        textDTO.content = "昨日から、頭痛がある。";
//        RecordTextsPane textsPane = new RecordTextsPane(List.of(textDTO), 1);
//        gui(() -> {
//            textsPane.setPrefWidth(329);
//            textsPane.setPrefHeight(400);
//            main.getChildren().setAll(textsPane);
//            stage.sizeToScene();
//        });
//    }

}
