package jp.chang.myclinic.scanner;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.nio.file.Paths;

class SetSavingDirDialog extends SetSettingDialogBase {

    //private static Logger logger = LoggerFactory.getLogger(SetSavingDirDialog.class);

    private StringProperty currentSavingDir = new SimpleStringProperty(Globals.savingDir.toString());

    SetSavingDirDialog() {
        super("保存先フォルダーの設定");
        setupUi();
    }

    String getSavingDir(){
        return currentSavingDir.getValue();
    }

    @Override
    protected String getDialogStyleClass() {
        return "set-saving-dir-dialog";
    }

    @Override
    protected String getChangeButtonLabel() {
        return "参照";
    }

    @Override
    ObservableValue<String> getCurrentValue() {
        return currentSavingDir;
    }

    @Override
    void doChange() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("保存先フォルダー");
        directoryChooser.setInitialDirectory(Paths.get(currentSavingDir.getValue()).toFile());
        File dir = directoryChooser.showDialog(this);
        if( dir != null ){
            currentSavingDir.setValue(dir.toString());
        }
    }

}
