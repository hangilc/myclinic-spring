package jp.chang.myclinic.pharma.rightpane;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.pharma.wrappedtext.Strut;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.List;

class AuxVisitsSubControl extends JPanel {

    interface Callbacks {

    }

    //private AuxRecordsNav nav;
    private Callbacks callbacks;

    AuxVisitsSubControl(PatientDTO patient, List<RecordPage> pages, Callbacks callbacks){
        this.callbacks = callbacks;
        setLayout(new MigLayout("fill, insets 0", "", ""));
        add(new Strut(width -> {
            System.out.println(width);
            //add(new WrappedText("(" + patient.lastName + patient.firstName + ")", 300), "wrap");
        }));
        //nav = new AuxRecordsNav(patient, pages);
        //add(nav, "");
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }
}
