package jp.chang.myclinic.practice.leftpane;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class DispRecords extends JPanel {

    public DispRecords(){
        setLayout(new MigLayout("insets 0 0 0 24", "[sizegroup c, grow] [sizegroup c, grow]", ""));
        add(new JLabel("平成２９年７月２９日"), "span, wrap");
        add(new JLabel("left"), "");
        add(new JLabel("right right right right"), "wrap");
    }
}
