package jp.chang.myclinic.scanner;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Paths;

class SetSavingDirDialog extends Stage {

    //private static Logger logger = LoggerFactory.getLogger(SetSavingDirDialog.class);
    private StringProperty currentSavingDir = new SimpleStringProperty();
    private BooleanProperty saveToSetting = new SimpleBooleanProperty(true);
    private boolean enterPushed = false;

    SetSavingDirDialog() {
        setTitle("保存先フォルダーの設定");
        currentSavingDir.setValue(Globals.savingDir.toString());
        Parent mainPane = createMainPane();
        mainPane.getStyleClass().add("set-saving-dir-dialog");
        mainPane.getStylesheets().add("Scanner.css");
        setScene(new Scene(mainPane));
    }

    boolean isEnterPushed(){
        return enterPushed;
    }

    String getSavingDir(){
        return currentSavingDir.getValue();
    }

    boolean saveToSettingSelected(){
        return saveToSetting.getValue();
    }

    private Parent createMainPane(){
        VBox vbox = new VBox(4);
        vbox.setAlignment(Pos.CENTER);
        Text currentSavingDirText = new Text();
        currentSavingDirText.textProperty().bind(currentSavingDir);
        Button chooseButton = new Button("参照");
        chooseButton.setOnAction(evt -> doChoose());
        CheckBox saveToSettingCheck = new CheckBox("設定ファイルに保存する");
        saveToSettingCheck.selectedProperty().bindBidirectional(saveToSetting);
        Button enterButton = new Button("決定");
        Button cancelButton = new Button("キャンセル");
        enterButton.setOnAction(evt -> {
            enterPushed = true;
            close();
        });
        cancelButton.setOnAction(evt -> close());
        HBox commandBox = new HBox(4);
        commandBox.setAlignment(Pos.CENTER_LEFT);
        commandBox.getChildren().addAll(saveToSettingCheck, enterButton, cancelButton);
        vbox.getChildren().addAll(
                new TextFlow(currentSavingDirText),
                chooseButton,
                commandBox
        );
        return vbox;
    }

    private void doChoose(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(Paths.get(currentSavingDir.getValue()).toFile());
        File dir = directoryChooser.showDialog(this);
        if( dir != null ){
            currentSavingDir.setValue(dir.toString());
        }
    }

}
