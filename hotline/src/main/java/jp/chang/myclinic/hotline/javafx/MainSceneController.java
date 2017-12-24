package jp.chang.myclinic.hotline.javafx;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class MainSceneController {

    public VBox messageBox;
    public TextArea inputText;

    public void onSubmit(Event event){
        String text = inputText.getText();
        if( text.isEmpty() ){
            return;
        }
        Label label = new Label(text);
        label.setAlignment(Pos.TOP_LEFT);
        label.setWrapText(true);
        messageBox.getChildren().add(label);
    }

    public void onRaja(ActionEvent actionEvent) {
        System.out.println("on raja");
    }

    public void onBeep(ActionEvent actionEvent) {
        System.out.println("on beep");
    }
}
