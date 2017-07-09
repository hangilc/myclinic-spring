package jp.chang.myclinic.pharma;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class NewDrugInfoDialog extends JDialog {

    NewDrugInfoDialog(){
        setTitle("新規薬剤情報入力");
        setLayout(new MigLayout("", "", ""));
        add(new JLabel("薬剤検索"), "span, wrap");
        JTextField searchTextField = new JTextField(12);
        add(searchTextField);
        JButton searchButton = new JButton("検索");
        add(searchButton);
        searchButton.addActionListener(event -> {
            String searchText = searchTextField.getText();
            if( searchText.isEmpty() ){
                return;
            }

        });
        pack();
    }

}
