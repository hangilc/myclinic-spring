package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.leftpane.WorkArea;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class DrugDispWrapper extends JPanel {

    private DrugDisp drugDisp;
    private WorkArea workArea;
    private DrugEditPane drugEditPane;

    DrugDispWrapper(DrugFullDTO drugFull, int index, VisitDTO visit){
        super(new MigLayout("insets 0", "[grow]", ""));
        drugDisp = new DrugDisp(drugFull, index);
        drugDisp.setCallback(new DrugDisp.Callback(){
            @Override
            public void onClick() {
                doDrugDispClick(drugFull, visit);
            }
        });
        add(drugDisp, "growx");
    }

    void update(DrugFullDTO drug){
        drugDisp.update(drug);
    }

    void updateIndex(int index){
        drugDisp.updateIndex(index);
    }

    private void doDrugDispClick(DrugFullDTO drugFull, VisitDTO visit){
        drugEditPane = new DrugEditPane(drugFull, visit);
        drugEditPane.setCallback(new DrugEditPane.Callback(){
            @Override
            public void onUpdated(DrugFullDTO newDrugFull) {
                remove(workArea);
                repaint();
                revalidate();
                EventQueue.invokeLater(() -> {
                    drugDisp.update(newDrugFull);
                    add(drugDisp, "growx");
                    workArea = null;
                    drugEditPane = null;
                });
            }

            @Override
            public void onClose() {
                remove(workArea);
                add(drugDisp);
                repaint();
                revalidate();
                workArea = null;
                drugEditPane = null;
            }
        });
        workArea = new WorkArea("薬剤の編集", drugEditPane);
        remove(drugDisp);
        add(workArea, "growx");
        repaint();
        revalidate();
    }
}
