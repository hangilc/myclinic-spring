package jp.chang.myclinic.practice.leftpane.drug;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class DrugSearch extends JPanel {

    DrugSearch(){
        JTextField searchTextField = new JTextField();
        JButton searchButton = new JButton("検索");
        setLayout(new MigLayout("insets 0", "[grow] []", ""));
        add(searchTextField, "growx");
        add(searchButton, "wrap");
        add(makeModeBox(), "span, wrap");
    }

    private JComponent makeModeBox(){
        JRadioButton masterRadio = new JRadioButton("マスター");
        JRadioButton exampleRadio = new JRadioButton("約束処方");
        JRadioButton prevRadio = new JRadioButton("過去の処方");
        ButtonGroup bg = new ButtonGroup();
        bg.add(masterRadio);
        bg.add(exampleRadio);
        bg.add(prevRadio);
        exampleRadio.setSelected(true);
        JPanel panel = new JPanel(new MigLayout("insets 0, gapx 0", "", ""));
        panel.add(masterRadio);
        panel.add(exampleRadio);
        panel.add(prevRadio);
        return panel;
    }
}
