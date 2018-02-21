package jp.chang.myclinic.practice.javafx.disease.add;

import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandBox extends HBox {

    private static Logger logger = LoggerFactory.getLogger(CommandBox.class);

    private Runnable onEnterCallback = () -> {};
    private Runnable onSuspCallback = () -> {};
    private Runnable onDeleteAdjCallback = () -> {};

    public CommandBox() {
        super(4);
        Button enterButton = new Button("入力");
        Hyperlink suspLink = new Hyperlink("の疑い");
        Hyperlink deleteAdjLink = new Hyperlink("修飾語削除");
        enterButton.setOnAction(evt -> onEnterCallback.run());
        suspLink.setOnAction(evt -> onSuspCallback.run());
        deleteAdjLink.setOnAction(evt -> onDeleteAdjCallback.run());
        getChildren().addAll(
                enterButton,
                suspLink,
                deleteAdjLink
        );
    }

    public void setOnEnterCallback(Runnable cb){
        this.onEnterCallback = cb;
    }

    public void setOnSuspCallback(Runnable cb){
        this.onSuspCallback = cb;
    }

    public void setOnDeleteAdjCallback(Runnable cb){
        this.onDeleteAdjCallback = cb;
    }

}
