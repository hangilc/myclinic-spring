package jp.chang.myclinic.practice.lib.dateinput;

import jp.chang.myclinic.consts.Gengou;

import javax.swing.*;
import java.awt.*;
import java.time.chrono.JapaneseEra;
import java.util.List;

public class GengouInput extends JComboBox<Gengou> {

    public GengouInput(List<Gengou> gengouList){
        setEditable(false);
        setRenderer(new ListCellRenderer<Gengou>(){
            @Override
            public Component getListCellRendererComponent(JList<? extends Gengou> list, Gengou value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = new JLabel(value.getKanji());
                if( isSelected ){
                    label.setBackground(list.getSelectionBackground());
                    label.setForeground(list.getSelectionForeground());
                } else {
                    label.setBackground(list.getBackground());
                    label.setForeground(list.getForeground());
                }
                label.setOpaque(true);
                return label;
            }
        });
        gengouList.forEach(this::addItem);
    }

    public JapaneseEra getEra(){
        System.out.println("selected item: " + ((Gengou)getSelectedItem()).getEra());
        return ((Gengou)getSelectedItem()).getEra();
    }

    public void setEra(JapaneseEra era){
        Gengou g = Gengou.fromEra(era);
        setSelectedItem(g);
    }

}
