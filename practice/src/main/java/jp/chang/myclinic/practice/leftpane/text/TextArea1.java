package jp.chang.myclinic.practice.leftpane.text;

import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.practice.Link;
import jp.chang.myclinic.practice.Service;

import javax.swing.*;
import java.awt.*;

public class TextArea1 extends JPanel {

    public TextArea1(VisitFull2DTO visitFull){
//        setLayout(new MigLayout("insets 0", "[grow]", ""));
//        visitFull.texts.forEach(text -> {
//            TextDispWrapper wrapper = new TextDispWrapper(text);
//            add(wrapper, "growx, wrap");
//        });
//        Link newTextLink = new Link("[文章作成]");
//        bindNewTextLink(newTextLink, visitFull.visit.visitId);
//        add(newTextLink);
    }

    private void bindNewTextLink(Link link, int visitId){
        JPanel panel = this;
        link.setCallback(event -> {
            TextDTO textDTO = new TextDTO();
            textDTO.visitId = visitId;
            TextCreator textCreator = new TextCreator(textDTO, new TextCreator.Callback(){
                @Override
                public void onEnter(TextCreator creator) {
                    Service.api.enterText(textDTO)
                            .thenAccept(textId -> {
                                textDTO.textId = textId;
                                EventQueue.invokeLater(() -> {
//                                    panel.remove(creator);
//                                    panel.add(new TextDispWrapper(textDTO), "growx, wrap");
//                                    panel.add(link);
//                                    panel.repaint();
//                                    panel.revalidate();
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
                public void onCancel(TextCreator creator) {
                    panel.remove(creator);
                    panel.add(link);
                    panel.repaint();
                    panel.revalidate();
                }
            });
            panel.remove(link);
            panel.add(textCreator, "growx");
            panel.repaint();
            panel.revalidate();
        });
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
