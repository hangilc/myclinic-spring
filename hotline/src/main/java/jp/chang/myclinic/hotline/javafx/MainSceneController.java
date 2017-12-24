package jp.chang.myclinic.hotline.javafx;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.hotline.Context;
import jp.chang.myclinic.hotline.lib.HotlineUtil;

public class MainSceneController {

    public VBox messageBox;
    public TextArea inputText;

    public void onSubmit(Event event){
        String message = inputText.getText();
        if( message.isEmpty() ){
            return;
        }
        HotlineUtil.postMessge(Context.INSTANCE.getSender().getName(), Context.INSTANCE.getRecipient().getName(), message);
//        Label label = new Label(text);
//        label.setAlignment(Pos.TOP_LEFT);
//        label.setWrapText(true);
//        messageBox.getChildren().add(label);
    }

    public void onRaja(ActionEvent actionEvent) {
        System.out.println("on raja");
    }

    public void onBeep(ActionEvent actionEvent) {
        System.out.println("on beep");
    }
}
