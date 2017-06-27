package jp.chang.myclinic.pharma;

import jp.chang.myclinic.dto.PatientDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.List;

public class AuxControl extends JPanel {

    private int width;
    private PatientDTO patient;
    private AuxDispVisits dispVisits;
    private AuxDispDrugs dispDrugs;
    private AuxArea auxArea;
    private JRadioButton showRecordsButton = new JRadioButton("日にち順");
    private JRadioButton showDrugsButton = new JRadioButton("薬剤別");

    public AuxControl(AuxArea auxArea, int width){
        this.auxArea = auxArea;
        //this.width = width;
        setLayout(new MigLayout("insets 0", "[" + width + "!]", ""));
        add(new JLabel("AuxControl"));
        //add(makeRow1());
        //add(makeRow2());
        bind();
    }

    public void update(PatientDTO patient){
        this.patient = patient;
        dispVisits = null;
        dispDrugs = null;
        showRecordsButton.setEnabled(false);
        Service.api.listVisitIdVisitedAtForPatient(patient.patientId)
                .thenAccept(visitIds -> {
                    List<RecordPage>  pages = RecordPage.divideToPages(visitIds);
                    dispVisits = new AuxDispVisits(patient, pages,width);
                    auxArea.setContent(dispVisits);
                    showRecordsButton.setEnabled(true);
                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    alert(t.toString());
                    return null;
                });
    }

    private JComponent makeRow1(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(showRecordsButton);
        buttonGroup.add(showDrugsButton);
        showRecordsButton.setSelected(true);
        panel.add(showRecordsButton);
        panel.add(showDrugsButton);
        return panel;
    }

//    private JComponent makeRow2(){
//        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
//        return panel;
//    }

    private void bind(){
        showRecordsButton.addActionListener(event -> doShowRecords());
        showDrugsButton.addActionListener(event -> doShowDrugs());
    }

    private void doShowRecords() {

    }

    private void doShowDrugs() {
        if( dispDrugs == null ){
            showDrugsButton.setEnabled(false);
            Service.api.listIyakuhinForPatient(patient.patientId)
                    .thenAccept(result -> {
                        dispDrugs = new AuxDispDrugs(patient, result);

                    })
                    .exceptionally(t -> {
                        t.printStackTrace();
                        alert(t.toString());
                        return null;
                    });
        } else {
            auxArea.setContent(dispDrugs);
        }
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
