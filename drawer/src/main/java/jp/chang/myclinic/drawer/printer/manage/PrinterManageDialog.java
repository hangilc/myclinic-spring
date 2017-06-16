package jp.chang.myclinic.drawer.printer.manage;

import jp.chang.myclinic.drawer.printer.AuxSetting;
import jp.chang.myclinic.drawer.printer.DrawerPrinter;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

public class PrinterManageDialog extends JDialog {

    private JButton newButton = new JButton("新規印刷設定");
    private NamesComboBox namesCombo;
    private JButton editButton = new JButton("編集");
    private JButton deleteButton = new JButton("削除");
    private JButton closeButton = new JButton("閉じる");

    public PrinterManageDialog(Window owner) {
        super(owner, "プリンター管理", Dialog.ModalityType.DOCUMENT_MODAL);
        setLayout(new MigLayout("fill", "[grow]", ""));
        add(newButton, "sizegroup btn, wrap");
        add(makeChoicePane(), "wrap");
        add(closeButton, "sizegroup btn");
        bind();
        pack();
    }

    private JComponent makeChoicePane() {
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        namesCombo = new NamesComboBox();
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
                PrinterSetting setting = PrinterSetting.INSTANCE;
                setting.ensureSettingDir();
                setting.saveSetting(name, confirmDialog.getDevnamesData(),
                        confirmDialog.getDevmodeData(),
                        confirmDialog.getAuxSetting());
                namesCombo.reload();
                repaint();
                revalidate();
                pack();
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
            PrinterSetting printerSetting = PrinterSetting.INSTANCE;
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
        deleteButton.addActionListener(event -> {
            String name = namesCombo.getSelectedName();
            if( name == null ){
                return;
            }
            if( JOptionPane.showConfirmDialog(this, "印刷設定 " + name + " を削除していいですか？",
                    "確認", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION ){
                return;
            }
            try {
                PrinterSetting printerSetting = PrinterSetting.INSTANCE;
                printerSetting.deleteSetting(name);
                namesCombo.reload();
                repaint();
                revalidate();
                pack();
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
        JOptionPane.showMessageDialog(this, message);
    }

}
