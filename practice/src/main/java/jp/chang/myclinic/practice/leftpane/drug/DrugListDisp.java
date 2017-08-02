package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.dto.DrugFullDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.List;

class DrugListDisp extends JPanel {

    DrugListDisp(List<DrugFullDTO> drugs){
        setLayout(new MigLayout("insets 0, gapy 0", "[grow]", ""));
        int index = 1;
        for(DrugFullDTO drug: drugs){
            DrugDisp drugDisp = new DrugDisp(drug, index++);
            add(drugDisp, "growx, wrap");
        }
    }
}
