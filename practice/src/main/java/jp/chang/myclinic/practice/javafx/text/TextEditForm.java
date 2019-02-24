package jp.chang.myclinic.practice.javafx.text;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.practice.Globals;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.util.function.Consumer;

public class TextEditForm extends VBox {

    private int visitId;
    private int textId;
    private TextArea textArea = new TextArea();
    private Hyperlink enterLink = new Hyperlink("入力");
    private Hyperlink cancelLink = new Hyperlink("キャンセル ");
    private Hyperlink deleteLink = new Hyperlink("削除");
    private Hyperlink shohousenLink = new Hyperlink("処方箋発行");
    private Hyperlink copyLink = new Hyperlink("コピー");
    private TextLib textLib;
    private Runnable onDeletedCallback;

    public TextEditForm(TextDTO text) {
        super(4);
        this.visitId = text.visitId;
        this.textId = text.textId;
        getStyleClass().addAll("record-text-form", "edit");
        setFillWidth(true);
        textArea.setWrapText(true);
        textArea.setText(text.content);
        deleteLink.setOnAction(event -> {
            if (GuiUtil.confirm("この文章を削除しますか？")) {
                getTextLib().deleteText(textId)
                        .thenAcceptAsync(ok -> {
                            if( onDeletedCallback != null ){
                                onDeletedCallback.run();
                            }
                        }, Platform::runLater)
                        .exceptionally(HandlerFX::exceptionally);
            }
        });
        getChildren().addAll(
                textArea,
                createButtons()
        );
    }

    public void simulateClickEnterButton(){
        enterLink.fire();
    }

    public void simulateClickCancelButton(){
        cancelLink.fire();
    }

    public void simulateClickDeleteButton(){
        deleteLink.fire();
    }

    public void simulateClickShohousenButton(){
        shohousenLink.fire();
    }

    public void simulateClickCopyButton(){
        copyLink.fire();
    }

    public void simulateSetText(String text){
        textArea.setText(text);
    }

    int getTextId() {
        return textId;
    }

    public void setOnUpdated(Consumer<TextDTO> callback){
        enterLink.setOnAction(event -> {
            TextDTO textDTO = new TextDTO();
            textDTO.visitId = visitId;
            textDTO.textId = textId;
            textDTO.content = textArea.getText().trim();
            getTextLib().updateText(textDTO)
                    .thenAcceptAsync(ok -> callback.accept(textDTO), Platform::runLater)
                    .exceptionally(HandlerFX::exceptionally);
        });
    }

    public void setOnCancel(Runnable handler){
        cancelLink.setOnAction(event -> handler.run());
    }

    public void setOnDeleted(Runnable callback){
        this.onDeletedCallback = callback;
    }

    public void setOnShohousen(Runnable callback){
        shohousenLink.setOnAction(event -> callback.run());
    }

    public void setOnCopy(Runnable callback){
        copyLink.setOnAction(event -> callback.run());
    }

    private TextLib getTextLib(){
        return textLib != null ? textLib : Globals.getInstance().getTextLib();
    }

    public void setTextLib(TextLib textLib){
        this.textLib = textLib;
    }

    private Node createButtons() {
        FlowPane wrapper = new FlowPane();
        wrapper.getChildren().addAll(enterLink, cancelLink, deleteLink, shohousenLink, copyLink);
        return wrapper;
    }

}
