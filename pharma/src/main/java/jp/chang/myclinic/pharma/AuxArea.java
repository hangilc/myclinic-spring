package jp.chang.myclinic.pharma;

import javax.swing.*;

/**
 * Created by hangil on 2017/06/14.
 */
public class AuxArea extends JPanel {

    private int patientId;

    public AuxArea(){

    }

    public void setup(int patientId){
        this.patientId = patientId;
    }
}
