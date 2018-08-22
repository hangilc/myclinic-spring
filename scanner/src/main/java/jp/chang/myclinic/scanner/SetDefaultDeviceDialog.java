package jp.chang.myclinic.scanner;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import jp.chang.wia.Wia;

class SetDefaultDeviceDialog extends SetSettingDialogBase {

    //private static Logger logger = LoggerFactory.getLogger(SetDefaultDeviceDialog.class);

    private StringProperty defaultDevice = new SimpleStringProperty(Globals.defaultDevice);

    SetDefaultDeviceDialog() {
        super("スキャナーデバイスの設定");
        setupUi();
    }

    String getDefaultDevice(){
        return defaultDevice.getValue();
    }

    @Override
    protected String getDialogStyleClass() {
        return "set-default-device-dialog";
    }

    @Override
    ObservableValue<String> getCurrentValue() {
        return defaultDevice;
    }

    @Override
    protected void addChangeButton(Pane wrapper) {
        Button clearButton = new Button("クリア");
        clearButton.setOnAction(evt -> defaultDevice.setValue(null));
        wrapper.getChildren().add(clearButton);
    }

    @Override
    void doChange() {
        String newValue = Wia.pickScannerDevice();
        defaultDevice.setValue(newValue);
    }
}
