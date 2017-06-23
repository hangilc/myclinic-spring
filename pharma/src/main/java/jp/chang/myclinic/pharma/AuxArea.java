package jp.chang.myclinic.pharma;

import javax.swing.*;

public class AuxArea extends JPanel {

    public AuxArea(){

    }

    void setContent(JComponent component){
        removeAll();
        add(component);
        repaint();
        revalidate();
    }
}
