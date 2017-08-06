package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.consts.DrugCategory;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

// TODO: implement COMMENT
class DrugInfoNew extends DrugInfoBase {

    private JCheckBox daysFixedCheckBox = new JCheckBox("日数を固定");

    DrugInfoNew(){
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
        add(daysField, "split 3, hidemode 3");
        add(daysUnit, "hidemode 3");
        add(daysFixedCheckBox, "hidemode 3, wrap");
        add(categoryPane, "span");
        setCategory(DrugCategory.Naifuku);
        daysFixedCheckBox.setSelected(true);
    }

    @Override
    void setDaysVisible(boolean visible){
        super.setDaysVisible(visible);
        daysFixedCheckBox.setVisible(visible);
    }
}
