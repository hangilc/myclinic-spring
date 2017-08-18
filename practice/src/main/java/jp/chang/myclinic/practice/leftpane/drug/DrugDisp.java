package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.util.DrugUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class DrugDisp extends JPanel {

    private int drugId;
    private int index;
    private String drugRep;

    DrugDisp(DrugFullDTO drug, int index){
        this.drugId = drug.drug.drugId;
        this.index = index;
        this.drugRep = DrugUtil.drugRep(drug);
        setLayout(new MigLayout("insets 0", "[grow]", ""));
        add(makeEditorPane(), "growx");
    }

    private JEditorPane makeEditorPane(){
        String label = String.format("%d)%s", index, drugRep);
        JEditorPane dispPane = new JEditorPane("text/plain", label);
        dispPane.setEditable(false);
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
        this.drugRep = DrugUtil.drugRep(drug);
        removeAll();
        add(makeEditorPane(), "growx");
        repaint();
        revalidate();
    }

    void updateIndex(int index){
        this.index = index;
        removeAll();
        add(makeEditorPane(), "growx");
        repaint();
        revalidate();
    }
}
