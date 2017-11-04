package jp.chang.myclinic.drawer.preview.manage;

import jp.chang.myclinic.drawer.printer.AuxSetting;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class AuxForm extends JPanel {

    interface Callback {
        default void onEnter(AuxSetting auxSetting){}
        default void onCancel(){}
    }

    private JTextField scaleField = new JTextField(4);
    private JTextField dxField = new JTextField(4);
    private JTextField dyField = new JTextField(4);
    private AuxSetting auxSetting;
    private Callback callback = new Callback(){};

    AuxForm(){
        setLayout(new MigLayout("insets 0", "", ""));
        JButton enterButton = new JButton("入力");
        enterButton.addActionListener(evt -> doEnter());
        JButton cancelButton = new JButton("キャンセル");
        cancelButton.addActionListener(evt -> {
            setData(auxSetting);
            callback.onCancel();
        });
        add(new JLabel("scale:"), "right");
        add(scaleField, "wrap");
        add(new JLabel("dx:"), "right");
        add(dxField, "split 2");
        add(new JLabel("mm"), "wrap");
        add(new JLabel("dy:"), "right");
        add(dyField, "split 2");
        add(new JLabel("mm"), "wrap");
        add(enterButton, "span, split 2");
        add(cancelButton);
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    void setData(AuxSetting auxSetting){
        this.auxSetting = auxSetting;
        if( auxSetting == null ){
            scaleField.setText("");
            dxField.setText("");
            dyField.setText("");
        } else {
            scaleField.setText("" + auxSetting.getScale());
            dxField.setText("" + auxSetting.getDx());
            dyField.setText("" + auxSetting.getDy());
        }
    }

    private void doEnter(){
        AuxSetting newSetting = new AuxSetting();
        try {
            String text = scaleField.getText();
            double value = Double.parseDouble(text);
            newSetting.setScale(value);
        } catch(NumberFormatException ex){
            alert("scale の値が不適切です。");
            return;
        }
        try {
            String text = dxField.getText();
            double value = Double.parseDouble(text);
            newSetting.setDx(value);
        } catch(NumberFormatException ex){
            alert("dx の値が不適切です。");
            return;
        }
        try {
            String text = dyField.getText();
            double value = Double.parseDouble(text);
            newSetting.setDy(value);
        } catch(NumberFormatException ex){
            alert("dy の値が不適切です。");
            return;
        }
        callback.onEnter(newSetting);
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
