package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.FixedWidthLayout;
import jp.chang.myclinic.practice.WrappedText;
import jp.chang.myclinic.util.DrugUtil;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class DrugListDisp extends JPanel {

    private int width;
    private Map<Integer, DrugDispWrapper> drugDispMap = new LinkedHashMap<>();
    private VisitDTO visit;
    private DrugExecContext drugExecContext;

    DrugListDisp(List<DrugFullDTO> drugs, VisitDTO visit){
//        this.visit = visit;
//        drugExecContext = makeDrugExecContext();
//        setLayout(new MigLayout("insets 0, gapy 0", "[grow]", ""));
//        drugs.forEach(this::addDrug);
    }

    DrugListDisp(List<DrugFullDTO> drugs, VisitDTO visit, int width){
        this.visit = visit;
        this.width = width;
        drugExecContext = makeDrugExecContext();
        setLayout(new FixedWidthLayout(width));
        //setLayout(new MigLayout("insets 0, gapy 0", String.format("[%dpx!]", width), ""));
        drugs.forEach(this::addDrug);
    }

    void addDrug(DrugFullDTO drug){
        int index = drugDispMap.size() + 1;
        //DrugDispWrapper drugDisp = new DrugDispWrapper(drug, index, visit, width, drugExecContext);
        WrappedText drugDisp = new WrappedText(width, index + ")" + DrugUtil.drugRep(drug));
        add(drugDisp, "wrap");
        //drugDispMap.put(drug.drug.drugId, drugDisp);
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
            DrugDispWrapper drugDisp = drugDispMap.get(drugId);
            remove(drugDisp);
            drugDispMap.remove(drugId);
        });
        int index = 1;
        for(DrugDispWrapper drugDisp: drugDispMap.values()){
            drugDisp.updateIndex(index);
            index += 1;
        }
        repaint();
        revalidate();
    }

    private DrugExecContext makeDrugExecContext(){
        return new DrugExecContext(){
            @Override
            public void onDrugDeleted(List<Integer> drugIds) {
                drugIds.forEach(drugId -> {
                    DrugDispWrapper drugDisp = drugDispMap.get(drugId);
                    remove(drugDisp);
                    drugDispMap.remove(drugId);
                });
                EventQueue.invokeLater(() -> {
                    int index = 1;
                    for(DrugDispWrapper drugDisp: drugDispMap.values()){
                        drugDisp.updateIndex(index);
                        index += 1;
                    }
                    repaint();
                    revalidate();
                });
            }
        };
    }

}
