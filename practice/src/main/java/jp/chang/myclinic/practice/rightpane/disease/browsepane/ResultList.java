package jp.chang.myclinic.practice.rightpane.disease.browsepane;

import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.DiseaseUtil;

import javax.swing.*;
import java.util.function.Consumer;

class ResultList extends JList<DiseaseFullDTO> {

    ResultList(int itemsPerPage){
        setVisibleRowCount(itemsPerPage);
        setCellRenderer((list, data, index, isSelected, cellHasFocus) -> {
            String labelText = dataToRep(data);
            JLabel text = new JLabel(labelText);
            if( isSelected ){
                text.setBackground(list.getSelectionBackground());
                text.setForeground(list.getSelectionForeground());
            } else {
                text.setBackground(list.getBackground());
                text.setForeground(list.getForeground());
            }
            text.setOpaque(true);
            return text;
        });
    }

    void setSelectionHandler(Consumer<DiseaseFullDTO> handler){
        addListSelectionListener(event -> {
            if( !event.getValueIsAdjusting() ){
                DiseaseFullDTO item = getSelectedValue();
                if( item != null ){
                    handler.accept(item);
                }
            }
        });
    }

    private String dataToRep(DiseaseFullDTO data) {
        return DiseaseUtil.getFullName(data) + " (" +
                DateTimeUtil.sqlDateToKanji(data.disease.startDate, DateTimeUtil.kanjiFormatter5) + ")";
    }
}
