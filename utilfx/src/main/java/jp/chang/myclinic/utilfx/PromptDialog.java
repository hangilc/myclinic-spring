package jp.chang.myclinic.utilfx;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PromptDialog extends Stage {

    public static String askForString(String title, String prompt, String initialValue, Node node){
        return askForString(title, prompt, initialValue, node.getScene().getWindow());
    }

    public static String askForString(String title, String prompt, String initialValue, Window owner){
        PromptDialog dialog = new PromptDialog(title, prompt, initialValue, owner);
        dialog.showAndWait();
        return dialog.cancel ? null : dialog.textField.getText();
    }

    private TextField textField = new TextField();
    private boolean cancel = true;

    private PromptDialog(String title, String prompt, String initialValue, Window owner) {
        setTitle(title);
        VBox vbox = new VBox(4);
        vbox.setStyle("-fx-padding: 10px");
        vbox.setPrefWidth(260);
        HBox commands = new HBox(4);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        enterButton.setOnAction(evt -> { this.cancel = false; close(); });
        cancelButton.setOnAction(evt -> close());
        commands.getChildren().addAll(enterButton, cancelButton);
        vbox.getChildren().addAll(
                new Label(prompt),
                textField,
                commands
        );
        setScene(new Scene(vbox));
        initOwner(owner);
        initModality(Modality.WINDOW_MODAL);
        sizeToScene();
    }

}
