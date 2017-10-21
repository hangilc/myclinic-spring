package jp.chang.myclinic.practice.rightpane.searchpatient;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.practice.Service;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class Workplace extends JPanel {

    interface Callback {
        default void onSelect(PatientDTO patient){}
    }

    private SearchCommand searchCommand = new SearchCommand();
    private SearchResult searchResult = new SearchResult();
    private JScrollPane scrollPane;
    private Callback callback = new Callback(){};

    Workplace(){
        setLayout(new MigLayout("insets 0, hidemode 3", "[grow]", ""));
        searchCommand.setCallback(new SearchCommand.Callback() {
            @Override
            public void onSearch(String text) {
                doSearch(text);
            }
        });
        searchResult.setVisibleRowCount(20);
        searchResult.setDoubleClickHandler(patient -> {
            callback.onSelect(patient);
        });
        scrollPane = new JScrollPane(searchResult);
        scrollPane.setVisible(false);
        add(searchCommand, "growx");
        add(scrollPane, "w 10, growx, newline");
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    void clear(){
        searchCommand.clear();
        searchResult.setListData(new PatientDTO[]{});
        scrollPane.setVisible(false);
    }

    private void doSearch(String text){
        if( text.isEmpty() ){
            return;
        }
        Service.api.searchPatient(text)
                .thenAccept(patients -> EventQueue.invokeLater(() -> {
                    if( patients.size() == 1 ){
                        callback.onSelect(patients.get(0));
                        return;
                    }
                    searchResult.setListData(patients.toArray(new PatientDTO[]{}));
                    if( !scrollPane.isVisible() ){
                        scrollPane.setVisible(true);
                        revalidate();
                        repaint();
                    }
                }))
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> alert(t.toString()));
                    return null;
                });
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
