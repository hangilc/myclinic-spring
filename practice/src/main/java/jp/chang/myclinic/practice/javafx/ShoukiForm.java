package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.ShoukiDTO;

class ShoukiForm extends Stage {

    //private static Logger logger = LoggerFactory.getLogger(ShoukiForm.class);
    private int visitId;
    private ObjectProperty<ShoukiDTO> shoukiProperty;
    private TextArea textArea = new TextArea();

    ShoukiForm(int visitId, ObjectProperty<ShoukiDTO> shoukiProperty) {
        this.visitId = visitId;
        this.shoukiProperty = shoukiProperty;
        setTitle(shoukiProperty.getValue() == null
                ? "症状詳記の追加" : "症状詳記の編集"
        );
        VBox root = new VBox(4);
        root.getStylesheets().add("css/Practice.css");
        root.getStyleClass().add("shouki-form");
        root.getChildren().addAll(
                createTextArea(),
                createCommands()
        );
        setScene(new Scene(root));
    }

    private Node createTextArea() {
        textArea.setWrapText(true);
        if (shoukiProperty.getValue() != null) {
            textArea.setText(shoukiProperty.getValue().shouki);
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
        if (shoukiProperty.getValue() == null) {
            ShoukiDTO shoukiDTO = new ShoukiDTO();
            shoukiDTO.visitId = visitId;
            shoukiDTO.shouki = textArea.getText();
            Service.api.updateShouki(shoukiDTO)
                    .thenAcceptAsync(result -> {
                        shoukiProperty.setValue(shoukiDTO);
                        close();
                    }, Platform::runLater)
                    .exceptionally(HandlerFX::exceptionally);
        } else {
            ShoukiDTO edited = ShoukiDTO.copy(shoukiProperty.getValue());
            edited.shouki = textArea.getText();
            Service.api.updateShouki(edited)
                    .thenAcceptAsync(result -> {
                        shoukiProperty.setValue(edited);
                        close();
                    }, Platform::runLater)
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

    private void doDelete() {
        if (GuiUtil.confirm("この症状詳記を削除していいですか？")) {
            Service.api.deleteShouki(visitId)
                    .thenAcceptAsync(result -> {
                        shoukiProperty.setValue(null);
                        close();
                    }, Platform::runLater)
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

}
