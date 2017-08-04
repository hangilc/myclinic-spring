package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.practice.Link;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class NewDrugInfoPane extends JPanel {

    private DrugInfoParts parts = new DrugInfoParts(DrugCategory.Naifuku);
    private Link exampleUsageLink = new Link("例");

    NewDrugInfoPane(){
        setLayout(new MigLayout("insets 0", "[] [grow]", ""));
        add(new JLabel("名称"));
        add(parts.getNameLabel(), "wrap");
        add(parts.getAmountLabel());
        add(parts.getAmountField(), "split 2");
        add(parts.getAmountUnit(), "wrap");
        add(new JLabel("用法"));
        add(parts.getUsageField(), "split 2, growx");
        add(exampleUsageLink, "wrap");
        add(parts.getDaysLabel());
        add(parts.getDaysField(), "split 2");
        add(parts.getDaysUnit());
    }
}
