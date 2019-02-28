package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.mockdata.MockData;
import jp.chang.myclinic.practice.PracticeConfigServiceMock;
import jp.chang.myclinic.practice.RestServiceMock;
import jp.chang.myclinic.practice.javafx.shohousen.ShohousenRequirement;
import jp.chang.myclinic.practice.javafx.text.TextDisp;
import jp.chang.myclinic.practice.javafx.text.TextEditForm;
import jp.chang.myclinic.practice.javafx.text.TextEnterForm;
import jp.chang.myclinic.practice.javafx.text.TextRequirement;
import jp.chang.myclinic.practice.testgui.ExtensionWaiter;
import jp.chang.myclinic.practice.testgui.TestGroup;
import jp.chang.myclinic.practice.testgui.TestHelper;
import jp.chang.myclinic.utilfx.ConfirmDialog;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class TestText extends TestGroup implements TestHelper {

    private Stage stage;
    private Pane main;
    private MockData mock = new MockData();
    private RestServiceMock restService = new RestServiceMock();
    private PatientDTO patient;

    {
        patient = mock.pickPatient();
        patient.patientId = restService.enterPatient(patient).join();
    }

    private VisitDTO visit;

    {
        visit = mock.pickVisit(patient.patientId, LocalDateTime.now());
        visit.visitId = restService.enterVisit(visit).join();
    }

    private MainPaneService mainPaneService = new MainPaneServiceMock();
    private PracticeConfigServiceMock configService = new PracticeConfigServiceMock();
    private ShohousenRequirement shohousenRequiremetn = new ShohousenRequirement(
            restService, configService
    );
    private TextRequirement req = new TextRequirement(
            restService, mainPaneService, shohousenRequiremetn
    );

    private VisitDTO newVisit(){
        VisitDTO newVisit = mock.pickVisit(patient.patientId, LocalDateTime.now());
        newVisit.visitId = restService.enterVisit(newVisit).join();
        return newVisit;
    }

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
//        addTestProc("record-text-shohousen", this::testRecordTextShohousen);
//        addTestProc("record-text-shohousen-check-modified", this::testRecordTextShohousenCheckModified);
//        addTestProc("record-text-shohousen-confirm-current-ok", this::testRecordTextShohousenConfirmCurrentOk);
//        addTestProc("record-text-shohousen-confirm-current-no", this::testRecordTextShohousenConfirmCurrentNo);
//        addTestProc("edit-form-copy", this::testEditFormCopy);
//        addTestProc("pane-disp", this::testPaneDisp);
    }

    public TestText(Stage stage, Pane main) {
        this.stage = stage;
        this.main = main;
    }

    private void testEnterFormDisp() {
        confirm(!Platform.isFxApplicationThread());
        TextEnterForm form = new TextEnterForm(1, req);
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
        TextEnterForm form = new TextEnterForm(visit.visitId, req);
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
        confirm(state.entered.visitId == visit.visitId);
        confirm(state.entered.textId != 0);
        confirm(state.entered.content.equals(content));
    }

    private void testTextsPaneDisp() {
        RecordTextsPane textsPane = new RecordTextsPane(Collections.emptyList(), visit.visitId);
        textsPane.setTextRequirement(req);
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
        VisitDTO newVisit = newVisit();
        RecordTextsPane textsPane = new RecordTextsPane(Collections.emptyList(), newVisit.visitId);
        textsPane.setTextRequirement(req);
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
        recordText.setTextRequirement(req);
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
        RecordText recordText = new RecordText(textDTO);
        recordText.setTextRequirement(req);
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
        TextRequirement tr = req.copy();
        tr.restService = new TextRequirement.RestServiceDelegate(tr.restService){
            @Override
            public CompletableFuture<Boolean> deleteText(int textId) {
                confirm(textId == textDTO.textId);
                return super.deleteText(textId);
            }
        };
        recordText.setTextRequirement(tr);
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
        recordText.setTextRequirement(req);
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

//    private void testRecordTextShohousen() {
//        TextDTO textDTO = new TextDTO();
//        textDTO.visitId = 1;
//        textDTO.textId = 10;
//        textDTO.content = "昨日から、頭痛がある。";
//        RecordText recordText = new RecordText(textDTO);
//        CurrentExamLib currentExamLib = new CurrentExamLib() {
//            @Override
//            public PatientDTO getCurrentPatient() {
//                return null;
//            }
//
//            @Override
//            public int getCurrentVisitId() {
//                return 1;
//            }
//
//            @Override
//            public int getTempVisitId() {
//                return 0;
//            }
//        };
//        recordText.setTextLib(new TextLibAdapter() {
//            @Override
//            public CurrentExamLib getCurrentExamLib() {
//                return currentExamLib;
//            }
//
//            @Override
//            public ShohousenService getShohousenLib() {
//                return ShohousenServiceMock.create();
//            }
//        });
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
//        RecordText recordText = new RecordText(textDTO);
//        CurrentExamLib currentExamLib = new CurrentExamLib() {
//            @Override
//            public PatientDTO getCurrentPatient() {
//                return null;
//            }
//
//            @Override
//            public int getCurrentVisitId() {
//                return 1;
//            }
//
//            @Override
//            public int getTempVisitId() {
//                return 0;
//            }
//
//        };
//        recordText.setTextLib(new TextLibAdapter() {
//            @Override
//            public CurrentExamLib getCurrentExamLib() {
//                return currentExamLib;
//            }
//
//            @Override
//            public ShohousenService getShohousenLib() {
//                return ShohousenServiceMock.create();
//            }
//        });
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
//        RecordText recordText = new RecordText(textDTO);
//        CurrentExamLib currentExamLib = new CurrentExamLib() {
//            @Override
//            public PatientDTO getCurrentPatient() {
//                return null;
//            }
//
//            @Override
//            public int getCurrentVisitId() {
//                return 1;
//            }
//
//            @Override
//            public int getTempVisitId() {
//                return 0;
//            }
//
//        };
//        recordText.setTextLib(new TextLibAdapter() {
//            @Override
//            public CurrentExamLib getCurrentExamLib() {
//                return currentExamLib;
//            }
//
//            @Override
//            public ShohousenService getShohousenLib() {
//                return ShohousenServiceMock.create();
//            }
//        });
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
//        RecordText recordText = new RecordText(textDTO);
//        CurrentExamLib currentExamLib = new CurrentExamLib() {
//            @Override
//            public PatientDTO getCurrentPatient() {
//                return null;
//            }
//
//            @Override
//            public int getCurrentVisitId() {
//                return 1;
//            }
//
//            @Override
//            public int getTempVisitId() {
//                return 0;
//            }
//
//        };
//        recordText.setTextLib(new TextLibAdapter() {
//            @Override
//            public CurrentExamLib getCurrentExamLib() {
//                return currentExamLib;
//            }
//
//            @Override
//            public ShohousenService getShohousenLib() {
//                throw new RuntimeException("should not be invoked");
//            }
//        });
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
//        TextEditForm editForm = new TextEditForm(textDTO);
//        class State {
//            private boolean done;
//            private boolean enterTextInvoked;
//            private boolean broadcastNewTextInvoked;
//        }
//        State state = new State();
//        editForm.setOnDone(() -> state.done = true);
//        CurrentExamLib currentExamLib = new CurrentExamLib() {
//            @Override
//            public PatientDTO getCurrentPatient() {
//                return null;
//            }
//
//            @Override
//            public int getCurrentVisitId() {
//                return 1;
//            }
//
//            @Override
//            public int getTempVisitId() {
//                return 0;
//            }
//
//        };
//        editForm.setTextLib(new TextLibAdapter() {
//            @Override
//            public CurrentExamLib getCurrentExamLib() {
//                return currentExamLib;
//            }
//
//            @Override
//            public CompletableFuture<Integer> enterText(TextDTO text) {
//                confirm(text.visitId == 1);
//                confirm(text.content.equals(textDTO.content));
//                state.enterTextInvoked = true;
//                return CompletableFuture.completedFuture(11);
//            }
//
//            @Override
//            public void broadcastNewText(TextDTO newText) {
//                confirm(newText.visitId == 1);
//                confirm(newText.content.equals(textDTO.content));
//                state.broadcastNewTextInvoked = true;
//            }
//        });
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
