package jp.chang.myclinic.practice.newvisitdialog;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class PatientSearch extends JPanel {

    PatientSearch(){
        setLayout(new MigLayout("insets 0", "[grow]", "[] [grow]"));
        JTextField searchTextField = new JTextField(10);
        JButton searchButton = new JButton("検索");
        SearchResult searchResult = new SearchResult();
        add(searchTextField, "split 2, grow");
        add(searchButton, "wrap");
        add(new JScrollPane(searchResult), "grow");
    }
}
