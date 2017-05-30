package jp.chang.myclinic.drawer.printer.manage;

import jp.chang.myclinic.drawer.printer.AuxSetting;
import jp.chang.myclinic.drawer.printer.DrawerPrinter;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

/**
 * Created by hangil on 2017/05/26.
 */
public class PrinterManageDialog extends JDialog {

    private Path settingDir;
    private JButton newButton = new JButton("新規印刷設定");
    private JButton closeButton = new JButton("閉じる");

    public PrinterManageDialog(Window owner, Path settingDir){
        super(owner, "プリンター管理", Dialog.ModalityType.APPLICATION_MODAL);
        this.settingDir = settingDir;
        setLayout(new MigLayout("fill", "[grow]", ""));
        add(newButton, "wrap");
        add(makeSouth(), "right");
        bind();
        pack();
    }

    private JComponent makeSouth(){
        return closeButton;
    }

    private void bind() {
        newButton.addActionListener(event -> {
            String name = JOptionPane.showInputDialog("新規印刷設定の名称");
            if( name == null ){
                return;
            }
            DrawerPrinter printer = new DrawerPrinter();
            DrawerPrinter.DialogResult result = printer.printDialog(null, null);
            if( !result.ok ){
                return;
            }
            CreatedSettingDialog confirmDialog = new CreatedSettingDialog(this, name, result.devnamesData,
                    result.devmodeData, new AuxSetting());
            confirmDialog.setLocationByPlatform(true);
            confirmDialog.setVisible(true);
            if( confirmDialog.isCanceled() ){
                dispose();
                return;
            }
            try{
                PrinterSetting setting = new PrinterSetting(settingDir);
                setting.ensureSettingDir();
                setting.saveSetting(name, confirmDialog.getDevnamesData(),
                        confirmDialog.getDevmodeData(),
                        confirmDialog.getAuxSetting());
                dispose();
            } catch (IOException ex){
                ex.printStackTrace();
                throw new UncheckedIOException(ex);
            }
        });
        closeButton.addActionListener(event -> {
            dispose();
        });
    }

}
