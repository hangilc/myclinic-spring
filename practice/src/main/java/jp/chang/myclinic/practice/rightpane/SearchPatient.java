package jp.chang.myclinic.practice.rightpane;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.practice.Service;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
        {
            Strut strut = new Strut();
            strut.addComponentListener(new ComponentAdapter(){
                @Override
                public void componentResized(ComponentEvent e) {
                    int w = (int)strut.getSize().getWidth();
                    searchResult.setFixedCellWidth(w);
                }
            });
            add(strut, "growx, wrap, h 0, gapy 0");
        }
        add(searchTextField, "growx");
        add(btn, "");
        scrollPane = new JScrollPane(searchResult);
        scrollPane.setVisible(false);
        add(scrollPane, "newline, span, grow, h 200, hidemode 2");
    }

    public void reset(){
        searchTextField.setText("");
        scrollPane.setVisible(false);
        searchResult.setListData(new PatientDTO[]{});
        repaint();
        revalidate();
    }

    private void setupSearchTextField(){
        searchTextField.addActionListener(event -> doSearch(true));
    }

    private void setupSearchResult(){
        searchResult.setCellRenderer((list, patient, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel();
            String text = String.format("[%04d] %s %s (%s %s)", patient.patientId, patient.lastName,
                    patient.firstName, patient.lastNameYomi, patient.firstNameYomi);
            label.setText(text);
            if( isSelected ){
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
            } else {
                label.setBackground(list.getBackground());
                label.setForeground(list.getForeground());
            }
            label.setOpaque(true);
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

    private void doSearch(boolean force){
        if( !force && scrollPane.isVisible() ){
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

    private void doSearch(){
        doSearch(false);
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

    private static class Strut extends JComponent {

    }
}
