package jp.chang.myclinic.practice.javafx.text;

import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.utilfx.GuiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextEditForm extends VBox {

    private static Logger logger = LoggerFactory.getLogger(TextEditForm.class);
    private int visitId;
    private int textId;
    private String content;
    private TextArea textArea = new TextArea();
    private Hyperlink enterLink = new Hyperlink("入力");
    private Hyperlink cancelLink = new Hyperlink("キャンセル ");
    private Hyperlink deleteLink = new Hyperlink("削除");
    private Hyperlink shohousenLink = new Hyperlink("処方箋発行");
    private Hyperlink copyLink = new Hyperlink("コピー");

    TextEditForm(TextDTO text) {
        super(4);
        this.visitId = text.visitId;
        this.textId = text.textId;
        this.content = text.content;
        getStyleClass().addAll("record-text-form", "edit");
        setFillWidth(true);
        textArea.setWrapText(true);
        textArea.setText(text.content);
        getChildren().addAll(
                textArea,
                createButtons()
        );

    }

    int getTextId() {
        return textId;
    }

    private Node createButtons() {
        FlowPane wrapper = new FlowPane();
        enterLink.setOnAction(event -> doUpdate());
        cancelLink.setOnAction(event -> {
            if (callback != null) {
                callback.onCancel();
            }
        });
        deleteLink.setOnAction(event -> {
            if (GuiUtil.confirm("この文章を削除しますか？")) {
                if (callback != null) {
                    callback.onDelete();
                }
            }
        });
        shohousenLink.setOnAction(evt -> doShohousen());
        copyLink.setOnAction(evt -> doCopy());
        wrapper.getChildren().addAll(enterLink, cancelLink, deleteLink, shohousenLink, copyLink);
        return wrapper;
    }

}
