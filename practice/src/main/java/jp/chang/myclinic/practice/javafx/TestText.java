package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.practice.javafx.text.TextEnterForm;
import jp.chang.myclinic.practice.javafx.text.TextLib;
import jp.chang.myclinic.practice.testgui.TestGroup;
import jp.chang.myclinic.practice.testgui.TestHelper;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class TestText extends TestGroup implements TestHelper {

    private Stage stage;
    private Pane main;

    {
        addTestProc("enter-form-disp", this::testEnterFormDisp);
        addTestProc("enter-form-cancel", this::testEnterFormCancel);
        addTestProc("enter-form-enter", this::testEnterFormEnter);
        addTestProc("record-text-disp", this::testRecordTextDisp);
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
            System.out.println("CANCEL");
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

    private void testRecordTextDisp(){
        TextDTO textDTO = new TextDTO();
        textDTO.visitId = 1;
        textDTO.textId = 10;
        textDTO.content = "昨日から、頭痛がある。";
        class State {
            private TextDTO updated;
        }
        State state = new State();
        TextLib textLib = new TextLib(){
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
    }

}
