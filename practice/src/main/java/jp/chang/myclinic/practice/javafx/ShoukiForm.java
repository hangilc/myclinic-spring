package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.dto.ShoukiDTO;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.util.function.BiConsumer;

public class ShoukiForm extends Stage {

    //private static Logger logger = LoggerFactory.getLogger(ShoukiForm.class);
    private int visitId;
    private ShoukiDTO orig;
    private TextArea textArea = new TextArea();
    private BiConsumer<Integer, ShoukiDTO> callback = (visitId, shoukiDTO) -> {};

    ShoukiForm(int visitId, ShoukiDTO orig) {
        this.visitId = visitId;
        this.orig = orig;
        setTitle(orig == null ? "症状詳記の追加" : "症状詳記の編集");
        VBox root = new VBox(4);
        root.getStylesheets().add("css/Practice.css");
        root.getStyleClass().add("shouki-form");
        root.getChildren().addAll(
                createTextArea(),
                createCommands()
        );
        setScene(new Scene(root));
    }

    public int getVisitId(){
        return visitId;
    }

    public void setCallback(BiConsumer<Integer, ShoukiDTO> callback){
        this.callback = callback;
    }

    private Node createTextArea() {
        textArea.setWrapText(true);
        if (orig != null) {
            textArea.setText(orig.shouki);
        }
        return textArea;
    }

    private Node createCommands() {
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        Hyperlink deleteLink = new Hyperlink("削除");
        enterButton.setOnAction(evt -> doEnter());
        cancelButton.setOnAction(evt -> close());
        deleteLink.setOnAction(evt -> doDelete());
        hbox.getChildren().addAll(enterButton, cancelButton, deleteLink);
        return hbox;
    }

    private void doEnter() {
        if (orig == null) {
            ShoukiDTO shoukiDTO = new ShoukiDTO();
            shoukiDTO.visitId = visitId;
            shoukiDTO.shouki = textArea.getText();
            Context.frontend.updateShouki(shoukiDTO)
                    .thenAcceptAsync(result -> {
                        callback.accept(visitId, shoukiDTO);
                        close();
                    }, Platform::runLater)
                    .exceptionally(HandlerFX::exceptionally);
        } else {
            ShoukiDTO edited = ShoukiDTO.copy(orig);
            edited.shouki = textArea.getText();
            Context.frontend.updateShouki(edited)
                    .thenAcceptAsync(result -> {
                        callback.accept(visitId, edited);
                        close();
                    }, Platform::runLater)
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

    private void doDelete() {
        if (GuiUtil.confirm("この症状詳記を削除していいですか？")) {
            Context.frontend.deleteShouki(visitId)
                    .thenAcceptAsync(result -> {
                        callback.accept(visitId, null);
                        close();
                    }, Platform::runLater)
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

}
