package jp.chang.myclinic.pharma;

import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.PharmaQueueFullDTO;
import jp.chang.myclinic.dto.WqueueDTO;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hangil on 2017/06/11.
 */
public class PharmaQueueListListCellRenderer extends JLabel implements ListCellRenderer<PharmaQueueFullDTO> {

    private Icon waitCashierImage;
    private Icon waitDrugImage;
    private Icon blankImage;

    public PharmaQueueListListCellRenderer(Icon waitCashierImage, Icon waitDrugImage){
        this.waitCashierImage = waitCashierImage;
        this.waitDrugImage = waitDrugImage;
        this.blankImage = new BlankIcon(12, 12);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends PharmaQueueFullDTO> list, PharmaQueueFullDTO value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        PatientDTO patient = value.patient;
        WqueueDTO wqueue = value.wqueue;
        if( wqueue != null ){
            switch(WqueueWaitState.fromCode(wqueue.waitState)){
                case WaitCashier: {
                    setForeground(Color.GREEN);
                    break;
                }
                case WaitDrug: {
                    setForeground(Color.RED);
                    break;
                }
                default: {
                    setForeground(Color.BLACK);
                    break;
                }
            }
        }
        String label = String.format("%s%s（%s%s）", patient.lastName, patient.firstName,
                patient.lastNameYomi, patient.firstNameYomi);
        setText(label);
        if( isSelected ){
            setBackground(list.getSelectionBackground());
        } else {
            setBackground(list.getBackground());
        }
        return this;
    }
}
