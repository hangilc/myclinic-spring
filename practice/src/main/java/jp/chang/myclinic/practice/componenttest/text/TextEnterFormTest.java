package jp.chang.myclinic.practice.componenttest.text;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.frontend.FrontendAdapter;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.componenttest.CompTest;
import jp.chang.myclinic.practice.componenttest.ComponentTestBase;
import jp.chang.myclinic.practice.javafx.text.TextEnterForm;

import java.util.concurrent.CompletableFuture;

public class TextEnterFormTest extends ComponentTestBase {

    public TextEnterFormTest(Stage stage, Pane pane) {
        super(stage, pane);
    }

    private TextEnterForm prepareForm(int visitId){
        TextEnterForm form = new TextEnterForm(visitId);
        gui(() -> {
            form.setPrefWidth(329);
            form.setPrefHeight(300);
            main.getChildren().setAll(form);
            stage.sizeToScene();
        });
        return form;
    }

    @CompTest(excludeFromBatch = true)
    public void testEnterTextFormDisp() {
        Context.frontend = new FrontendAdapter(){
            @Override
            public CompletableFuture<Integer> enterText(TextDTO text) {
                text.textId = 1;
                return CompletableFuture.completedFuture(text.textId);
            }
        };
        TextEnterForm form = prepareForm(1);
        form.setOnEntered(entered -> System.out.printf("entered: %s\n", entered.toString()));
        form.setOnCancel(() -> System.out.println("cancel"));
    }

    @CompTest
    public void testEnterTextFormEnter(){
        String text = "昨日から、のどの痛みある。";
        class Local {
            private boolean textConfirmed;
        }
        Local local = new Local();
        Context.frontend = new FrontendAdapter(){
            @Override
            public CompletableFuture<Integer> enterText(TextDTO entered) {
                entered.textId = 1;
                local.textConfirmed = text.equals(entered.content);
                return CompletableFuture.completedFuture(entered.textId);
            }
        };
        TextEnterForm form = prepareForm(1);
        gui(() -> {
            form.simulateSetText(text);
            form.simulateClickEnterButton();
        });
        waitForTrue(10, () -> local.textConfirmed);
    }

    @CompTest
    public void testEnterTextFormCancel(){
        class Local {
            private boolean cancelCalled;
        }
        Local local = new Local();
        TextEnterForm form = prepareForm(1);
        form.setOnCancel(() -> { local.cancelCalled = true; });
        gui(() -> {
            form.simulateClickCancelButton();
        });
        waitForTrue(10, () -> local.cancelCalled);
    }


}
