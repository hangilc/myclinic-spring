package jp.chang.myclinic.practice.rightpane.disease.endpane;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class CommandPart extends JPanel {
    interface Callback {
        default void onEnter(){}
    }

    private Callback callback = new Callback(){};

    CommandPart(){
        setLayout(new MigLayout("insets 0", "", ""));
        JButton enterButton = new JButton("入力");
        enterButton.addActionListener(evt -> callback.onEnter());
        add(enterButton);
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }
}
