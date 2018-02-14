package jp.chang.myclinic.practice.javafx.conduct;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.practice.javafx.parts.WorkForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnterXpForm extends WorkForm {

    private static Logger logger = LoggerFactory.getLogger(EnterXpForm.class);

    public EnterXpForm(int visitId) {
        super("X線入力");
        getChildren().addAll(
                createCommands()
        );
    }

    private Node createCommands(){
        HBox hbox = new HBox(4);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        cancelButton.setOnAction(evt -> onCancel(this));
        hbox.getChildren().addAll(enterButton, cancelButton);
        return hbox;
    }

    protected void onCancel(EnterXpForm form){

    }

}
