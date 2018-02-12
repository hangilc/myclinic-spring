package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.TextDTO;

public class TextEditForm extends VBox {

    public interface Callback {
        void onEnter(String content);
        void onCancel();
        void onDelete();
        void onShohousen();
        void onCopy();
    }

    private TextArea textArea = new TextArea();
    private Callback callback;

    public TextEditForm(TextDTO text){
        super(4);
        getStyleClass().addAll("record-text-form", "edit");
        setFillWidth(true);
        textArea.setWrapText(true);
        textArea.setText(text.content);
        getChildren().addAll(
                textArea,
                createButtons()
        );
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    public void acquireFocus(){
        textArea.requestFocus();
    }

    private Node createButtons(){
        FlowPane wrapper = new FlowPane();
        Hyperlink enterLink = new Hyperlink("入力");
        Hyperlink cancelLink = new Hyperlink("キャンセル ");
        Hyperlink deleteLink = new Hyperlink("削除");
        Hyperlink shohousenLink = new Hyperlink("処方箋発行");
        Hyperlink copyLink = new Hyperlink("コピー");
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
        deleteLink.setOnAction(event -> {
            if( GuiUtil.confirm("この文章を削除しますか？") ){
                if( callback != null ){
                    callback.onDelete();
                }
            }
        });
        wrapper.getChildren().addAll(enterLink, cancelLink, deleteLink, shohousenLink, copyLink);
        return wrapper;
    }

}
