package jp.chang.myclinic.practice.rightpane;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.practice.Service;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class SearchPatient extends JPanel {

    private JTextField tf = new JTextField();
    private JList<PatientDTO> searchResult = new JList<>();
    private JScrollPane scrollPane;

    public SearchPatient(){
        setLayout(new MigLayout("insets 0, fill", "[grow] []", ""));
        setupSearchResult();
        JButton btn = new JButton("検索");
        btn.addActionListener(event -> doSearch());
        add(tf, "grow");
        add(btn, "");
        scrollPane = new JScrollPane(searchResult);
        scrollPane.setVisible(false);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, "newline, span, grow, h 200, hidemode 2");
    }

    private void setupSearchResult(){
        searchResult.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel();
            label.setText(value.lastName + " " + value.firstName);
            return label;
        });
    }

    private void doSearch(){
        if( scrollPane.isVisible() ){
            searchResult.setListData(new PatientDTO[]{});
            scrollPane.setVisible(false);
            repaint();
            revalidate();
        } else {
            String text = tf.getText();
            if( text.isEmpty() ){
                return;
            }
            Service.api.searchPatient(text)
                    .thenAccept(patients -> {
                        searchResult.setListData(patients.toArray(new PatientDTO[]{}));
                        scrollPane.setVisible(true);
                        repaint();
                        revalidate();
                    })
                    .exceptionally(t -> {
                        t.printStackTrace();
                        EventQueue.invokeLater(() -> alert(t.toString()));
                        return null;
                    });
        }
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }
}
