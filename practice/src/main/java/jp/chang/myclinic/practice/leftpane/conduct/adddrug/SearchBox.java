package jp.chang.myclinic.practice.leftpane.conduct.adddrug;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class SearchBox extends JPanel {

    interface Callback {
        default void onSearch(String searchText){}
    }

    private Callback callback = new Callback(){};

    SearchBox(){
        setLayout(new MigLayout("insets 0", "[grow] []", ""));
        JTextField textField = new JTextField();
        JButton searchButton = new JButton("検索");
        searchButton.addActionListener(event -> callback.onSearch(textField.getText().trim()));
        add(textField, "growx");
        add(searchButton);
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

}
