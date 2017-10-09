package jp.chang.myclinic.practice.rightpane.disease.browsepane;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class CommandPart extends JPanel {
    interface Callback {
        default void onEdit(){}
    }

    private Callback callback = new Callback(){};

    CommandPart(){
        setLayout(new MigLayout("insets 2", "", ""));
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JButton editButton = new JButton("編集");
        editButton.addActionListener(evt -> callback.onEdit());
        add(editButton);
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }
}
