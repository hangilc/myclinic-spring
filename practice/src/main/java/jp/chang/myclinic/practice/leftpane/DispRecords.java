package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.util.DrugUtil;
import jp.chang.myclinic.util.HokenUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
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
            JEditorPane ep = new JEditorPane();
            ep.setContentType("text/plain");
            ep.setText(textDTO.content.trim());
            panel.add(ep, "growx, wrap");
        });
        return panel;
    }

    private JComponent makeRightPane(VisitFull2DTO visitFull){
        JPanel panel = new JPanel(new MigLayout("insets 0", "[grow]", ""));
        panel.add(makeHokenPane(visitFull.hoken), "wrap");
        panel.add(makeDrugPane(visitFull.drugs), "growx, wrap");
        panel.add(makeShinryouPane(visitFull.shinryouList), "growx, wrap");
        return panel;
    }

    private JComponent makeHokenPane(HokenDTO hoken){
        String rep = HokenUtil.hokenRep(hoken);
        return new JLabel(rep);
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
}
