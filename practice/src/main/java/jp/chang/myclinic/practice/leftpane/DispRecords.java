package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.VisitFullDTO;
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

    public void setVisits(List<VisitFullDTO> visits){
        removeAll();
        setLayout(makeLayout());
        visits.forEach(visitFull -> {
            add(new JLabel(visitFull.visit.visitedAt), "span, wrap");
            add(makeTextPane(visitFull), "top, growx");
            add(new JLabel("right pane"), "top, growx, wrap");
        });
    }

    private JComponent makeTextPane(VisitFullDTO visitFull){
        JPanel panel = new JPanel(new MigLayout("insets 0, fillx", "[grow]", ""));
        visitFull.texts.forEach(textDTO -> {
            JEditorPane ep = new JEditorPane();
            ep.setContentType("text/plain");
            ep.setText(textDTO.content.trim());
            panel.add(ep, "growx, wrap");
        });
        return panel;
    }
}
