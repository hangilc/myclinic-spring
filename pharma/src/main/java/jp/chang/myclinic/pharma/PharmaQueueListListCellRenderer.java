package jp.chang.myclinic.pharma;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.PharmaQueueFullDTO;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hangil on 2017/06/11.
 */
public class PharmaQueueListListCellRenderer extends JLabel implements ListCellRenderer<PharmaQueueFullDTO> {
    @Override
    public Component getListCellRendererComponent(JList<? extends PharmaQueueFullDTO> list, PharmaQueueFullDTO value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        PatientDTO patient = value.patient;
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
