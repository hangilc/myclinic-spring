package jp.chang.myclinic.practice.componenttest.text;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.frontend.FrontendAdapter;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.componenttest.CompTest;
import jp.chang.myclinic.practice.componenttest.ComponentTestBase;
import jp.chang.myclinic.practice.javafx.text.TextEnterForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class TextFormTest extends ComponentTestBase {

    public TextFormTest(Stage stage, Pane pane) {
        super(stage, pane);
    }

    @CompTest
    public void testEnterTextFormDisp() {
        Context.frontend = new FrontendAdapter(){
            @Override
            public CompletableFuture<Integer> enterText(TextDTO text) {
                text.textId = 1;
                return CompletableFuture.completedFuture(text.textId);
            }
        };
        TextEnterForm form = new TextEnterForm(1);
        form.setOnEntered(entered -> System.out.printf("entered: %s\n", entered.toString()));
        form.setOnCancel(() -> System.out.println("cancel"));
        form.setPrefWidth(329);
        form.setPrefHeight(300);
        main.getChildren().setAll(form);
        stage.sizeToScene();
    }

    
}
