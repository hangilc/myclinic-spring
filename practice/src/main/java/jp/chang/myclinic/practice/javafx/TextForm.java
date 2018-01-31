package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TextForm extends VBox {

    public interface Callback {
        void onEnter(String content);
        void onCancel();
    }

    private TextArea textArea = new TextArea();
    private Callback callback;

    public TextForm(){
        super(4);
        setFillWidth(true);
        textArea.setPrefHeight(160);
        getChildren().addAll(
                textArea,
                createButtons()
        );
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    private Node createButtons(){
        HBox hbox = new HBox(4);
        Hyperlink enterLink = new Hyperlink("入力");
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
