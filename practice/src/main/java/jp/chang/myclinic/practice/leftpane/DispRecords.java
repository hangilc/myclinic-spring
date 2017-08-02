package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.Link;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.leftpane.drug.DrugArea;
import jp.chang.myclinic.practice.leftpane.hoken.HokenDisp;
import jp.chang.myclinic.practice.leftpane.text.TextCreator;
import jp.chang.myclinic.practice.leftpane.text.TextDisp;
import jp.chang.myclinic.practice.leftpane.text.TextEditor;
import jp.chang.myclinic.util.DrugUtil;
import jp.chang.myclinic.util.KizaiUtil;
import jp.chang.myclinic.util.NumberUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class DispRecords extends JPanel {

    public DispRecords(){
        setLayout(makeLayout());
        add(new JLabel("平成２９年７月２９日"), "span, wrap");
        add(new JLabel("left"), "");
        add(new JLabel("right right right right"), "wrap");
    }

    private MigLayout makeLayout(){
        return new MigLayout("insets 0, fillx", "[sizegroup c, grow] [sizegroup c, grow]", "");
    }

    public void setVisits(List<VisitFull2DTO> visits){
        removeAll();
        setLayout(makeLayout());
        visits.forEach(visitFull -> {
            add(new JLabel(visitFull.visit.visitedAt), "span, wrap");
            add(makeTextPane(visitFull), "top, growx");
            add(makeRightPane(visitFull), "top, growx, wrap");
        });
    }

    private JComponent makeTextPane(VisitFull2DTO visitFull){
        JPanel panel = new JPanel(new MigLayout("insets 0, fillx", "[grow]", ""));
        visitFull.texts.forEach(textDTO -> {
            panel.add(makeTextItemPane(textDTO), "growx, wrap");
        });
        Link newTextLink = new Link("[文章作成]");
        newTextLink.setCallback(event -> {
            TextDTO textDTO = new TextDTO();
            textDTO.visitId = visitFull.visit.visitId;
            TextCreator textCreator = new TextCreator(textDTO, new TextCreator.Callback(){
                @Override
                public void onEnter(TextCreator creator) {
                    Service.api.enterText(textDTO)
                            .thenAccept(textId -> {
                                textDTO.textId = textId;
                                EventQueue.invokeLater(() -> {
                                    panel.remove(creator);
                                    panel.add(makeTextItemPane(textDTO), "growx, wrap");
                                    panel.add(newTextLink);
                                    panel.repaint();
                                    panel.revalidate();
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
                    panel.add(newTextLink);
                    panel.repaint();
                    panel.revalidate();
                }
            });
            panel.remove(newTextLink);
            panel.add(textCreator, "growx");
            panel.repaint();
            panel.revalidate();
        });
        panel.add(newTextLink);
        return panel;
    }

    private JComponent makeTextItemPane(TextDTO textDTO){
        JPanel wrapper = new JPanel(new MigLayout("insets 0", "[grow]", ""));
        addTextDisp(wrapper, textDTO);
        return wrapper;
    }

    private void addTextDisp(JPanel wrapper, TextDTO textDTO){
        TextDisp textDisp = new TextDisp(textDTO, getBackground());
        textDisp.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                TextEditor textEditor = new TextEditor(textDTO, new TextEditor.Callback(){
                    @Override
                    public void onEnter(TextDTO newText) {
                        Service.api.updateText(newText)
                                .thenAccept(result -> {
                                    EventQueue.invokeLater(() -> {
                                        wrapper.removeAll();
                                        addTextDisp(wrapper, newText);
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
                    public void onDelete(){
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
                        wrapper.removeAll();
                        wrapper.add(textDisp, "growx");
                        wrapper.repaint();
                        wrapper.revalidate();
                    }
                });
                wrapper.removeAll();
                wrapper.add(textEditor, "growx");
                wrapper.repaint();
                wrapper.revalidate();
            }
        });
        wrapper.add(textDisp, "growx");
    }

    private JComponent makeRightPane(VisitFull2DTO visitFull){
        JPanel panel = new JPanel(new MigLayout("insets 0", "[grow]", ""));
        panel.add(new HokenDisp(visitFull.hoken, visitFull.visit), "wrap");
        panel.add(new DrugArea(visitFull.drugs), "growx, wrap");
        panel.add(makeShinryouPane(visitFull.shinryouList), "growx, wrap");
        panel.add(makeConductPane(visitFull.conducts), "growx, wrap");
        panel.add(makeChargePane(visitFull.charge), "");
        return panel;
    }

    private JComponent makeDrugPane(List<DrugFullDTO> drugs){
        JPanel panel = new JPanel(new MigLayout("insets 0, gapy 0", "[grow]", ""));
        if( drugs.size() > 0 ){
            panel.add(new JLabel("Rp)"), "wrap");
        }
        int index = 1;
        for(DrugFullDTO drug: drugs){
            String label = String.format("%d)%s", index, DrugUtil.drugRep(drug));
            index += 1;
            JEditorPane ep = new JEditorPane("text/plain", label);
            ep.setBackground(getBackground());
            //ep.setBorder(BorderFactory.createEmptyBorder());
            panel.add(ep, "growx, wrap");
        }
        return panel;
    }

    private JComponent makeShinryouPane(List<ShinryouFullDTO> shinryouList){
        JPanel panel = new JPanel(new MigLayout("insets 0, gapy 0", "[grow]", ""));
        for(ShinryouFullDTO shinryou: shinryouList){
            String label = shinryou.master.name;
            JEditorPane ep = new JEditorPane("text/plain", label);
            ep.setBackground(getBackground());
            ep.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            panel.add(ep, "growx, wrap");
        }
        return panel;
    }

    private JComponent makeConductPane(List<ConductFullDTO> conducts){
        JPanel panel = new JPanel(new MigLayout("insets 0", "[grow]", ""));
        for(ConductFullDTO conduct: conducts){
            panel.add(makeConductItemPane(conduct), "growx, wrap");
        }
        return panel;
    }

    private JComponent makeConductItemPane(ConductFullDTO conductFull){
        JPanel panel = new JPanel(new MigLayout("insets 0, fillx, gapy 0", "[grow]", ""));
        {
            String kindRep;
            ConductKind conductKind = ConductKind.fromCode(conductFull.conduct.kind);
            if( conductKind != null ){
                kindRep = "<" + conductKind.getKanjiRep() + ">";
            } else {
                kindRep = "<" + "不明" + ">";
            }
            panel.add(new JLabel(kindRep), "wrap");
        }
        {
            if( conductFull.gazouLabel != null ){
                JEditorPane ep = new JEditorPane("text/plain", conductFull.gazouLabel.label);
                ep.setBackground(getBackground());
                ep.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
                panel.add(ep, "growx, wrap");
            }
        }
        for(ConductShinryouFullDTO shinryou: conductFull.conductShinryouList){
            String label = shinryou.master.name;
            JEditorPane ep = new JEditorPane("text/plain", label);
            ep.setBackground(getBackground());
            ep.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            panel.add(ep, "growx, wrap");
        }
        for(ConductDrugFullDTO drug: conductFull.conductDrugs){
            String label = DrugUtil.conductDrugRep(drug);
            JEditorPane ep = new JEditorPane("text/plain", label);
            ep.setBackground(getBackground());
            ep.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            panel.add(ep, "growx, wrap");
        }
        for(ConductKizaiFullDTO kizai: conductFull.conductKizaiList){
            String label = KizaiUtil.kizaiRep(kizai);
            JEditorPane ep = new JEditorPane("text/plain", label);
            ep.setBackground(getBackground());
            ep.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            panel.add(ep, "growx, wrap");
        }
        return panel;
    }

    private JComponent makeChargePane(ChargeDTO charge){
        String label;
        if( charge != null ){
            label = "請求額：" + NumberUtil.formatNumber(charge.charge) + "円";
        } else {
            label = "(未請求)";
        }
        return new JLabel(label);
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
