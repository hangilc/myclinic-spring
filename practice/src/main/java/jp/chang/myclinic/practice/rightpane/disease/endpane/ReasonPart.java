package jp.chang.myclinic.practice.rightpane.disease.endpane;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class ReasonPart extends JPanel {



    ReasonPart(){
        setLayout(new MigLayout("insets 0", "", ""));
        JRadioButton cureButton = new JRadioButton("治癒");
        JRadioButton stopButton = new JRadioButton("中止");
        JRadioButton deathButton = new JRadioButton("死亡");
        ButtonGroup group = new ButtonGroup();
        group.add(cureButton);
        group.add(stopButton);
        group.add(deathButton);
        cureButton.setSelected(true);
        add(new JLabel("転帰:"));
        add(cureButton);
        add(stopButton);
        add(deathButton);
    }

    char getReason(){

    }
}
