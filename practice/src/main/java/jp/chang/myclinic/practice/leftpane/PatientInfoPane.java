package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.PatientDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class PatientInfoPane extends JPanel {

    private Container container;
    private PatientDTO patient;
    private JEditorPane infoPane;
    private boolean detailShown = false;

    public PatientInfoPane(Container container, PatientDTO patient){
        this.container = container;
        this.patient = patient;
        setLayout(new MigLayout("insets 0, fill", "", ""));
        infoPane = new JEditorPane();
        infoPane.setContentType("text/plain");
        infoPane.setBackground(container.getBackground());
        infoPane.setText(makeText());
        infoPane.setEditable(false);
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
