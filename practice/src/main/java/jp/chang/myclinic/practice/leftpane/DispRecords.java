package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
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
        return new MigLayout("insets 0 0 0 24, fillx", "[sizegroup c, grow] [sizegroup c, grow]", "");
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
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        panel.add(makeHokenPane(visitFull.hoken), "wrap");
        return panel;
    }

    private JComponent makeHokenPane(HokenDTO hoken){
        String rep = HokenUtil.hokenRep(hoken);
        return new JLabel(rep);
    }
}
