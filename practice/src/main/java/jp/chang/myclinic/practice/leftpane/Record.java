package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.leftpane.conduct.ConductArea;
import jp.chang.myclinic.practice.leftpane.drug.DrugArea;
import jp.chang.myclinic.practice.leftpane.hoken.HokenDisp;
import jp.chang.myclinic.practice.leftpane.shinryou.ShinryouArea;
import jp.chang.myclinic.practice.leftpane.text.TextArea;
import jp.chang.myclinic.practice.leftpane.title.Title;
import jp.chang.myclinic.util.NumberUtil;
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
        panel.add(new HokenDisp(visitFull.hoken, visitFull.visit), "wrap");
        panel.add(makeDrugArea(visitFull.drugs, visitFull.visit, currentVisitId, tempVisitId), "growx, wrap");
        panel.add(makeShinryouArea(visitFull.shinryouList), "growx, wrap");
        panel.add(makeConductArea(visitFull.conducts), "growx, wrap");
        panel.add(makeChargePane(visitFull.charge), "");
        return panel;

    }

    private DrugArea makeDrugArea(List<DrugFullDTO> drugs, VisitDTO visit, int currentVisitId, int tempVisitId){
        return new DrugArea(drugs, visit, currentVisitId, tempVisitId,
                new DrugArea.Callback() {
                    @Override
                    public void onCopyAll(int targetVisitId, List<Integer> drugIds) {

                    }
                });
    }

    private ShinryouArea makeShinryouArea(List<ShinryouFullDTO> shinryouList){
        ShinryouArea shinryouArea = new ShinryouArea(shinryouList);
        return shinryouArea;
    }

    private ConductArea makeConductArea(List<ConductFullDTO> conducts){
        ConductArea conductArea = new ConductArea(conducts);
        return conductArea;
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

}
