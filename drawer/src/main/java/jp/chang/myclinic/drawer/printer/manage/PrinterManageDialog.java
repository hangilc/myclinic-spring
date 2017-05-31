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
    private String currentSetting;
    private JButton newButton = new JButton("新規印刷設定");
    private NamesComboBox namesCombo;
    private JButton editButton = new JButton("編集");
    private JButton deleteButton = new JButton("削除");
    private JButton closeButton = new JButton("閉じる");

    public PrinterManageDialog(Window owner, Path settingDir, String currentSetting) throws IOException{
        super(owner, "プリンター管理", Dialog.ModalityType.DOCUMENT_MODAL);
        this.settingDir = settingDir;
        this.currentSetting = currentSetting;
        setLayout(new MigLayout("fill", "[grow]", ""));
        add(newButton, "sizegroup btn, wrap");
        add(makeChoicePane(), "wrap");
        add(closeButton, "sizegroup btn");
        bind();
        pack();
    }

    private JComponent makeChoicePane() throws IOException {
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        namesCombo = new NamesComboBox(settingDir, currentSetting);
        panel.add(namesCombo);
        panel.add(editButton);
        panel.add(deleteButton);
        return panel;
    }

    private void bind() {
        newButton.addActionListener(event -> {
            String name = JOptionPane.showInputDialog("新規印刷設定の名称");
            if( name == null ){
                return;
            }
            DrawerPrinter printer = new DrawerPrinter();
            DrawerPrinter.DialogResult result = printer.printDialog(PrinterManageDialog.this, null, null);
            if( !result.ok ){
                return;
            }
            SettingEditorDialog confirmDialog = new SettingEditorDialog(PrinterManageDialog.this,
                    name, result.devnamesData, result.devmodeData, new AuxSetting());
            confirmDialog.setTitle("新規印刷設定");
            confirmDialog.setLocationByPlatform(true);
            confirmDialog.setVisible(true);
            if( confirmDialog.isCanceled() ){
                return;
            }
            try{
                PrinterSetting setting = new PrinterSetting(settingDir);
                setting.ensureSettingDir();
                setting.saveSetting(name, confirmDialog.getDevnamesData(),
                        confirmDialog.getDevmodeData(),
                        confirmDialog.getAuxSetting());
            } catch (IOException ex){
                ex.printStackTrace();
                throw new UncheckedIOException(ex);
            }
        });
        editButton.addActionListener(event -> {
            String name = namesCombo.getSelectedName();
            if( name == null ){
                return;
            }
            PrinterSetting printerSetting = new PrinterSetting(settingDir);
            try {
                SettingEditorDialog editor = new SettingEditorDialog(PrinterManageDialog.this,
                        name,
                        printerSetting.readDevnames(name),
                        printerSetting.readDevmode(name),
                        printerSetting.readAuxSetting(name));
                editor.setLocationByPlatform(true);
                editor.setVisible(true);
                if( !editor.isCanceled() ){
                    printerSetting.saveSetting(name, editor.getDevnamesData(),
                            editor.getDevmodeData(), editor.getAuxSetting());
                }
            } catch(IOException ex){
                ex.printStackTrace();
                alert(ex.toString());
            }
        });
        closeButton.addActionListener(event -> {
            dispose();
        });
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message.toString());
    }

}
