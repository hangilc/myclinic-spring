package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.practice.leftpane.drug.DrugArea;
import jp.chang.myclinic.practice.leftpane.hoken.HokenDisp;
import jp.chang.myclinic.practice.leftpane.text.TextArea;
import jp.chang.myclinic.practice.leftpane.title.Title;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.List;

class Record extends JPanel {

    Record(VisitFull2DTO visitFull, int currentVisitId, int tempVisitId){
        setLayout(new MigLayout("insets 0, fillx", "[sizegroup c, grow] [sizegroup c, grow]", ""));
        Title title = new Title(visitFull.visit, currentVisitId, tempVisitId);
        TextArea textArea = new TextArea(visitFull);
        add(title, "span, growx, wrap");
        add(textArea, "top, growx");
        add(makeRightPane(visitFull, currentVisitId, tempVisitId), "top, growx");
    }

    private JComponent makeRightPane(VisitFull2DTO visitFull, int currentVisitId, int tempVisitId){
        JPanel panel = new JPanel(new MigLayout("insets 0", "[grow]", ""));
        DrugArea drugArea = new DrugArea(visitFull.drugs, visitFull.visit, currentVisitId, tempVisitId,
                new DrugArea.Callback() {
                    @Override
                    public void onCopyAll(int targetVisitId, List<Integer> drugIds) {

                    }
                });
        panel.add(new HokenDisp(visitFull.hoken, visitFull.visit), "wrap");
        panel.add(drugArea, "growx, wrap");
//        panel.add(makeShinryouPane(visitFull.shinryouList), "growx, wrap");
//        panel.add(makeConductPane(visitFull.conducts), "growx, wrap");
//        panel.add(makeChargePane(visitFull.charge), "");
        return panel;

    }
}
