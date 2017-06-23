package jp.chang.myclinic.pharma;

import jp.chang.myclinic.dto.PatientDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

/**
 * Created by hangil on 2017/06/14.
 */
public class AuxControl extends JPanel {

    private int patientId;
    private AuxArea auxArea;
    private JRadioButton showRecordsButton = new JRadioButton("日にち順");
    private JRadioButton showDrugsButton = new JRadioButton("薬剤別");

    public AuxControl(AuxArea auxArea){
        this.auxArea = auxArea;
        setLayout(new MigLayout("", "", ""));
        add(makeRow1(), "wrap");
        add(makeRow2());
        bind();
    }

    public void update(PatientDTO patient){
        AuxDispVisits auxDispVisits = new AuxDispVisits(patient.patientId);
        auxArea.setContent(auxDispVisits);
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

    private JComponent makeRow2(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        return panel;
    }

    private void bind(){
        showRecordsButton.addActionListener(event -> doShowRecords());
        showDrugsButton.addActionListener(event -> doShowDrugs());
    }

    private void doShowRecords() {

    }

    private void doShowDrugs() {

    }

}
