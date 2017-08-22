package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.leftpane.conduct.ConductArea;
import jp.chang.myclinic.practice.leftpane.drug.DrugArea;
import jp.chang.myclinic.practice.leftpane.hoken.HokenDisp;
import jp.chang.myclinic.practice.leftpane.shinryou.ShinryouArea;
import jp.chang.myclinic.practice.leftpane.text.TextArea1;
import jp.chang.myclinic.practice.leftpane.title.Title;
import jp.chang.myclinic.util.NumberUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.List;

class Record1 extends JPanel {

    interface Callback {
        default void onCopyAllDrugs(int targetVisitId, List<Integer> drugIds){}
    }

    private DrugArea drugArea;
    private Callback callback = new Callback(){};

    Record1(VisitFull2DTO visitFull, int currentVisitId, int tempVisitId){
        setLayout(new MigLayout("insets 0, fillx", "[sizegroup c, grow] [sizegroup c, grow]", ""));
        Title title = new Title(visitFull.visit, currentVisitId, tempVisitId);
        TextArea1 textArea1 = new TextArea1(visitFull);
        add(title, "span, growx, wrap");
        add(textArea1, "top, growx");
        add(makeRightPane(visitFull, currentVisitId, tempVisitId), "top, growx");
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    void appendDrugs(List<DrugFullDTO> drugs){
        this.drugArea.appendDrugs(drugs);
    }

    private JComponent makeRightPane(VisitFull2DTO visitFull, int currentVisitId, int tempVisitId){
        JPanel panel = new JPanel(new MigLayout("insets 0", "[grow]", ""));
        panel.add(new HokenDisp(visitFull.hoken, visitFull.visit), "wrap");
        panel.add(makeDrugArea(visitFull.drugs, visitFull.visit, currentVisitId, tempVisitId), "growx, wrap");
        panel.add(makeShinryouArea(visitFull.shinryouList, visitFull.visit, currentVisitId, tempVisitId), "growx, wrap");
        panel.add(makeConductArea(visitFull.conducts, visitFull.visit, currentVisitId, tempVisitId), "growx, wrap");
        panel.add(makeChargePane(visitFull.charge), "");
        return panel;

    }

    private DrugArea makeDrugArea(List<DrugFullDTO> drugs, VisitDTO visit, int currentVisitId, int tempVisitId){
        this.drugArea = new DrugArea(drugs, visit, currentVisitId, tempVisitId,
                new DrugArea.Callback() {
//                    @Override
//                    public void onCopyAll(int targetVisitId, List<Integer> drugIds) {
//                        callback.onCopyAllDrugs(targetVisitId, drugIds);
//                    }
                });
        return this.drugArea;
    }

    private ShinryouArea makeShinryouArea(List<ShinryouFullDTO> shinryouList, VisitDTO visit, int currentVisitId, int tempVisitId){
        ShinryouArea shinryouArea = new ShinryouArea(shinryouList, visit, currentVisitId, tempVisitId);
        return shinryouArea;
    }

    private ConductArea makeConductArea(List<ConductFullDTO> conducts, VisitDTO visit, int currentVisitId, int tempVisitId){
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
