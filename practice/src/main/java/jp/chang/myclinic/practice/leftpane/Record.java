package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.ChargeDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.practice.MainExecContext;
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

    interface Callback {
        default void onDrugsCopied(int targetVisitId, List<DrugFullDTO> drugs){}
    }

    private VisitFull2DTO fullVisit;
    private MainExecContext mainExecContext;
    private int colWidth;
    private Title title;
    private TextArea textArea;
    private DrugArea drugArea;
    private Callback callback;

    Record(VisitFull2DTO fullVisit, int width, MainExecContext mainExecContext){
        this.fullVisit = fullVisit;
        this.mainExecContext = mainExecContext;
        colWidth = (width - 4) / 2;
        int gap = width - colWidth * 2;
        setLayout(new MigLayout("insets 0",
                String.format("[%dpx!]%d[%dpx!]", colWidth, gap, colWidth),
                ""));
        title = new Title(fullVisit.visit, mainExecContext);
        textArea = new TextArea(fullVisit, colWidth);
        add(title, "span, growx, wrap");
        add(textArea, "top");
        add(makeRightColumn(), "top");
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    void addDrugs(List<DrugFullDTO> drugs){
        drugArea.appendDrugs(drugs);
    }

    private JComponent makeRightColumn(){
        JPanel panel = new JPanel(new MigLayout("insets 0", String.format("[%dpx!]", colWidth), ""));
        drugArea = new DrugArea(fullVisit.drugs, fullVisit.visit, colWidth, mainExecContext);
        bindDrugArea(drugArea);
        panel.add(new HokenDisp(fullVisit.hoken, fullVisit.visit), "wrap");
        panel.add(drugArea, "wrap");
        panel.add(new ShinryouArea(fullVisit.shinryouList, fullVisit.visit, colWidth, mainExecContext), "wrap");
        panel.add(new ConductArea(fullVisit.conducts, colWidth, mainExecContext), "wrap");
        panel.add(makeChargePane(fullVisit.charge));
        return panel;
    }

    private void bindDrugArea(DrugArea drugArea){
        drugArea.setCallback(new DrugArea.Callback(){
            @Override
            public void onCopyAll(int targetVisitId, List<DrugFullDTO> enteredDrugs) {
                callback.onDrugsCopied(targetVisitId, enteredDrugs);
            }
        });
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
