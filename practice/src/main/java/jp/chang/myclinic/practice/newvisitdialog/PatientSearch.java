package jp.chang.myclinic.practice.newvisitdialog;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.practice.Service;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class PatientSearch extends JPanel {

    interface Callback {
        default void onSelect(PatientDTO patient){ throw new RuntimeException("not implemented"); }
    }

    private Callback callback = new Callback(){};

    PatientSearch(){
        setLayout(new MigLayout("insets 0", "[grow]", "[] [grow]"));
        JTextField searchTextField = new JTextField(10);
        JButton searchButton = new JButton("検索");
        SearchResult searchResult = new SearchResult();
        searchButton.addActionListener(event -> {
            String text = searchTextField.getText().trim();
            if( text.isEmpty() ){
                return;
            }
            Service.api.searchPatient(text)
                    .thenAccept(list -> {
                        searchResult.setListData(list.toArray(new PatientDTO[]{}));
                    })
                    .exceptionally(t -> {
                        t.printStackTrace();
                        EventQueue.invokeLater(() -> {
                            alert(t.toString());
                        });
                        return null;
                    });

        });
        searchResult.addListSelectionListener(event -> {
            if( event.getValueIsAdjusting() ){
                return;
            }
            PatientDTO data = searchResult.getSelectedValue();
            if( data != null ){
                callback.onSelect(data);
            }
        });
        add(searchTextField, "split 2, grow");
        add(searchButton, "wrap");
        add(new JScrollPane(searchResult), "grow");
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
