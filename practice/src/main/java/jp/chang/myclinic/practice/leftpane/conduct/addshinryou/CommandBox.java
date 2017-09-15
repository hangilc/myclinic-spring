package jp.chang.myclinic.practice.leftpane.conduct.addshinryou;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class CommandBox extends JPanel {

    interface Callback {
        default void onEnter(){}
        default void onCancel(){}
    }

    private Callback callback = new Callback(){};

    CommandBox(){
        setLayout(new MigLayout("insets 2", "", ""));
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JButton enterButton = new JButton("入力");
        enterButton.addActionListener(event -> callback.onEnter());
        JButton cancelButton = new JButton("キャンセル");
        cancelButton.addActionListener(event -> callback.onCancel());
        add(enterButton);
        add(cancelButton);
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }
}
