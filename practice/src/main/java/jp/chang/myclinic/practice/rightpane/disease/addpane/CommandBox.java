package jp.chang.myclinic.practice.rightpane.disease.addpane;

import jp.chang.myclinic.practice.Link;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class CommandBox extends JPanel {

    interface Callback {
        default void onEnter(){}
        default void onAddSusp(){}
        default void onDeleteAdj(){}
    }

    private Callback callback = new Callback(){};

    CommandBox(){
        setLayout(new MigLayout("insets 2", "", ""));
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JButton enterButton = new JButton("入力");
        enterButton.addActionListener(evt -> callback.onEnter());
        Link suspectLink = new Link("の疑い");
        suspectLink.setCallback(evt -> callback.onAddSusp());
        Link delAdjLink = new Link("修飾語削除");
        delAdjLink.setCallback(evt -> callback.onDeleteAdj());
        add(enterButton);
        add(suspectLink);
        add(new JLabel("|"));
        add(delAdjLink);
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }
}
