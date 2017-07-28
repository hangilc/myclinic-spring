package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.PatientDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class PatientInfoPane extends JPanel {

    private PatientDTO patient;

    public PatientInfoPane(){
        this.patient = patient;
        setLayout(new MigLayout("insets 0", "", ""));
    }
}
