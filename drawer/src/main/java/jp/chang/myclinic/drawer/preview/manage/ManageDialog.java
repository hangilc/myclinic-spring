package jp.chang.myclinic.drawer.preview.manage;

import jp.chang.myclinic.drawer.printer.AuxSetting;
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
    private DevPart devPart = new DevPart();
    private AuxPart auxPart = new AuxPart();

    public ManageDialog(PrintManager printManager, List<String> names){
        this.printManager = printManager;
        setLayout(new MigLayout("", "", ""));
        setTitle("印刷設定の管理");
        namesCombo = new JComboBox<>();
        namesCombo.setEditable(false);
        names.forEach(name -> namesCombo.addItem(name));
        start(getSelectedSettingName());
        namesCombo.addActionListener(evt -> start(getSelectedSettingName()));
        add(makeSelectBox(), "wrap");
        add(devPart, "wrap");
        add(auxPart);
        pack();
    }

    private Component makeSelectBox(){
        JPanel panel = new JPanel(new MigLayout("", "", ""));
        panel.add(namesCombo);
        return panel;
    }

    private String getSelectedSettingName(){
        int i = namesCombo.getSelectedIndex();
        return namesCombo.getItemAt(i);
    }

    private void start(String settingName){
        if( settingName == null ){
            devPart.clear();
            auxPart.clear();
            return;
        }
        try {
            byte[] devnames = printManager.readDevnames(settingName);
            byte[] devmode = printManager.readDevmode(settingName);
            AuxSetting auxSetting = printManager.readAuxSetting(settingName);
            devPart.setData(devnames, devmode);
            auxPart.setData(auxSetting);
        } catch(IOException ex){
            logger.error("印刷設定の取得に失敗しました（{}）。", settingName, ex);
            alert("印刷設定の取得に失敗しました");
        }
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
