package jp.chang.myclinic.practice.rightpane;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.practice.Service;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SearchPatient extends JPanel {

    public interface Callback {
        void onSelect(PatientDTO patient);
    }

    private JTextField searchTextField = new JTextField();
    private JList<PatientDTO> searchResult = new JList<>();
    private JScrollPane scrollPane;
    private Callback callback;

    public SearchPatient(Callback callback){
        this.callback = callback;
        setLayout(new MigLayout("insets 0, fill", "[grow] []", ""));
        setupSearchTextField();
        setupSearchResult();
        JButton btn = new JButton("検索");
        btn.addActionListener(event -> doSearch());
        add(searchTextField, "grow");
        add(btn, "");
        scrollPane = new JScrollPane(searchResult);
        scrollPane.setVisible(false);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, "newline, span, grow, h 200, hidemode 2");
    }

    private void setupSearchTextField(){
        searchTextField.addActionListener(event -> doSearch());
    }

    private void setupSearchResult(){
        searchResult.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel();
            label.setText(value.lastName + " " + value.firstName);
            return label;
        });
        searchResult.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if( e.getClickCount() == 2 ){
                    PatientDTO patient = searchResult.getSelectedValue();
                    if( patient != null ){
                        callback.onSelect(patient);
                    }
                }
            }
        });
    }

    private void doSearch(){
        if( scrollPane.isVisible() ){
            searchResult.setListData(new PatientDTO[]{});
            scrollPane.setVisible(false);
            searchTextField.setText("");
            repaint();
            revalidate();
        } else {
            String text = searchTextField.getText();
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
