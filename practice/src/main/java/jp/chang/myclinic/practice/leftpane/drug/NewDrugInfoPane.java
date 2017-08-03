package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.practice.Link;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class NewDrugInfoPane extends JPanel {

    private JLabel drugNameLabel = new JLabel();
    private JTextField amountField = new JTextField(6);
    private JLabel unitLabel = new JLabel();
    private JTextField usageField = new JTextField();
    private Link exampleUsageLink = new Link("例");

    NewDrugInfoPane(){
        setLayout(new MigLayout("insets 0", "[] [grow]", ""));
        add(new JLabel("名称"));
        add(drugNameLabel, "wrap");
        add(new JLabel("用量"));
        add(amountField, "split 2");
        add(unitLabel, "wrap");
        add(new JLabel("用法"));
        add(usageField, "split 2, growx");
        add(exampleUsageLink, "wrap");
        add(new JLabel("日数"));


    }
}
