package jp.chang.myclinic.drawer.preview.manage;

import jp.chang.myclinic.drawer.printer.AuxSetting;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class AuxPart extends JPanel {

    interface Callback {
        default void repackRequired(){}
        default void onModify(AuxSetting newSetting){}
    }

    private AuxDisp auxDisp = new AuxDisp();
    private AuxForm auxForm = new AuxForm();
    private Callback callback = new Callback(){};

    AuxPart(){
        setLayout(new MigLayout("insets 0, hidemode 3", "", ""));
        auxForm.setVisible(false);
        auxDisp.setCallback(new AuxDisp.Callback(){
            @Override
            public void onModify() {
                auxDisp.setVisible(false);
                auxForm.setVisible(true);
                repaint();
                revalidate();
                callback.repackRequired();
            }
        });
        auxForm.setCallback(new AuxForm.Callback(){
            @Override
            public void onEnter(AuxSetting newSetting) {
                callback.onModify(newSetting);
            }

            @Override
            public void onCancel() {
                auxDisp.setVisible(true);
                auxForm.setVisible(false);
                repaint();
                revalidate();
                callback.repackRequired();
            }
        });
        add(auxDisp);
        add(auxForm);
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    void clear(){
        auxDisp.setData(null);
        auxForm.setData(null);
        auxDisp.setVisible(true);
        auxForm.setVisible(false);
    }

    void setData(AuxSetting auxSetting){
        auxDisp.setData(auxSetting);
        auxForm.setData(auxSetting);
        repaint();
        revalidate();
    }
}
