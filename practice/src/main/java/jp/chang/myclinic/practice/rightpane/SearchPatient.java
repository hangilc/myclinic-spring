package jp.chang.myclinic.practice.rightpane;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class SearchPatient extends JPanel {

    public SearchPatient(){
        setLayout(new MigLayout("insets 0, fill, debug", "", ""));
        JTextField tf = new JTextField();
        JButton btn = new JButton("検索");
        add(tf, "grow");
        add(btn);
    }
}
