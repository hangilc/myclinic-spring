package jp.chang.myclinic.practice.leftpane.text;

import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.practice.Link;
import jp.chang.myclinic.practice.Service;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class TextEditor extends JPanel {

    public interface Callback {
        default void onEnter(TextDTO newText){}
        default void onDelete(){}
        default void onCancel(){}
    }

    private TextDTO textDTO;
    private JEditorPane ep;
    private Callback callback = new Callback(){};

    TextEditor(TextDTO textDTO, int width){
        this.textDTO = textDTO;
        setLayout(new MigLayout("insets 0, fill", String.format("[%dpx!]", width), ""));
        ep = new JEditorPane("text/plain", textDTO.content);
        JButton enterButton = new JButton("入力");
        enterButton.addActionListener(event -> doEnter());
        JButton cancelButton = new JButton("キャンセル");
        cancelButton.addActionListener(event -> TextAreaContext.get(this).onEditorCancel(this));
        Link deleteLink = new Link("削除", event -> doDelete());
        Link prescLink = new Link("処方箋発行", event -> {});
        Link copyLink = new Link("コピー", event -> {});
        add(new JScrollPane(ep), "growx, h 200:n:n, wrap");
        add(enterButton, "split 2");
        add(cancelButton, "wrap");
        add(deleteLink, "split 3");
        add(prescLink);
        add(copyLink);
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    private void doEnter(){
        TextDTO newText = textDTO.copy();
        newText.content = ep.getText();
        Service.api.updateText(newText)
                .thenCompose(res -> Service.api.getText(newText.textId))
                .thenAccept(enteredText -> {
                    TextAreaContext.get(this).onTextUpdated(enteredText, this);
                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });
    }

    private void doDelete(){
        int select = JOptionPane.showConfirmDialog(this, "この文章を削除していいですか？", "確認",
                JOptionPane.YES_NO_OPTION);
        if( select == JOptionPane.YES_OPTION ){
            Service.api.deleteText(textDTO.textId)
                    .thenAccept(res -> EventQueue.invokeLater(() ->{
                        TextAreaContext.get(this).onTextDeleted(this, textDTO.textId);
                    }))
                    .exceptionally(t -> {
                        t.printStackTrace();
                        EventQueue.invokeLater(() -> {
                            alert(t.toString());
                        });
                        return null;
                    });
        }
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}