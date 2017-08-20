package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.leftpane.WorkArea1;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class DrugDispWrapper extends JPanel {

    private DrugDisp drugDisp;
    private WorkArea1 workArea;
    private DrugEditPane drugEditPane;

    DrugDispWrapper(DrugFullDTO drugFull, int index, VisitDTO visit, int width, DrugExecContext drugExecContext){
        super(new MigLayout("insets 0", String.format("[%dpx!]", width), ""));
        drugDisp = new DrugDisp(drugFull, index, width);
        drugDisp.setCallback(new DrugDisp.Callback(){
            @Override
            public void onClick() {
                doDrugDispClick(drugFull, visit, drugExecContext);
            }
        });
        add(drugDisp, "");
    }

    void update(DrugFullDTO drug){
        drugDisp.update(drug);
    }

    void updateIndex(int index){
        drugDisp.updateIndex(index);
    }

    private void doDrugDispClick(DrugFullDTO drugFull, VisitDTO visit, DrugExecContext drugExecContext){
        drugEditPane = new DrugEditPane(drugFull, visit, drugExecContext);
        drugEditPane.setCallback(new DrugEditPane.Callback(){
            @Override
            public void onUpdated(DrugFullDTO newDrugFull) {
                remove(workArea);
                repaint();
                revalidate();
                EventQueue.invokeLater(() -> {
                    drugDisp.update(newDrugFull);
                    add(drugDisp, "");
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
        workArea = new WorkArea1("薬剤の編集", drugEditPane);
        remove(drugDisp);
        add(workArea, "");
        repaint();
        revalidate();
    }

}
