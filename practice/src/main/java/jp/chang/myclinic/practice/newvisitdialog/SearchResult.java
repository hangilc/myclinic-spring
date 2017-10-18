package jp.chang.myclinic.practice.newvisitdialog;

import jp.chang.myclinic.dto.PatientDTO;

import javax.swing.*;

class SearchResult extends JList<PatientDTO> {

    SearchResult(){
        setCellRenderer((list, result, index, isSelected, cellHasFocus) -> {
            String labelText = String.format("[%04d] %s %s (%s %s)",
                    result.patientId,
                    result.lastName,
                    result.firstName,
                    result.lastNameYomi,
                    result.firstNameYomi);
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
