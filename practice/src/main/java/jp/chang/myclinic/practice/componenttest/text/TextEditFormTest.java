package jp.chang.myclinic.practice.componenttest.text;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.practice.componenttest.CompTest;
import jp.chang.myclinic.practice.componenttest.ComponentTestBase;
import jp.chang.myclinic.practice.javafx.text.TextEditForm;
import jp.chang.myclinic.practice.javafx.text.TextEnterForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextEditFormTest extends ComponentTestBase {

    public TextEditFormTest(Stage stage, Pane main) {
        super(stage, main);
    }

    private TextEditForm prepareForm(TextDTO text){
        TextEditForm form = new TextEditForm(text);
        form.setPrefWidth(329);
        form.setPrefHeight(300);
        main.getChildren().setAll(form);
        stage.sizeToScene();
        return form;
    }

    @CompTest(excludeFromBatch = true)
    public void testTextEditFormDisp(){
        TextDTO text = new TextDTO();
        text.visitId = 1;
        text.content = "体調いい。";
        prepareForm(text);
    }

}
