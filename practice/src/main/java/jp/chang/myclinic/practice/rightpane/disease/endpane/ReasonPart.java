package jp.chang.myclinic.practice.rightpane.disease.endpane;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.ArrayList;

class ReasonPart extends JPanel {

    private static class Item {
        private JRadioButton radio;
        private char code;

        Item(JRadioButton radio, char code){
            this.radio = radio;
            this.code = code;
        }

        boolean isSelected(){
            return radio.isSelected();
        }

        void setSelected(boolean selected){
            radio.setSelected(selected);
        }

        JRadioButton getRadio(){
            return radio;
        }

        char getCode(){
            return code;
        }
    }

    private static class Items extends ArrayList<Item> {
        private ButtonGroup group = new ButtonGroup();

        @Override
        public boolean add(Item item){
            group.add(item.getRadio());
            return super.add(item);
        }

        boolean add(JRadioButton radio, char code){
            return add(new Item(radio, code));
        }

        boolean setSelected(char code){
            for(Item item: this){
                if( item.getCode() == code ){
                    item.getRadio().setSelected(true);
                    return true;
                }
            }
            return false;
        }

        char getSelected(){
            for(Item item: this){
                if( item.isSelected() ){
                    return item.getCode();
                }
            }
            throw new RuntimeException("no item selected (cannot happen)");
        }
    }

    private Items items = new Items();

    ReasonPart(){
        setLayout(new MigLayout("insets 0", "", ""));
        items.add(new Item(new JRadioButton("治癒"), 'C'));
        items.add(new Item(new JRadioButton("中止"), 'S'));
        items.add(new Item(new JRadioButton("死亡"), 'D'));
        items.setSelected('C');
        add(new JLabel("転帰:"));
        items.forEach(item -> add(item.getRadio()));
    }

    char getReason(){
        return items.getSelected();
    }
}
