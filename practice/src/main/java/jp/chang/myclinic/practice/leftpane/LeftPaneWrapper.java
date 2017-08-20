package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.practice.MainExecContext;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class LeftPaneWrapper extends JPanel {

    private MainExecContext mainExecContext;
    private PatientInfoPane patientInfoPane;

    public LeftPaneWrapper(MainExecContext mainExecContext){
        setLayout(new MigLayout("insets 0", "[grow]", ""));
        this.mainExecContext = mainExecContext;
    }

    private void setupComponents(){
        int width = getSize().width;
        System.out.println("width: " + width);
        this.patientInfoPane = new PatientInfoPane(width);
        add(this.patientInfoPane, "wrap");
        revalidate();
    }

    public void reset(){
        setVisible(false);
        repaint();
        revalidate();
    }

    public void start(){
        if( patientInfoPane == null ){
            setupComponents();
        }
        PatientDTO patient = mainExecContext.getCurrentPatient();
        patientInfoPane.setPatient(patient);
        repaint();
        revalidate();
    }
}
