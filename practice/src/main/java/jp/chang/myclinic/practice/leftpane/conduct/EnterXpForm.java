package jp.chang.myclinic.practice.leftpane.conduct;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class EnterXpForm extends JPanel {

    interface Callback {
        default void onEnter(){}
        default void onCancel(){}
    }

    private JComboBox<String> labelCombo;
    private JComboBox<String> filmCombo;
    private Callback callback = new Callback(){};

    EnterXpForm(){
        setLayout(new MigLayout("insets 0", "", ""));
        labelCombo = new JComboBox<>(new String[]{"胸部単純Ｘ線", "腹部単純Ｘ線"});
        labelCombo.setSelectedIndex(0);
        filmCombo = new JComboBox<>(new String[]{"半切", "大角", "四ツ切"});
        filmCombo.setSelectedIndex(1);
        JButton enterButton = new JButton("入力");
        enterButton.addActionListener(event -> callback.onEnter());
        JButton cancelButton = new JButton("キャンセル");
        cancelButton.addActionListener(event -> callback.onCancel());
        add(labelCombo, "w 120, wrap");
        add(filmCombo, "w 120, wrap");
        add(enterButton, "split 2");
        add(cancelButton);
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    String getLabel(){
        return (String)labelCombo.getSelectedItem();
    }

    String getFilm(){
        return (String)filmCombo.getSelectedItem();
    }
}
