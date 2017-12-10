package jp.chang.myclinic.practice.refer;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class EditorPartCommand extends JPanel {

    EditorPartCommand(){
        setLayout(new MigLayout("insets 0", "", ""));
        JButton enterButton = new JButton("入力");
        add(enterButton);
    }

}
