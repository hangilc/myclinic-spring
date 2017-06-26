package jp.chang.myclinic.pharma;

import jp.chang.myclinic.dto.PatientDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

public class AuxDispVisits extends JPanel {
    private PatientDTO patient;
    private List<RecordPage> pages = Collections.emptyList();
    private AuxDispVisitsNav nav;
    private AuxDispVisitsRecords records;

    public AuxDispVisits(PatientDTO patient, List<RecordPage> pages, int width){
        this.patient = patient;
        this.pages = pages;
        setLayout(new MigLayout("fill, insets 0", "", ""));
        records = new AuxDispVisitsRecords(width);
        nav = new AuxDispVisitsNav(records, pages, patient);
        add(nav, "growx, wrap");
        if( pages.size() > 0 ){
            records.showVisits(pages.get(0).visitIds);
        }
        add(records);
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }
}
