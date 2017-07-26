package jp.chang.myclinic.practice.rightpane;

import jp.chang.myclinic.practice.Service;
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
        Service.api.searchPatient(text)
                .thenAccept(patients -> {
                    System.out.println(patients);
                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    alert(t.toString());
                    return null;
                });
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }
}
