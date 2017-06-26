package jp.chang.myclinic.pharma;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class AuxArea extends JPanel {

    public AuxArea(int width){
        setLayout(new MigLayout("insets 0", "[" + width + "!]", ""));
    }

    void setContent(JComponent component){
        removeAll();
        add(component);
        repaint();
        revalidate();
    }
}
