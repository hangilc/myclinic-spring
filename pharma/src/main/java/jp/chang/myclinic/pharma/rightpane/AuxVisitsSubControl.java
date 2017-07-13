package jp.chang.myclinic.pharma.rightpane;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.pharma.RecordPage;
import jp.chang.myclinic.pharma.wrappedtext.WrappedText;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.List;

public class AuxVisitsSubControl extends JPanel {

    private AuxRecordsNav nav;

    public AuxVisitsSubControl(PatientDTO patient, List<RecordPage> pages, AuxDispRecords dispRecords){
        setLayout(new MigLayout("fill, insets 0", "", ""));
        add(new WrappedText("(" + patient.lastName + patient.firstName + ")", 300), "wrap");
        nav = new AuxRecordsNav(dispRecords, pages, patient);
        add(nav, "");
        nav.updateVisits();
    }

    public void updateVisitsArea(){
        nav.updateVisits();
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }
}
