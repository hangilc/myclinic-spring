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

public class TextFormTest extends ComponentTestBase {

    public TextFormTest(Stage stage, Pane pane) {
        super(stage, pane);
    }

    private TextEnterForm prepareForm(int visitId){
        TextEnterForm form = new TextEnterForm(visitId);
        form.setPrefWidth(329);
        form.setPrefHeight(300);
        main.getChildren().setAll(form);
        stage.sizeToScene();
        return form;
    }

    @CompTest(name = "disp-text-enter-form", excludeFromBatch = true)
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
        form.simulateSetText(text);
        form.simulateClickEnterButton();
        waitForTrue(4, () -> local.textConfirmed);
    }


}
