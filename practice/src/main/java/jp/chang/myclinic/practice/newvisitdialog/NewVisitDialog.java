package jp.chang.myclinic.practice.newvisitdialog;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.practice.Service;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class NewVisitDialog extends JDialog {

    private int patientId;

    public NewVisitDialog() {
        setTitle("診察受付");
        PatientDisp patientDisp = new PatientDisp();
        PatientSearch patientSearch = new PatientSearch();
        JComponent commandBox = makeCommandBox();
        patientSearch.setCallback(new PatientSearch.Callback(){
            @Override
            public void onSelect(PatientDTO patient) {
                patientDisp.setPatient(patient);
                patientId = patient.patientId;
            }
        });
        setLayout(new MigLayout("fill", "[grow]", "[] [] [grow]"));
        setPreferredSize(new Dimension(300, 500));
        add(patientDisp, "grow, wrap");
        add(commandBox, "grow, wrap");
        add(patientSearch, "grow");
        pack();
    }

    private JComponent makeCommandBox(){
        JButton enterButton = new JButton("診察受付");
        enterButton.addActionListener(event -> {
            if( patientId <= 0 ){
                return;
            }
            Service.api.startVisit(patientId)
                    .thenAccept(visitId -> EventQueue.invokeLater(() -> {
                        dispose();
                    }))
                    .exceptionally(t -> {
                        t.printStackTrace();
                        EventQueue.invokeLater(() -> {
                            alert(t.toString());
                        });
                        return null;
                    });
        });
        JPanel panel = new JPanel(new MigLayout("", "", ""));
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panel.add(enterButton);
        return panel;
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
