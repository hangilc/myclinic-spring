package jp.chang.myclinic.practice.leftpane.drug;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class NewDrugInfoPane extends JPanel {

    private DrugInfoParts parts = new DrugInfoParts();
    private JCheckBox daysFixedCheckBox = new JCheckBox("日数を固定");

    NewDrugInfoPane(){
        setLayout(new MigLayout("insets 0", "[] [grow]", ""));
        add(new JLabel("名称"));
        add(parts.getNameLabel(), "wrap");
        add(parts.getAmountLabel());
        add(parts.getAmountField(), "split 2");
        add(parts.getAmountUnit(), "wrap");
        add(new JLabel("用法"));
        add(parts.getUsageField(), "split 2, growx");
        add(parts.getUsageExampleLink(), "wrap");
        add(parts.getUsageExampleWrapper(), "span, hidemode 3, wrap");
        add(parts.getDaysLabel(), "hidemode 3");
        add(parts.getDaysField(), "split 3, hidemode 3");
        add(parts.getDaysUnit(), "hidemode 3");
        add(daysFixedCheckBox, "hidemode 3, wrap");
        add(parts.getCatgegoryPane(), "span");
        parts.setCallback(new DrugInfoParts.Callback() {
            @Override
            public void onSetDaysVisible(boolean visible) {
                daysFixedCheckBox.setVisible(visible);
            }
        });
        daysFixedCheckBox.setSelected(true);
    }

}
