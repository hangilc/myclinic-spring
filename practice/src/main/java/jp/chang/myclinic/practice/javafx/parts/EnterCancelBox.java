package jp.chang.myclinic.practice.javafx.parts;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnterCancelBox extends HBox {

    private static Logger logger = LoggerFactory.getLogger(EnterCancelBox.class);

    private Runnable enterCallback;
    private Runnable cancelCallback;

    public EnterCancelBox(){
        this(() -> {}, () -> {});
    }

    public EnterCancelBox(Runnable enterCallback, Runnable cancelCallback){
        super(4);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        enterButton.setOnAction(evt -> enterCallback.run());
        cancelButton.setOnAction(evt -> cancelCallback.run());
        getChildren().addAll(enterButton, cancelButton);
    }

    public void setEnterCallback(Runnable enterCallback){
        this.enterCallback = enterCallback;
    }

    public void setCancelCallback(Runnable cancelCallback){
        this.cancelCallback = cancelCallback;
    }

}
