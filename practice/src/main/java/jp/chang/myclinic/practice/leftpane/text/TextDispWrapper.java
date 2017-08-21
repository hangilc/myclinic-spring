package jp.chang.myclinic.practice.leftpane.text;

import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.practice.Service;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class TextDispWrapper extends JPanel {

    private int width;

    TextDispWrapper(TextDTO textDTO, int width){
        this.width = width;
        setLayout(new MigLayout("insets 0", String.format("[%dpx!]", width), ""));
        TextDisp textDisp = makeTextDisp(textDTO);
        add(textDisp, "wrap");
    }

    private TextDisp makeTextDisp(TextDTO textDTO){
        TextDisp textDisp = new TextDisp(textDTO, getBackground(), width);
        textDisp.setCallback(new TextDisp.Callback(){
            @Override
            public void onClick() {
                TextEditor textEditor = makeTextEditor(textDTO, textDisp);
                removeAll();
                add(textEditor, "growx, wrap");
                repaint();
                revalidate();
            }
        });
        return textDisp;
    }

    private TextEditor makeTextEditor(TextDTO textDTO, TextDisp origTextDisp){
        TextDispWrapper wrapper = this;
        return new TextEditor(textDTO, new TextEditor.Callback(){

            @Override
            public void onEnter(TextDTO newText) {
                Service.api.updateText(newText)
                        .thenAccept(result -> {
                            EventQueue.invokeLater(() -> {
                                wrapper.removeAll();
                                wrapper.add(makeTextDisp(newText), "wrap");
                                wrapper.repaint();
                                wrapper.revalidate();
                            });
                        })
                        .exceptionally(t -> {
                            EventQueue.invokeLater(() -> {
                                t.printStackTrace();
                                alert(t.toString());
                            });
                            return null;
                        });
            }

            @Override
            public void onDelete() {
                Service.api.deleteText(textDTO.textId)
                        .thenAccept(result -> {
                            EventQueue.invokeLater(() -> {
                                Container parent = wrapper.getParent();
                                parent.remove(wrapper);
                                parent.repaint();
                                parent.revalidate();
                            });
                        })
                        .exceptionally(t -> {
                            EventQueue.invokeLater(() -> {
                                t.printStackTrace();
                                alert(t.toString());
                            });
                            return null;
                        });
            }

            @Override
            public void onCancel() {
                removeAll();
                add(origTextDisp, "wrap");
                repaint();
                revalidate();
            }
        });
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
