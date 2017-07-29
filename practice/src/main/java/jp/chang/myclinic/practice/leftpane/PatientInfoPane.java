package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.PatientDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class PatientInfoPane extends JPanel {

    private PatientDTO patient;
    private JEditorPane infoPane;
    private boolean detailShown = false;

    PatientInfoPane(PatientDTO patient){
        this.patient = patient;
        setLayout(new MigLayout("fillx", "", ""));
        infoPane = new JEditorPane();
        infoPane.setContentType("text/plain");
        infoPane.setText(makeText());
        infoPane.setEditable(false);
        infoPane.setBackground(this.getBackground());
        JButton detailButton = new JButton("詳細");
        detailButton.addActionListener(event -> doToggleDetail());
        add(infoPane, "growx");
        add(detailButton, "top");
    }

    private String makeText(){
        return String.format("[%d] %s %s (%s %s)", patient.patientId, patient.lastName, patient.firstName,
                patient.lastNameYomi, patient.firstNameYomi);
    }

    private void doToggleDetail(){
        String text = makeText();
        if( !detailShown ){
            text += "\n住所：" + patient.address + "\n電話：" + patient.phone;
        }
        infoPane.setText(text);
        detailShown = !detailShown;
    }


}
