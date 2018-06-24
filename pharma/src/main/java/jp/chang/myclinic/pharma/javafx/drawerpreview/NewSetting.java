package jp.chang.myclinic.pharma.javafx.drawerpreview;

import javafx.scene.control.TextInputDialog;
import jp.chang.myclinic.drawer.printer.AuxSetting;
import jp.chang.myclinic.drawer.printer.DrawerPrinter;
import jp.chang.myclinic.drawer.printer.PrinterEnv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.Consumer;

public class NewSetting {

    private static Logger logger = LoggerFactory.getLogger(NewSetting.class);

    private NewSetting() { }

    public static void createNewPrinterSetting(PrinterEnv printerEnv, Consumer<String> cb){
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("新規印刷設定の名前");
        textInputDialog.setHeaderText(null);
        textInputDialog.setContentText("新規印刷設定の名前：");
        Optional<String> result = textInputDialog.showAndWait();
        if (result.isPresent()) {
            String name = result.get();
            if (printerEnv.settingExists(name)) {
                GuiUtil.alertError(name + " という設定名は既に存在します。");
                return;
            }
            DrawerPrinter drawerPrinter = new DrawerPrinter();
            DrawerPrinter.DialogResult dialogResult = drawerPrinter.printDialog(null, null);
            if (dialogResult.ok) {
                byte[] devmode = dialogResult.devmodeData;
                byte[] devnames = dialogResult.devnamesData;
                AuxSetting auxSetting = new AuxSetting();
                try {
                    printerEnv.saveSetting(name, devnames, devmode, auxSetting);
                    EditSettingDialog editSettingDialog = new EditSettingDialog(
                            printerEnv, name, devmode, devnames, auxSetting);
                    editSettingDialog.showAndWait();
                    cb.accept(name);
                } catch(Exception e){
                    logger.error("Failed to save printer settng.", e);
                    GuiUtil.alertException("印刷設定の保存に失敗しました。", e);
                }
            }
        }
    }

}
