package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.util.DrugUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class DrugDisp extends JPanel {

    private DrugFullDTO drug;
    private JEditorPane dispPane;

    DrugDisp(DrugFullDTO drug, int index){
        this.drug = drug;
        setLayout(new MigLayout("insets 0", "[grow]", ""));
        String label = String.format("%d)%s", index, DrugUtil.drugRep(drug));
        dispPane = new JEditorPane("text/plain", label);
        dispPane.setBackground(getBackground());
        add(dispPane, "growx");
    }

}
