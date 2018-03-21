package jp.chang.myclinic.hotline.javafx;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class EditDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(EditDialog.class);
    private TextArea textArea = new TextArea();

    EditDialog(String text) {
        VBox root = new VBox(4);
        root.getStyleClass().add("edit-dialog");
        root.getStylesheets().add("Hotline.css");
        root.setStyle("-fx-padding:10px");
        root.getChildren().addAll(
                createTextArea(text)
        );
        setScene(new Scene(root));
    }

    private Node createTextArea(String text){
        textArea.setWrapText(true);
        textArea.setEditable(false);
        textArea.setText(text);
        return textArea;
    }

}
