package jp.chang.myclinic.practice.rightpane.searchpatient;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class SearchCommand extends JPanel {

    interface Callback {
        default void onSearch(String text){}
    }

    private JTextField textField = new JTextField();
    private Callback callback = new Callback(){};

    SearchCommand(){
        setLayout(new MigLayout("insets 0", "[grow] []", ""));
        JButton button = new JButton("検索");
        textField.addActionListener(evt -> button.doClick());
        button.addActionListener(evt -> {
            String text = textField.getText();
            callback.onSearch(text);
        });
        add(textField, "growx");
        add(button);
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    void clear(){
        textField.setText("");
    }
}
