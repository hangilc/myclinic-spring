package jp.chang.myclinic.scanner;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.nio.file.Paths;

class SetRegularDocSavingHintDialog extends SetSettingDialogBase {

    //private static Logger logger = LoggerFactory.getLogger(SetRegularDocSavingHintDialog.class);
    private StringProperty savingDir = new SimpleStringProperty(Globals.regularDocSavingDirHint.toString());

    SetRegularDocSavingHintDialog() {
        super("一般書類の保存先の設定");
        setupUi();
    }

    String getSavingHint(){
        return savingDir.getValue();
    }

    @Override
    protected String getDialogStyleClass() {
        return "set-regular-doc-saving-dir-hint-dialog";
    }

    @Override
    ObservableValue<String> getCurrentValue() {
        return savingDir;
    }

    @Override
    void doChange() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("一般書類の保存先");
        directoryChooser.setInitialDirectory(Paths.get(savingDir.getValue()).toFile());
        File dir = directoryChooser.showDialog(this);
        if( dir != null ){
            savingDir.setValue(dir.toString());
        }
    }
}
