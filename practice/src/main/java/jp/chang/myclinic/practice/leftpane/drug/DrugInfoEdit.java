package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.dto.DrugFullDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class DrugInfoEdit extends DrugInfoBase {

    DrugInfoEdit(DrugFullDTO drugFull){
        setLayout(new MigLayout("insets 0", "[] [grow]", ""));
        add(new JLabel("名称"));
        add(drugNameLabel, "growx, wrap");
        add(amountLabel);
        add(amountField, "split 2");
        add(amountUnit, "wrap");
        add(new JLabel("用法"));
        add(usageField, "split 2, growx");
        add(usageExampleLink, "wrap");
        add(usageExampleWrapper, "span, hidemode 3, wrap");
        add(daysLabel, "hidemode 3");
        add(daysField, "split 2, hidemode 3");
        add(daysUnit, "hidemode 3, wrap");
        add(categoryPane, "span");
        setDrugFull(drugFull);
    }
}
