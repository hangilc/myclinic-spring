package jp.chang.myclinic.pharma;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

/**
 * Created by hangil on 2017/06/14.
 */
public class AuxControll extends JPanel {

    private JRadioButton showRecordsButton = new JRadioButton("日にち順");
    private JRadioButton showDrugsButton = new JRadioButton("薬剤別");

    public AuxControll(){
        setLayout(new MigLayout("", "", ""));
        add(makeRow1(), "wrap");
        add(makeRow2());
    }

    private JComponent makeRow1(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(showRecordsButton);
        buttonGroup.add(showDrugsButton);
        showRecordsButton.setSelected(true);
        panel.add(showRecordsButton);
        panel.add(showDrugsButton);
        return panel;
    }

    private JComponent makeRow2(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        return panel;
    }

}
