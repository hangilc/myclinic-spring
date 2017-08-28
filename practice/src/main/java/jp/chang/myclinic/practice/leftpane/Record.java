package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.FixedWidthLayout;
import jp.chang.myclinic.practice.MainExecContext;
import jp.chang.myclinic.practice.PracticeUtil;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.leftpane.conduct.ConductArea;
import jp.chang.myclinic.practice.leftpane.drug.DrugArea;
import jp.chang.myclinic.practice.leftpane.drug.DrugHandler;
import jp.chang.myclinic.practice.leftpane.hoken.HokenChooser;
import jp.chang.myclinic.practice.leftpane.hoken.HokenDisp;
import jp.chang.myclinic.practice.leftpane.shinryou.ShinryouArea;
import jp.chang.myclinic.practice.leftpane.text.TextArea;
import jp.chang.myclinic.practice.leftpane.title.Title;
import jp.chang.myclinic.util.NumberUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

class Record extends JPanel {

    interface Callback {
        default void onDrugsCopied(int targetVisitId, List<DrugFullDTO> drugs){}
    }

    //private VisitFull2DTO fullVisit;
    private MainExecContext mainExecContext;
    private int width;
    private int colWidth;
    private Title title;
    private TextArea textArea;
    private DrugHandler drugHandler;
    private DrugArea drugArea;
    private Callback callback;

    Record(VisitFull2DTO fullVisit, int width, MainExecContext mainExecContext){
        //this.fullVisit = fullVisit;
        this.mainExecContext = mainExecContext;
        this.width = width;
        this.colWidth = (width - 4) / 2;
        int gap = width - colWidth * 2;
        setLayout(new MigLayout("insets 0",
                String.format("[%dpx!]%d[%dpx!]", colWidth, gap, colWidth),
                ""));
        title = new Title(fullVisit.visit, mainExecContext);
        textArea = new TextArea(fullVisit, colWidth);
        add(title, "span, growx, wrap");
        add(textArea, "top");
        add(makeRightColumn(fullVisit), "top");
    }

    @Override
    public int getWidth() {
        return width;
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    void addDrugs(List<DrugFullDTO> drugs){
        drugArea.appendDrugs(drugs);
    }

    private JComponent makeRightColumn(VisitFull2DTO fullVisit){
        JPanel panel = new JPanel(new FixedWidthLayout(colWidth)){
            @Override
            public int getWidth() {
                return colWidth;
            }
        };
        panel.add(new Box());
        drugHandler = new DrugHandler(colWidth, panel);
        //drugArea = new DrugArea(fullVisit.drugs, fullVisit.visit, colWidth, mainExecContext);
        //bindDrugArea(drugArea);
        panel.add(makeHokenDisp(fullVisit.hoken, fullVisit.visit, panel));
        //panel.add(drugArea);
        drugHandler.setup(panel);
        panel.add(new ShinryouArea(fullVisit.shinryouList, fullVisit.visit, colWidth, mainExecContext), "wrap");
        panel.add(new ConductArea(fullVisit.conducts, colWidth, mainExecContext), "wrap");
        panel.add(makeChargePane(fullVisit.charge));
        return panel;
    }

    private JComponent makeHokenDisp(HokenDTO hoken, VisitDTO visit, JPanel wrapper){
        HokenDisp disp = new HokenDisp(colWidth, hoken);
        disp.setCursor(PracticeUtil.handCursor);
        disp.setOnClick(() -> {
            Service.api.listAvailableHoken(visit.patientId, visit.visitedAt.substring(0, 10))
                    .thenAccept(available -> EventQueue.invokeLater(() -> {
                        HokenChooser chooser = makeHokenChooser(available, hoken, visit, wrapper, disp);
                        wrapper.add(chooser, new FixedWidthLayout.Replace(disp));
                        wrapper.repaint();
                        wrapper.revalidate();
                    }))
                    .exceptionally(t -> {
                        t.printStackTrace();
                        EventQueue.invokeLater(() -> {
                            alert(t.toString());
                        });
                        return null;
                    });
        });
        return disp;
    }

    private HokenChooser makeHokenChooser(HokenDTO available, HokenDTO origHoken, VisitDTO visit,
                                          JPanel wrapper, JComponent disp){
        String atDate = visit.visitedAt.substring(0, 10);
        HokenChooser chooser = new HokenChooser(available, origHoken, visit.visitId, atDate, visit.patientId);
        chooser.setCallback(new HokenChooser.Callback(){
            @Override
            public void onEnter(HokenDTO hoken) {
                wrapper.add(makeHokenDisp(hoken, visit, wrapper), new FixedWidthLayout.Replace(chooser));
                wrapper.repaint();
                wrapper.revalidate();
            }

            @Override
            public void onCancel() {
                wrapper.add(disp, new FixedWidthLayout.Replace(chooser));
                wrapper.repaint();
                wrapper.revalidate();
            }
        });
        return chooser;
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

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }


    private static class Box extends JPanel {
        Box() {
            super(new MigLayout("insets 0, fillx, debug", "[grow]", ""));
            add(new JLabel("Box"), "");
        }

    }
}
