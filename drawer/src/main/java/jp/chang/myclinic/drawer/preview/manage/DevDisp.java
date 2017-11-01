package jp.chang.myclinic.drawer.preview.manage;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class DevDisp extends JPanel {

    private JLabel devnamesLabel = new JLabel();

    DevDisp(){
        setLayout(new MigLayout("", "", ""));
        add(devnamesLabel, "wrap");
    }
}
