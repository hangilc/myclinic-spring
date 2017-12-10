package jp.chang.myclinic.practice.refer;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class EditorPart extends JPanel {

    private EditorPartEditor editor = new EditorPartEditor();
    private EditorPartCommand command = new EditorPartCommand();

    EditorPart(){
        setLayout(new MigLayout("insets 0, debug", "[grow]", "[grow] []"));
        add(editor, "grow, wrap");
        add(command, "");
    }

}
