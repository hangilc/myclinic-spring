package jp.chang.myclinic.drawer.preview.manage;

import jp.chang.myclinic.drawer.printer.manager.PrintManager;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class ManageDialog extends JDialog {

    private static final Logger logger = LoggerFactory.getLogger(ManageDialog.class);

    private PrintManager printManager;
    private JComboBox<String> namesCombo;
    private DevDisp devDisp = new DevDisp();

    public ManageDialog(PrintManager printManager, List<String> names,  String settingName){
        this.printManager = printManager;
        setLayout(new MigLayout("", "", ""));
        setTitle("印刷設定の管理");
        namesCombo = new JComboBox<>();
        namesCombo.setEditable(false);
        names.forEach(name -> namesCombo.addItem(name));
        setSelectedSettingName(settingName);
        add(makeSelectBox(), "wrap");
        add(devDisp, "wrap");
        pack();
    }

    private Component makeSelectBox(){
        JPanel panel = new JPanel(new MigLayout("", "", ""));
        panel.add(namesCombo);
        return panel;
    }

    private void setSelectedSettingName(String name){
        for(int i=0;i<namesCombo.getItemCount();i++){
            String item = namesCombo.getItemAt(i);
            if( item.equals(name) ){
                namesCombo.setSelectedIndex(i);
                break;
            }
        }
        if( name == null ){
            return;
        }
        try {
            byte[] devnames = printManager.readDevnames(name);

        } catch(IOException ex){
            logger.error("印刷設定の取得に失敗しました（{}）。", name, ex);
            alert("印刷設定の取得に失敗しました");
        }
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
