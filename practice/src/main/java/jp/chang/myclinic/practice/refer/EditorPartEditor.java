package jp.chang.myclinic.practice.refer;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class EditorPartEditor extends JPanel {

    private JEditorPane editorPane = new JEditorPane("text/plain", "");

    EditorPartEditor(){
        setLayout(new MigLayout("insets 0", "[grow]", "[][grow, fill]"));
        add(new JLabel("内容"), "left, wrap");
        add(new JScrollPane(editorPane), "w 300px, grow,");
    }

}
