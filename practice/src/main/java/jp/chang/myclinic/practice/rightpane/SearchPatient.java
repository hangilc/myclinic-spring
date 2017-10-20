package jp.chang.myclinic.practice.rightpane;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.practice.MainContext;
import jp.chang.myclinic.practice.Service;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// TODO: search by patientId
// TODO: open patient where serach result is single
// TODO: close searc result when selected
class SearchPatient extends JPanel {

   private JTextField searchTextField = new JTextField();
    private JList<PatientDTO> searchResult = new JList<PatientDTO>(){
        @Override
        public Dimension getPreferredScrollableViewportSize() {
            Dimension dim = super.getPreferredScrollableViewportSize();
            return new Dimension(10,dim.height);
        }
    };
    private JScrollPane scrollPane;

    SearchPatient(){
        setLayout(new MigLayout("insets 0, fill", "[grow] []", ""));
        setupSearchTextField();
        setupSearchResult();
        JButton btn = new JButton("検索");
        btn.addActionListener(event -> doSearch());
        add(searchTextField, "growx");
        add(btn, "");
        scrollPane = new JScrollPane(searchResult);
        scrollPane.setVisible(false);
        add(scrollPane, "newline, span, grow, h 200, hidemode 2");
    }

    void reset(){
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
        Component self = this;
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
                        MainContext.get(self).startBrowse(patient, () -> {

                        });
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
                    .thenAccept(patients -> EventQueue.invokeLater(() -> {
                        searchResult.setListData(patients.toArray(new PatientDTO[]{}));
                        scrollPane.setVisible(true);
                        repaint();
                        revalidate();
                    }))
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

}
