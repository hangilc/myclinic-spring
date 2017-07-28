package jp.chang.myclinic.practice.rightpane;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.practice.Service;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class SearchPatient extends JPanel {

    private JTextField tf = new JTextField();
    private JList<PatientDTO> searchResult = new JList<>();

    public SearchPatient(){
        setLayout(new MigLayout("insets 0, fill", "[grow] []", ""));
        setupSearchResult();
        JButton btn = new JButton("検索");
        btn.addActionListener(event -> doSearch());
        add(tf, "grow");
        add(searchResult, "newline, span, grow");
        add(btn);
    }

    private void setupSearchResult(){
        searchResult.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel();
            label.setText(value.lastName + " " + value.firstName);
            return label;
        });
    }

    private void doSearch(){
        String text = tf.getText();
        if( text.isEmpty() ){
            return;
        }
        Service.api.searchPatient(text)
                .thenAccept(patients -> {
                    searchResult.setListData(patients.toArray(new PatientDTO[]{}));
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
