package jp.chang.myclinic.hotline.javafx;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

class EditDialog extends Stage {

    //private static Logger logger = LoggerFactory.getLogger(EditDialog.class);
    private TextArea textArea = new TextArea();

    EditDialog(String text) {
        VBox root = new VBox(4);
        root.getStyleClass().add("edit-dialog");
        root.getStylesheets().add("Hotline.css");
        root.getChildren().addAll(
                createTextArea(text),
                createCommands()
        );
        setScene(new Scene(root));
    }

    private Node createTextArea(String text){
        textArea.setWrapText(true);
        textArea.setEditable(false);
        textArea.setText(text);
        return textArea;
    }

    private Node createCommands(){
        HBox hbox = new HBox(4);
        Button enterSelectionButton = new Button("選択部を入力");
        Button enterAllButton = new Button("全部入力");
        enterSelectionButton.setDisable(true);
        enterSelectionButton.setOnAction(evt -> doEnterSelection());
        enterAllButton.setOnAction(evt -> doEnterAll());
        hbox.getChildren().addAll(
                enterSelectionButton,
                enterAllButton
        );
        textArea.selectedTextProperty().addListener((obs, oldValue, newValue) -> {
            enterSelectionButton.setDisable(newValue == null || newValue.isEmpty());
        });
        return hbox;
    }

    private void doEnterSelection(){
        String sel = textArea.getSelectedText();
        onEnter(this, sel);
    }

    private void doEnterAll(){
        onEnter(this, textArea.getText());
    }

    protected void onEnter(EditDialog self, String text){

    }

}
