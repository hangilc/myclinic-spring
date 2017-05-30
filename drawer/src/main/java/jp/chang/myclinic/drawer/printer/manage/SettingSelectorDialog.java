package jp.chang.myclinic.drawer.printer.manage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;


/**
 * Created by hangil on 2017/05/30.
 */
public class SettingSelectorDialog extends JDialog {

    private boolean canceled;

    public SettingSelectorDialog(Window owner, Path settingDir){
        super(owner, "印刷設定の選択", Dialog.ModalityType.DOCUMENT_MODAL);
        try {
            PrinterSetting setting = new PrinterSetting(settingDir);
            setting.ensureSettingDir();
            List<String> names = setting.listNames();
            if( names.size() == 0 ){
                alert("選択できる印刷設定がありません。");
                canceled = true;
            }
        } catch(IOException ex){
            ex.printStackTrace();
            alert(ex.toString());
        }
    }

    public boolean isCanceled(){
        return canceled;
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }
}
