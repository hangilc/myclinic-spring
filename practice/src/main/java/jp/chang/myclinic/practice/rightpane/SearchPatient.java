package jp.chang.myclinic.practice.rightpane;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class SearchPatient extends JPanel {

    private JTextField tf = new JTextField();

    public SearchPatient(){
        setLayout(new MigLayout("insets 0, fill", "[grow] []", ""));
        JButton btn = new JButton("検索");
        btn.addActionListener(event -> doSearch());
        add(tf, "grow");
        add(btn);
    }

    private void doSearch(){
        String text = tf.getText();
        if( text.isEmpty() ){
            return;
        }

    }
}
