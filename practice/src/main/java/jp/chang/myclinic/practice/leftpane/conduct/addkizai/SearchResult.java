package jp.chang.myclinic.practice.leftpane.conduct.addkizai;

import jp.chang.myclinic.dto.KizaiMasterDTO;

import javax.swing.*;

class SearchResult extends JList<KizaiMasterDTO> {

    interface Callback {
        default void onSelected(KizaiMasterDTO master){}
    }

    private Callback callback = new Callback(){};

    SearchResult(){
        setCellRenderer((list, result, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel();
            label.setText(result.name);
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
        addListSelectionListener(event -> {
            if( !event.getValueIsAdjusting() ){
                KizaiMasterDTO master = getSelectedValue();
                if( master != null ){
                    callback.onSelected(master);
                }
            }
        });
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

}
