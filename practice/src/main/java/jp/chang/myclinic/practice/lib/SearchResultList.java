package jp.chang.myclinic.practice.lib;

import jp.chang.myclinic.dto.ShinryouMasterDTO;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class SearchResultList<T> extends JList<T> {

    public SearchResultList(){
        setCellRenderer((list, data, index, isSelected, cellHasFocus) -> {
            String labelText = dataToRep(data);
            JLabel label = new JLabel(labelText);
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

    protected String dataToRep(T data){
        return data.toString();
    }

    public void setDoubleClickHandler(Consumer<T> handler){
        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    T data = getSelectedValue();
                    if (data != null) {
                        handler.accept(data);
                    }
                }
            }
        });
    }

    public void setSelectionHandler(Consumer<T> handler){
        addListSelectionListener(event -> {
            if( !event.getValueIsAdjusting() ){
                T item = getSelectedValue();
                if( item != null ){
                    handler.accept(item);
                }
            }
        });
    }

}
