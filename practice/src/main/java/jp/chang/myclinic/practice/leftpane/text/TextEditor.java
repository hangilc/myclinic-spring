package jp.chang.myclinic.practice.leftpane.text;

import jp.chang.myclinic.dto.TextDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class TextEditor extends JPanel {

    public interface Callback {
        void onEnter(TextDTO newText);
        void onCancel();
    }

    private TextDTO textDTO;
    private JEditorPane ep;
    private Callback callback;

    public TextEditor(TextDTO textDTO, Callback callback){
        this.textDTO = textDTO;
        this.callback = callback;
        setLayout(new MigLayout("insets 0", "[grow]", ""));
        ep = new JEditorPane("text/plain", textDTO.content);
        JButton enterButton = new JButton("入力");
        enterButton.addActionListener(event -> doEnter());
        JButton cancelButton = new JButton("キャンセル");
        cancelButton.addActionListener(event -> callback.onCancel());
        add(new JScrollPane(ep), "growx, h 200:n:n, wrap");
        add(enterButton, "split 2");
        add(cancelButton);
    }

    private void doEnter(){
        TextDTO newText = textDTO.copy();
        newText.content = ep.getText();
        callback.onEnter(newText);
    }
}
