package jp.chang.myclinic.drawer.printer.manage;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class SettingChooserDialog extends JDialog {

    private NamesComboBox namesCombo;
    private JButton selectButton = new JButton("選択");
    private JButton cancelButton = new JButton("キャンセル");
    private JButton clearButton = new JButton("設定をクリア");
    private String printerSetting = null;
    private boolean canceled = true;

    public SettingChooserDialog(Window owner, String prompt){
        super(owner, "印刷設定の選択", ModalityType.DOCUMENT_MODAL);
        setLayout(new MigLayout("", "", ""));
        add(new JLabel(prompt), "wrap");
        add(makeRow1(), "wrap");
        add(makeRow2());
        bind();
        pack();
    }

    public String getSelectedSetting(){
        return printerSetting;
    }

    public boolean isCanceled(){
        return canceled;
    }

    private JComponent makeRow1(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        namesCombo = new NamesComboBox();
        panel.add(namesCombo);
        panel.add(selectButton);
        panel.add(cancelButton);
        return panel;
    }

    private JComponent makeRow2(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        panel.add(clearButton);
        return panel;
    }

    private void bind(){
        selectButton.addActionListener(event -> {
            printerSetting = namesCombo.getSelectedName();
            canceled = false;
            dispose();
        });
        cancelButton.addActionListener(event -> {
            canceled = true;
            dispose();
        });
        clearButton.addActionListener(event -> {
            printerSetting = null;
            canceled = false;
            dispose();
        });
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
