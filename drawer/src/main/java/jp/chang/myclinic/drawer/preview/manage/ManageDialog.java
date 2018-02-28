package jp.chang.myclinic.drawer.preview.manage;

import jp.chang.myclinic.drawer.lib.Link;
import jp.chang.myclinic.drawer.printer.AuxSetting;
import jp.chang.myclinic.drawer.printer.DrawerPrinter;
import jp.chang.myclinic.drawer.printer.manager.PrinterEnv;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class ManageDialog extends JDialog {

    private static final Logger logger = LoggerFactory.getLogger(ManageDialog.class);

    private PrinterEnv printManager;
    private JComboBox<String> namesCombo;
    private DevPart devPart = new DevPart();
    private AuxPart auxPart = new AuxPart();

    public ManageDialog(PrinterEnv printManager, List<String> names){
        this.printManager = printManager;
        setLayout(new MigLayout("", "", ""));
        setTitle("印刷設定の管理");
        devPart.setBorder(new CompoundBorder(
                BorderFactory.createTitledBorder("プリンター設定"),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        auxPart.setBorder(new CompoundBorder(
                BorderFactory.createTitledBorder("その他の設定"),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        devPart.setCallback(new DevPart.Callback(){
            @Override
            public void onModify(byte[] devnames, byte[] devmode) {
                String name = getSelectedSettingName();
                if( name == null || printManager == null ){
                    return;
                }
                DrawerPrinter drawerPrinter = new DrawerPrinter();
                DrawerPrinter.DialogResult result = drawerPrinter.printDialog(devmode, devnames);
                if( !result.ok ){
                    return;
                }
                devnames = result.devnamesData;
                devmode = result.devmodeData;
                try {
                    printManager.saveSetting(name, devnames, devmode);
                    devPart.setData(devnames, devmode);
                    pack();
                } catch (PrinterEnv.SettingDirNotSuppliedException e) {
                    logger.error("printer setting dir not specified", e);
                    alert("印刷設定の保存場所が設定されていません。");
                } catch (IOException e) {
                    logger.error("Failed to save printer setting", e);
                    alert("印刷設定の保存に失敗しました。");
                }
            }
        });
        auxPart.setCallback(new AuxPart.Callback(){
            @Override
            public void onModify(AuxSetting newSetting) {
                String name = getSelectedSettingName();
                if( name == null || printManager == null ){
                    return;
                }
                try {
                    printManager.saveSetting(name, newSetting);
                    auxPart.clear();
                    auxPart.setData(newSetting);
                    pack();
                } catch (PrinterEnv.SettingDirNotSuppliedException e) {
                    logger.error("printer setting dir not specified", e);
                    alert("印刷設定の保存場所が設定されていません。");
                } catch (IOException e) {
                    logger.error("Failed to save printer setting", e);
                    alert("印刷設定の保存に失敗しました。");
                }
            }

            @Override
            public void repackRequired() {
                pack();
            }
        });
        namesCombo = new JComboBox<>();
        namesCombo.setEditable(false);
        names.forEach(name -> namesCombo.addItem(name));
        start(getSelectedSettingName());
        namesCombo.addActionListener(evt -> start(getSelectedSettingName()));
        add(makeSelectBox(), "wrap");
        add(devPart, "growx, wrap");
        add(auxPart, "growx");
        pack();
    }

    private Component makeSelectBox(){
        JPanel panel = new JPanel(new MigLayout("", "", ""));
        Link delLink = new Link("削除");
        delLink.setCallback(evt -> doDel());
        Link newLink = new Link("新規");
        newLink.setCallback(evt -> doNew());
        panel.add(namesCombo);
        panel.add(delLink);
        panel.add(newLink);
        return panel;
    }

    private void doDel(){
        if( printManager == null ){
            logger.error("No printManager supplied");
            alert("No printManager supplied");
            return;
        }
        String name = getSelectedSettingName();
        if( name == null ){
            return;
        }
        if( !confirm("本当に " + name + " 設定を削除しますか？") ){
            return;
        }
        try {
            printManager.deleteSetting(name);
        } catch (IOException e) {
            logger.error("Failed to delete printer setting ({})", name, e);
            alert("Failed to delete printer setting.");
        }
        updateSettingNames();
    }

    private void doNew(){
        if( printManager == null ){
            logger.error("No printManager supplied");
            alert("No printManager supplied");
            return;
        }
        String name = JOptionPane.showInputDialog(this, "新しい設定の名前");
        if( name == null ){
            return;
        }
        try {
            printManager.createNewSetting(name);
            updateSettingNames();
            alert(name + "が追加されました。");
        } catch(PrinterEnv.SettingDirNotSuppliedException ex){
            logger.error("Setting dir not specified", ex);
            alert("Setting dir is not specified.");
        } catch(IOException ex){
            logger.error("", ex);
        }
    }

    private void updateSettingNames(){
        if( printManager == null ){
            logger.error("No printManager supplied");
            alert("No printManager supplied");
            return;
        }
        try {
            List<String> names = printManager.listNames();
            namesCombo.removeAllItems();
            names.forEach(name -> {
                namesCombo.addItem(name);
            });
            start(getSelectedSettingName());
        } catch (IOException e) {
            logger.error("IOException while listing setting names.", e);
        }

    }

    private String getSelectedSettingName(){
        int i = namesCombo.getSelectedIndex();
        return namesCombo.getItemAt(i);
    }

    private void start(String settingName){
        if( settingName == null ){
            devPart.clear();
            auxPart.clear();
            pack();
            return;
        }
        try {
            byte[] devnames = printManager.readDevnames(settingName);
            byte[] devmode = printManager.readDevmode(settingName);
            AuxSetting auxSetting = printManager.readAuxSetting(settingName);
            devPart.setData(devnames, devmode);
            auxPart.setData(auxSetting);
            pack();
        } catch(IOException ex){
            logger.error("印刷設定の取得に失敗しました（{}）。", settingName, ex);
            alert("印刷設定の取得に失敗しました");
        }
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

    private boolean confirm(String message){
        int choice = JOptionPane.showConfirmDialog(this, message, "確認", JOptionPane.YES_NO_OPTION);
        return choice == JOptionPane.YES_OPTION;
    }

}
