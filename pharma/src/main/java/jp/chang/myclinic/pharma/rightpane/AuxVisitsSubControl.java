package jp.chang.myclinic.pharma.rightpane;

import jp.chang.myclinic.dto.PatientDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.List;

class AuxVisitsSubControl extends JPanel {

    interface Callbacks {
        void onShowRecords(List<Integer> visitIds);
    }

    private AuxRecordsNav nav;

    AuxVisitsSubControl(PatientDTO patient, List<RecordPage> pages, Callbacks callbacks){
        setLayout(new MigLayout("insets 0", "", ""));
        nav = new AuxRecordsNav(pages, page -> {
            callbacks.onShowRecords(page.getVisitIds());
        });
        add(nav);
        add(new JLabel("(" + patient.lastName + patient.firstName + ")"));
    }

    void trigger(){
        nav.trigger();
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }
}
