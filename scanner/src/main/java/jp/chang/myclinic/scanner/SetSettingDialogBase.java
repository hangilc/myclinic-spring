package jp.chang.myclinic.scanner;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

abstract class SetSettingDialogBase extends Stage {

    //private static Logger logger = LoggerFactory.getLogger(SetSettingDialogBase.class);
    private BooleanProperty saveToSetting = new SimpleBooleanProperty(true);
    private boolean enterPushed = false;

    SetSettingDialogBase(String title) {
        setTitle(title);
    }

    protected void setupUi(){
        Parent mainPane = createMainPane();
        if( getDialogStyleClass() != null ){
            mainPane.getStyleClass().add(getDialogStyleClass());
        }
        mainPane.getStylesheets().add("Scanner.css");
        setScene(new Scene(mainPane));
    }

    boolean isEnterPushed(){
        return enterPushed;
    }

    boolean saveToSettingSelected(){
        return saveToSetting.getValue();
    }

    protected String getDialogStyleClass(){
        return null;
    }

    private Parent createMainPane(){
        VBox vbox = new VBox(4);
        vbox.setAlignment(Pos.CENTER);
        Text currentSetting = new Text();
        currentSetting.textProperty().bind(getCurrentValue());
        TextFlow dispFlow = new TextFlow(currentSetting);
        dispFlow.setTextAlignment(TextAlignment.CENTER);
        Button changeButton = new Button(getChangeButtonLabel());
        changeButton.setOnAction(evt -> doChange());
        HBox changeButtonBox = new HBox(4);
        changeButtonBox.setAlignment(Pos.CENTER);
        changeButtonBox.getChildren().add(changeButton);
        addChangeButton(changeButtonBox);
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
                dispFlow,
                changeButtonBox,
                commandBox
        );
        return vbox;
    }

    abstract ObservableValue<String> getCurrentValue();

    protected String getChangeButtonLabel(){
        return "変更";
    }

    abstract void doChange();

    protected void addChangeButton(Pane wrapper){

    }

}
