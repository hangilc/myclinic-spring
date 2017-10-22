package jp.chang.myclinic.drawer.printer.manage;

import jp.chang.myclinic.drawer.printer.AuxSetting;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

/**
 * Created by hangil on 2017/05/30.
 */
public class AuxSettingEditor extends JDialog {

    private AuxSetting auxSetting;
    private JTextField dxField;
    private JTextField dyField;
    private JTextField scaleField;
    private JButton enterButton;
    private JButton cancelButton;
    private boolean canceled;
    private NumberFormat numberFormat = NumberFormat.getNumberInstance();

    public AuxSettingEditor(Window owner, AuxSetting auxSetting){
        super(owner, "印刷設定（その他）の編集", ModalityType.DOCUMENT_MODAL);
        this.auxSetting = auxSetting;
        setupUI();
    }

    public boolean isCanceled(){
        return canceled;
    }

    private void setupUI(){
        setLayout(new MigLayout("", "", ""));
        add(makeFields(), "wrap");
        add(makeSouth(), "right");
        bind();
        pack();
    }

    private JComponent makeFields(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        dxField = new JTextField(6);
        dxField.setText(numberFormat.format(auxSetting.getDx()));
        dyField = new JTextField(6);
        dyField.setText(numberFormat.format(auxSetting.getDy()));
        scaleField = new JTextField(6);
        scaleField.setText(numberFormat.format(auxSetting.getScale()));
        panel.add(new JLabel("dx"));
        panel.add(dxField);
        panel.add(new JLabel("mm"), "wrap");
        panel.add(new JLabel("dy"));
        panel.add(dyField);
        panel.add(new JLabel("mm"), "wrap");
        panel.add(new JLabel("scale"));
        panel.add(scaleField);
        panel.add(new JLabel(""), "wrap");
        return panel;
    }

    private JComponent makeSouth(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        enterButton = new JButton("入力");
        cancelButton = new JButton("キャンセル");
        panel.add(enterButton, "sizegroup btn");
        panel.add(cancelButton, "sizegroup btn");
        return panel;
    }

    private void bind(){
        enterButton.addActionListener(event -> {
            double dx, dy, scale;
            try {
                dx = Double.parseDouble(dxField.getText());
            } catch(NumberFormatException ex){
                alert("dx の入力が不適切です。");
                return;
            }
            try {
                dy = Double.parseDouble(dyField.getText());
            } catch(NumberFormatException ex){
                alert("dy の入力が不適切です。");
                return;
            }
            try {
                scale = Double.parseDouble(scaleField.getText());
            } catch(NumberFormatException ex){
                alert("scale の入力が不適切です。");
                return;
            }
            auxSetting.setDx(dx);
            auxSetting.setDy(dy);
            auxSetting.setScale(scale);
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
