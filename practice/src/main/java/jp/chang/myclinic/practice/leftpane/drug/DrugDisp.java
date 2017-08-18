package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.util.DrugUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class DrugDisp extends JPanel {

    private int drugId;
    private int index;

    DrugDisp(DrugFullDTO drug, int index){
        this.drugId = drug.drug.drugId;
        this.index = index;
        setLayout(new MigLayout("insets 0", "[grow]", ""));
        add(makeEditorPane(drug, index), "growx");
    }

    private String makeLabel(DrugFullDTO drug, int index){
        return String.format("%d)%s", index, DrugUtil.drugRep(drug));
    }

    private JEditorPane makeEditorPane(DrugFullDTO drug, int index){
        String label = String.format("%d)%s", index, DrugUtil.drugRep(drug));
        JEditorPane dispPane = new JEditorPane("text/plain", label);
        dispPane.setBackground(getBackground());
        return dispPane;
    }

    int getDrugId() {
        return drugId;
    }

    int getIndex(){
        return index;
    }

    void update(DrugFullDTO drug){
        removeAll();
        add(makeEditorPane(drug, index), "growx");
        repaint();
        revalidate();
    }
}
