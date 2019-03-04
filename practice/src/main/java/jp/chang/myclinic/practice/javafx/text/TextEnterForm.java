package jp.chang.myclinic.practice.javafx.text;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.practice.javafx.ExecEnv;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.util.function.Consumer;

public class TextEnterForm extends VBox {

    private TextArea textArea = new TextArea();
    private Hyperlink enterLink = new Hyperlink("入力");
    private Hyperlink cancelLink = new Hyperlink("キャンセル ");
    private int visitId;
    private ExecEnv execEnv;

    public TextEnterForm(int visitId, ExecEnv execEnv) {
        super(4);
        this.visitId = visitId;
        this.execEnv = execEnv;
        getStyleClass().addAll("record-text-form", "enter");
        setFillWidth(true);
        textArea.setWrapText(true);
        getChildren().addAll(
                textArea,
                createButtons()
        );
    }

    private TextDTO getFormTextDTO(){
        TextDTO textDTO = new TextDTO();
        textDTO.visitId = visitId;
        textDTO.content = textArea.getText().trim();
        return textDTO;
    }

    public void setOnEntered(Consumer<TextDTO> handler){
        enterLink.setOnAction(event -> {
            TextDTO textDTO = getFormTextDTO();
            execEnv.restService.enterText(getFormTextDTO())
                    .thenAcceptAsync(textId -> {
                        textDTO.textId = textId;
                        handler.accept(textDTO);
                    }, Platform::runLater)
                    .exceptionally(HandlerFX::exceptionally);
        });
    }

    public void setOnCancel(Runnable handler){
        cancelLink.setOnAction(event -> handler.run());
    }

    public void simulateClickCancelButton(){
        cancelLink.fire();
    }

    public void simulateSetText(String text){
        textArea.setText(text);
    }

    public void simulateClickEnterButton(){
        enterLink.fire();
    }

    private Node createButtons(){
        HBox hbox = new HBox(4);
        hbox.getChildren().addAll(enterLink, cancelLink);
        return hbox;
    }

}
