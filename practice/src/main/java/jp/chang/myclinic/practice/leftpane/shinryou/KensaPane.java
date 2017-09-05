package jp.chang.myclinic.practice.leftpane.shinryou;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

class KensaPane extends JPanel {

    interface Callback {
        default void onEnter(List<String> names){}
        default void onCancel(){}
    }

    private Callback callback = new Callback(){};

    KensaPane(){
        setLayout(new MigLayout("insets 0, fill", "[grow] [grow]", ""));
        add(new JLabel("A"));
        add(new JLabel("B"), "wrap");
        add(makeCommandBox(), "span");
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    private Component makeCommandBox(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        JButton enterButton = new JButton("入力");
        JButton cancelButton = new JButton("キャンセル");
        cancelButton.addActionListener(event -> callback.onCancel());
        panel.add(enterButton);
        panel.add(cancelButton);
        return panel;
    }
}
