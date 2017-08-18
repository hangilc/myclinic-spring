package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.dto.DrugFullDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class DrugListDisp extends JPanel {

    private Map<Integer, DrugDisp> drugDispMap = new LinkedHashMap<>();

    DrugListDisp(List<DrugFullDTO> drugs){
        setLayout(new MigLayout("insets 0, gapy 0", "[grow]", ""));
        drugs.forEach(this::addDrug);
    }

    void addDrug(DrugFullDTO drug){
        int index = drugDispMap.size() + 1;
        DrugDisp drugDisp = new DrugDisp(drug, index);
        add(drugDisp, "growx, wrap");
        drugDispMap.put(drug.drug.drugId, drugDisp);
    }

    void addDrugs(List<DrugFullDTO> drugs){
        drugs.forEach(this::addDrug);
    }

    void update(List<DrugFullDTO> modifiedDrugs) {
        modifiedDrugs.forEach(modifiedDrug -> {
            drugDispMap.get(modifiedDrug.drug.drugId).update(modifiedDrug);
        });
    }

    void deleteDrugs(List<Integer> drugIds){
        drugIds.forEach(drugId -> {
            DrugDisp drugDisp = drugDispMap.get(drugId);
            remove(drugDisp);
            drugDispMap.remove(drugId);
        });
        int index = 1;
        for(DrugDisp drugDisp: drugDispMap.values()){
            drugDisp.updateIndex(index);
            index += 1;
        }
        repaint();
        revalidate();
    }
}
