package jp.chang.myclinic.practice.leftpane.text;

import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.practice.Link;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class TextEditor extends JPanel {

    public interface Callback {
        void onEnter(TextDTO newText);
        void onDelete();
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
        Link deleteLink = new Link("削除", this::doDelete);
        Link prescLink = new Link("処方箋発行", () -> {});
        Link copyLink = new Link("コピー", () -> {});
        add(new JScrollPane(ep), "growx, h 200:n:n, wrap");
        add(enterButton, "split 2");
        add(cancelButton, "wrap");
        add(deleteLink, "split 3");
        add(prescLink);
        add(copyLink);
    }

    private void doEnter(){
        TextDTO newText = textDTO.copy();
        newText.content = ep.getText();
        callback.onEnter(newText);
    }

    private void doDelete(){
        int select = JOptionPane.showConfirmDialog(this, "この文章を削除していいですか？", "確認",
                JOptionPane.YES_NO_OPTION);
        if( select == JOptionPane.YES_OPTION ){
            callback.onDelete();
        }
    }
}
