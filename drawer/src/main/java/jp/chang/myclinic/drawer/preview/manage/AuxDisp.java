package jp.chang.myclinic.drawer.preview.manage;

import jp.chang.myclinic.drawer.printer.AuxSetting;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class AuxDisp extends JPanel {

    interface Callback {
        default void onModify(){}
    }

    private JLabel scaleLabel = new JLabel();
    private JLabel dxLabel = new JLabel();
    private JLabel dyLabel = new JLabel();
    private Callback callback = new Callback(){};

    AuxDisp(){
        setLayout(new MigLayout("insets 0, gapy 3", "", ""));
        JButton modifyButton = new JButton("変更");
        modifyButton.addActionListener(evt -> callback.onModify());
        add(new JLabel("scale:"), "right");
        add(scaleLabel, "wrap");
        add(new JLabel("dx:"), "right");
        add(dxLabel, "wrap");
        add(new JLabel("dy:"), "right");
        add(dyLabel, "wrap");
        add(modifyButton);
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    void setData(AuxSetting auxSetting){
        if( auxSetting == null ){
            scaleLabel.setText("");
            dxLabel.setText("");
            dyLabel.setText("");
        } else {
            scaleLabel.setText("" + auxSetting.getScale());
            dxLabel.setText(auxSetting.getDx() + " mm");
            dyLabel.setText(auxSetting.getDy() + " mm");
        }
    }

}
