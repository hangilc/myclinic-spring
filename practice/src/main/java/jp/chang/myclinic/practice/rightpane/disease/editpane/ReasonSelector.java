package jp.chang.myclinic.practice.rightpane.disease.editpane;

import jp.chang.myclinic.consts.DiseaseEndReason;

import javax.swing.*;

class ReasonSelector extends JComboBox<DiseaseEndReason> {

    ReasonSelector(){
        setEditable(false);
        setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel(value.getKanjiRep());
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
        for(DiseaseEndReason r: DiseaseEndReason.values()){
            addItem(r);
        }
    }
}
