package jp.chang.myclinic.practice.leftpane.text;

import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.practice.Service;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class TextCreator extends JPanel {

    public interface Callback {
        void onEnter(TextCreator creator);
        void onCancel(TextCreator creator);
    }

    private TextDTO textDTO;
    private JEditorPane ep;
    private Callback callback;

    public TextCreator(int visitId, int width){
        this.textDTO = new TextDTO();
        this.textDTO.visitId = visitId;
        setLayout(new MigLayout("insets 0", String.format("[%dpx!]", width), ""));
        ep = new JEditorPane("text/plain", textDTO.content);
        JButton enterButton = new JButton("入力");
        enterButton.addActionListener(event -> doEnter());
        JButton cancelButton = new JButton("キャンセル");
        cancelButton.addActionListener(event -> TextAreaContext.get(this).onCreatorCancel(this));
        add(new JScrollPane(ep), "growx, h 200:n:n, wrap");
        add(enterButton, "split 2");
        add(cancelButton, "");
    }

    private void doEnter(){
        textDTO.content = ep.getText();
        Service.api.enterText(textDTO)
                .thenCompose(textId -> Service.api.getText(textId))
                .thenAccept(enteredText -> EventQueue.invokeLater(() -> {
                    TextAreaContext.get(this).onTextEntered(enteredText, this);
                }))
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
