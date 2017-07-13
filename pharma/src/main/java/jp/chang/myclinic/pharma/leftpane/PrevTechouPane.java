package jp.chang.myclinic.pharma.leftpane;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class PrevTechouPane extends JPanel {

    private JTextField prevTechouSearchField = new JTextField(6);
    private JButton searchPrevTechouButton = new JButton("検索");

    public PrevTechouPane(){
        setLayout(new MigLayout("", "", ""));
        setBorder(BorderFactory.createTitledBorder("過去のお薬手帳"));
        add(prevTechouSearchField);
        add(searchPrevTechouButton);
    }
}
