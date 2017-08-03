package jp.chang.myclinic.practice.leftpane.drug;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class DrugNew extends JPanel {

    DrugNew(){
        setLayout(new MigLayout("", "[grow]", ""));
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        NewDrugInfoPane drugInfoPane = new NewDrugInfoPane();
        add(new JLabel("新規処方の入力"), "growx, wrap");
        add(drugInfoPane, "growx, wrap");

    }
}
