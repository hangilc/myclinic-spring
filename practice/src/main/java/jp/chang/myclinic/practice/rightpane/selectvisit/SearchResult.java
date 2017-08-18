package jp.chang.myclinic.practice.rightpane.selectvisit;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.WqueueFullDTO;

import javax.swing.*;

class SearchResult extends JList<WqueueFullDTO> {

    SearchResult(){
        setCellRenderer((list, result, index, isSelected, cellHasFocus) -> {
            PatientDTO patient = result.patient;
            String labelText = String.format("[%04d] %s %s (%s %s)",
                    patient.patientId,
                    patient.lastName,
                    patient.firstName,
                    patient.lastNameYomi,
                    patient.firstNameYomi);
            JLabel label = new JLabel();
            label.setText(labelText);
            if( isSelected ){
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
            } else {
                label.setBackground(list.getBackground());
                label.setForeground(list.getForeground());
            }
            label.setOpaque(true);
            return label;
        });
    }
}
