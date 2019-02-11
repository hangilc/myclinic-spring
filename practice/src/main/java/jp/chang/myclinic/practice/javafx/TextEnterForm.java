package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TextEnterForm extends VBox {

    public interface Callback {
        void onEnter(String content);
        void onCancel();
    }

    private TextArea textArea = new TextArea();
    private Hyperlink enterLink;
    private Callback callback;

    public TextEnterForm(){
        super(4);
        getStyleClass().addAll("record-text-form", "enter");
        setFillWidth(true);
        textArea.setWrapText(true);
        getChildren().addAll(
                textArea,
                createButtons()
        );
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    public void setContent(String content){
        textArea.setText(content);
    }

    public void simulateClickEnterButton(){
        enterLink.fire();
    }

    private Node createButtons(){
        HBox hbox = new HBox(4);
        this.enterLink = new Hyperlink("入力");
        Hyperlink cancelLink = new Hyperlink("キャンセル ");
        enterLink.setOnAction(event -> {
            if( callback != null ){
                String content = textArea.getText().trim();
                callback.onEnter(content);
            }
        });
        cancelLink.setOnAction(event -> {
            if( callback != null ){
                callback.onCancel();
            }
        });
        hbox.getChildren().addAll(enterLink, cancelLink);
        return hbox;
    }
}
