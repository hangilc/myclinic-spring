package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.dto.DrugFullDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.List;

class DrugListDisp extends JPanel {

    private int index = 1;

    DrugListDisp(List<DrugFullDTO> drugs){
        setLayout(new MigLayout("insets 0, gapy 0", "[grow]", ""));
        drugs.forEach(this::addDrug);
    }

    void addDrug(DrugFullDTO drug){
        DrugDisp drugDisp = new DrugDisp(drug, index++);
        add(drugDisp, "growx, wrap");
    }

    void addDrugs(List<DrugFullDTO> drugs){
        drugs.forEach(this::addDrug);
    }
}
