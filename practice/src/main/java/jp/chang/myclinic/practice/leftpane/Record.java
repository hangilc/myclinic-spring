package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.*;
import jp.chang.myclinic.practice.leftpane.conduct.ConductBox;
import jp.chang.myclinic.practice.leftpane.drug.DrugHandler;
import jp.chang.myclinic.practice.leftpane.hoken.HokenChooser;
import jp.chang.myclinic.practice.leftpane.hoken.HokenDisp;
import jp.chang.myclinic.practice.leftpane.shinryou.ShinryouBox;
import jp.chang.myclinic.practice.leftpane.text.TextArea;
import jp.chang.myclinic.practice.leftpane.title.Title;
import jp.chang.myclinic.util.NumberUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// TODO: make charge editable
class Record extends JPanel implements RecordContext {

    interface Callback {
        default void onDrugsCopied(int targetVisitId, List<DrugFullDTO> drugs){}
    }

    private int width;
    private int colWidth;
    private Title title;
    private TextArea textArea;
    private DrugHandler drugHandler;
    private ShinryouBox shinryouBox;
    private ConductBox conductBox;
    private Callback callback;

    Record(VisitFull2DTO fullVisit, int width, int currentVisitId, int tempVisitId){
        this.width = width;
        this.colWidth = (width - 4) / 2;
        int gap = width - colWidth * 2;
        setLayout(new MigLayout("insets 0",
                String.format("[%dpx!]%d[%dpx!]", colWidth, gap, colWidth),
                ""));
        title = new Title(fullVisit.visit, currentVisitId, tempVisitId);
        textArea = new TextArea(fullVisit, colWidth);
        add(title, "span, growx, wrap");
        add(textArea, "top");
        add(makeRightColumn(fullVisit), "top");
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public void onDrugsModified(List<DrugFullDTO> modifiedDrugs) {
        drugHandler.onDrugsModified(modifiedDrugs);
    }

    @Override
    public void onDrugsDeleted(List<Integer> drugIds) {
        drugHandler.onDrugsDeleted(drugIds);
    }

    @Override
    public void onConductsEntered(List<ConductFullDTO> conducts) {
        conducts.forEach(conductBox::append);
        conductBox.revalidate();
        conductBox.repaint();
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    void appendDrugs(List<DrugFullDTO> drugs){
        drugHandler.appendDrugs(drugs);
    }

    void appendShinryou(List<ShinryouFullDTO> entered) {
        shinryouBox.appendShinryou(entered);
    }

    public void appendConduct(List<ConductFullDTO> entered) {
        conductBox.appendConduct(entered);
    }

    private JComponent makeRightColumn(VisitFull2DTO fullVisit){
        JPanel panel = new JPanel(new FixedWidthLayout(colWidth)){
            @Override
            public int getWidth() {
                return colWidth;
            }
        };
        drugHandler = new DrugHandler(colWidth, panel, fullVisit.visit);
        shinryouBox = new ShinryouBox(colWidth, fullVisit.shinryouList, fullVisit.visit);
        conductBox = new ConductBox(colWidth, fullVisit.conducts, fullVisit.visit);
        panel.add(makeHokenDisp(fullVisit.hoken, fullVisit.visit, panel));
        drugHandler.setup(fullVisit.drugs);
        panel.add(shinryouBox);
        panel.add(conductBox);
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

//    private void bindDrugArea(DrugArea drugArea){
//        drugArea.setCallback(new DrugArea.Callback(){
//            @Override
//            public void onCopyAll(int targetVisitId, List<DrugFullDTO> enteredDrugs) {
//                callback.onDrugsCopied(targetVisitId, enteredDrugs);
//            }
//        });
//    }

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
