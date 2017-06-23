package jp.chang.myclinic.pharma;

import javax.swing.*;

public class AuxDispDrugs extends JPanel {
    private int patientId;

    public AuxDispDrugs(int patientId){
        this.patientId = patientId;
        add(new JLabel("Drugs"));
    }
}
