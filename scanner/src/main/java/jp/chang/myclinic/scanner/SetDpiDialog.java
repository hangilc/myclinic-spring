package jp.chang.myclinic.scanner;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import jp.chang.myclinic.utilfx.GuiUtil;

class SetDpiDialog extends Stage {

    //private static Logger logger = LoggerFactory.getLogger(SetDpiDialog.class);
    private IntegerProperty dpi = new SimpleIntegerProperty(200);
    private BooleanProperty saveToSetting = new SimpleBooleanProperty(true);
    private boolean enterPushed = false;

    SetDpiDialog() {
        setTitle("ＤＰＩの設定");
        dpi.setValue(Globals.dpi);
        Parent mainPane = createMainPane();
        mainPane.getStyleClass().add("set-dpi-dialog");
        mainPane.getStylesheets().add("Scanner.css");
        setScene(new Scene(mainPane));
    }

    boolean isEnterPushed(){
        return enterPushed;
    }

    int getDpi(){
        return dpi.getValue();
    }

    boolean saveToSettingSelected(){
        return saveToSetting.getValue();
    }

    private Parent createMainPane(){
        VBox vbox = new VBox(4);
        vbox.setAlignment(Pos.CENTER);
        Text currentSetting = new Text();
        currentSetting.textProperty().bind(Bindings.convert(dpi));
        TextFlow dispFlow = new TextFlow(currentSetting);
        dispFlow.setTextAlignment(TextAlignment.CENTER);
        Button changeButton = new Button("変更");
        changeButton.setOnAction(evt -> doChange());
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
                changeButton,
                commandBox
        );
        return vbox;
    }

    private void doChange(){
        GuiUtil.askForString("DPIの値の入力", dpi.getValue().toString()).ifPresent(input -> {
            try {
                int intValue = Integer.parseInt(input);
                dpi.setValue(intValue);
            } catch(NumberFormatException ex){
                GuiUtil.alertError("dpiの入力が不適切です。");
            }
        });
    }

}
