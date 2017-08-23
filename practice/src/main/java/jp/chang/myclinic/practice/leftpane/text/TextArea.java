package jp.chang.myclinic.practice.leftpane.text;

import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.practice.FixedWidthLayout;
import jp.chang.myclinic.practice.Link;
import jp.chang.myclinic.practice.WrappedText;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class TextArea extends JPanel implements TextAreaContext {

    private int width;
    private Map<TextEditor, WrappedText> editorMap = new HashMap<>();
    private Link newTextLink;

    public TextArea(VisitFull2DTO fullVisit, int width){
        this.width = width;
        setLayout(new FixedWidthLayout(width));
        fullVisit.texts.forEach(text -> {
            add(makeDisp(text));
        });
        newTextLink = new Link("[文章作成]");
        bindNewTextLink(newTextLink, fullVisit.visit.visitId);
        add(newTextLink);
    }

    private WrappedText makeDisp(TextDTO text){
        String content = text.content;
        if( content.isEmpty() ){
            content = "(空白)";
        }
        WrappedText textDisp = new WrappedText(width, content);
        bindTextDisp(textDisp, text);
        return textDisp;
    }

    private void bindTextDisp(WrappedText disp, TextDTO text){
        disp.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                TextEditor editor = new TextEditor(text, width);
                add(editor, new FixedWidthLayout.Replace(disp));
                editorMap.put(editor, disp);
                repaint();
                revalidate();
            }
        });
    }

    private void bindNewTextLink(Link link, int visitId){
        JPanel panel = this;
        link.setCallback(event -> {
            TextCreator textCreator = new TextCreator(visitId, width);
//            TextCreator textCreator = new TextCreator(textDTO, new TextCreator.Callback(){
//                @Override
//                public void onEnter(TextCreator creator) {
//                    Service.api.enterText(textDTO)
//                            .thenAccept(textId -> {
//                                textDTO.textId = textId;
//                                EventQueue.invokeLater(() -> {
//                                    panel.remove(creator);
//                                    panel.add(new TextDispWrapper(textDTO, width), "growx, wrap");
//                                    panel.add(link);
//                                    panel.repaint();
//                                    panel.revalidate();
//                                });
//                            })
//                            .exceptionally(t -> {
//                                EventQueue.invokeLater(() -> {
//                                    t.printStackTrace();
//                                    alert(t.toString());
//                                });
//                                return null;
//                            });
//                }
//
//                @Override
//                public void onCancel(TextCreator creator) {
//                    panel.remove(creator);
//                    panel.add(link);
//                    panel.repaint();
//                    panel.revalidate();
//                }
//            });
            panel.add(textCreator, new FixedWidthLayout.Replace(link));
            panel.repaint();
            panel.revalidate();
        });
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

    @Override
    public void onEditorCancel(TextEditor editor) {
        WrappedText disp = editorMap.getOrDefault(editor, null);
        if( disp != null ){
            add(disp, new FixedWidthLayout.Replace(editor));
            editorMap.remove(editor);
            repaint();
            revalidate();
        }
    }

    @Override
    public void onTextDeleted(TextEditor editor, int textId) {
        editorMap.remove(editor);
        remove(editor);
        repaint();
        revalidate();
    }

    @Override
    public void onTextUpdated(TextDTO enteredText, TextEditor editor) {
        WrappedText disp = makeDisp(enteredText);
        add(disp, new FixedWidthLayout.Replace(editor));
        editorMap.remove(editor);
        repaint();
        revalidate();
    }

    @Override
    public void onCreatorCancel(TextCreator creator) {
        add(newTextLink, new FixedWidthLayout.Replace(creator));
        repaint();
        revalidate();
    }

    @Override
    public void onTextEntered(TextDTO enteredText, TextCreator creator) {
        WrappedText disp = makeDisp(enteredText);
        add(newTextLink, new FixedWidthLayout.Replace(creator));
        add(disp, new FixedWidthLayout.Before(newTextLink));
        repaint();
        revalidate();
    }
}
