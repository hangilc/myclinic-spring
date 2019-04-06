package jp.chang.myclinic.practice.componenttest.text;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.practice.componenttest.CompTest;
import jp.chang.myclinic.practice.componenttest.ComponentTestBase;
import jp.chang.myclinic.practice.javafx.RecordText;
import jp.chang.myclinic.practice.javafx.text.TextDisp;
import jp.chang.myclinic.practice.javafx.text.TextEditForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecordTextTest extends ComponentTestBase {

    public RecordTextTest(Stage stage, Pane main) {
        super(stage, main);
    }

    private RecordText createRecordText(TextDTO text){
        RecordText recordText = new RecordText(text);
        gui(() -> {
            recordText.setPrefWidth(329);
            recordText.setPrefHeight(300);
            main.getChildren().setAll(recordText);
            stage.sizeToScene();
        });
        return recordText;
    }

    @CompTest
    public void testRecordTextDisp(){
        TextDTO text = new TextDTO();
        text.visitId = 1;
        text.content = "体調いい。";
        createRecordText(text);
    }

    @CompTest
    public void testRecordTextClick(){
        TextDTO text = new TextDTO();
        text.visitId = 1;
        text.content = "体調いい。";
        RecordText rec = createRecordText(text);
        TextDisp disp = waitFor(rec::findTextDisp);
        confirm(disp.getRep().equals(text.content));
        gui(() -> disp.simulateMouseEvent(createMouseClickedEvent(disp)));
        TextEditForm form = waitFor(rec::findTextEditForm);
        TextDTO formData = form.deriveTextDTO();
        confirm(text.equals(formData));
        gui(form::simulateClickCancelButton);
        TextDisp disp2 = waitFor(rec::findTextDisp);
        confirm(disp2.getRep().equals(text.content));
    }
}
