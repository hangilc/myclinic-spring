package jp.chang.myclinic.pharma;

import jp.chang.myclinic.dto.PatientDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.List;

public class AuxVisitsSubControl extends JPanel {

    public AuxVisitsSubControl(PatientDTO patient, List<RecordPage> pages, AuxDispRecords dispRecords){
        setLayout(new MigLayout("fill, insets 0", "", ""));
        AuxRecordsNav nav = new AuxRecordsNav(dispRecords, pages, patient);
        add(nav, "growx");
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }
}
