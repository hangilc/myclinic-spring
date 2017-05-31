package jp.chang.myclinic.drawer.printer.manage;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;


/**
 * Created by hangil on 2017/05/30.
 */
public class SettingSelectorDialog extends JDialog {

    private boolean canceled = true;
    private String currentSetting;
    private JComboBox<String> comboBox;
    private JButton okButton = new JButton("選択");
    private JButton cancelButton = new JButton("キャンセル");

    public SettingSelectorDialog(Window owner,List<String> names, String currentSetting){
        super(owner, "印刷設定の選択", Dialog.ModalityType.DOCUMENT_MODAL);
        this.currentSetting = currentSetting;
        setupComponents(names);
        bind();
        pack();
    }

    public boolean isCanceled(){
        return canceled;
    }

    public String getSelectedItem(){
        if( isCanceled() ){
            return null;
        }
        int index = comboBox.getSelectedIndex();
        return comboBox.getItemAt(index);
    }

    private String maxName(List<String> names){
        Font font = getFont();
        FontMetrics metrics = getFontMetrics(font);
        String curName = null;
        int curSize = 0;
        for(String name: names){
            int w = metrics.stringWidth(name);
            if( w > curSize ){
                curName = name;
                curSize = w;
            }
        }
        return curName;
    }

    private void setupComponents(List<String> names){
        setLayout(new MigLayout("", "", ""));
        setupComboBox(names);
        add(comboBox);
        add(okButton, "sizegroup btn");
        add(cancelButton, "sizegroup btn");
    }

    private void setupComboBox(List<String> names){
        comboBox = new JComboBox<>(names.toArray(new String[]{}));
        String dispName = maxName(names);
        if( dispName != null ){
            comboBox.setPrototypeDisplayValue(dispName + "  ");
        }
        for(String name: names){
            if( name.equals(currentSetting) ){
                comboBox.setSelectedItem(name);
                break;
            }
        }
    }

    private void bind(){
        okButton.addActionListener(event -> {
            canceled = false;
            dispose();
        });
        cancelButton.addActionListener(event -> {
            canceled = true;
            dispose();
        });
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }
}
