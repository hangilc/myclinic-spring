package jp.chang.myclinic.practice.javafx.parts;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.HBox;

public class EnterCancelDeleteBox extends HBox {

    //private static Logger logger = LoggerFactory.getLogger(EnterCancelDeleteBox.class);
    private Runnable enterCallback = () -> {};
    private Runnable cancelCallback = () -> {};
    private Runnable deleteCallback = () -> {};

    public EnterCancelDeleteBox() {
        super(4);
        setAlignment(Pos.CENTER_LEFT);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        Hyperlink deleteLink = new Hyperlink("削除");
        enterButton.setOnAction(evt -> enterCallback.run());
        cancelButton.setOnAction(evt -> cancelCallback.run());
        deleteLink.setOnAction(evt -> deleteCallback.run());
        getChildren().addAll(enterButton, cancelButton, deleteLink);
    }

    public void setEnterCallback(Runnable enterCallback) {
        this.enterCallback = enterCallback;
    }

    public void setCancelCallback(Runnable cancelCallback) {
        this.cancelCallback = cancelCallback;
    }

    public void setDeleteCallback(Runnable deleteCallback) {
        this.deleteCallback = deleteCallback;
    }
}
