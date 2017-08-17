package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.VisitFull2DTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.List;

class DispRecords extends JPanel {

    DispRecords(){
        setLayout(makeLayout());
        add(new JLabel("平成２９年７月２９日"), "span, wrap");
        add(new JLabel("left"), "");
        add(new JLabel("right right right right"), "wrap");
    }

    private MigLayout makeLayout(){
        return new MigLayout("insets 0, fillx", "[grow]", "");
    }

//    private MigLayout makeLayout(){
//        return new MigLayout("insets 0, fillx", "[sizegroup c, grow] [sizegroup c, grow]", "");
//    }
//
    void setVisits(List<VisitFull2DTO> visits, int currentVisitId, int tempVisitId){
        removeAll();
        setLayout(makeLayout());
        visits.forEach(visitFull -> {
            Record record = new Record(visitFull, currentVisitId, tempVisitId);
            add(record, "growx, wrap");
//            add(makeTitle(visitFull.visit, currentVisitId, tempVisitId), "span, growx, wrap");
//            add(makeTextPane(visitFull), "top, growx");
//            add(makeRightPane(visitFull, currentVisitId, tempVisitId), "top, growx, wrap");
        });
    }

//    private JComponent makeRightPane(VisitFull2DTO visitFull, int currentVisitId, int tempVisitId){
//        DrugArea drugArea = new DrugArea(visitFull.drugs, visitFull.visit, currentVisitId, tempVisitId,
//                new DrugArea.Callback() {
//                    @Override
//                    public void onCopyAll(int targetVisitId, List<Integer> drugIds) {
//
//                    }
//                });
//        JPanel panel = new JPanel(new MigLayout("insets 0", "[grow]", ""));
//        panel.add(new HokenDisp(visitFull.hoken, visitFull.visit), "wrap");
//        panel.add(drugArea, "growx, wrap");
//        panel.add(makeShinryouPane(visitFull.shinryouList), "growx, wrap");
//        panel.add(makeConductPane(visitFull.conducts), "growx, wrap");
//        panel.add(makeChargePane(visitFull.charge), "");
//        return panel;
//    }

//    private JComponent makeDrugPane(List<DrugFullDTO> drugs){
//        JPanel panel = new JPanel(new MigLayout("insets 0, gapy 0", "[grow]", ""));
//        if( drugs.size() > 0 ){
//            panel.add(new JLabel("Rp)"), "wrap");
//        }
//        int index = 1;
//        for(DrugFullDTO drug: drugs){
//            String label = String.format("%d)%s", index, DrugUtil.drugRep(drug));
//            index += 1;
//            JEditorPane ep = new JEditorPane("text/plain", label);
//            ep.setBackground(getBackground());
//            //ep.setBorder(BorderFactory.createEmptyBorder());
//            panel.add(ep, "growx, wrap");
//        }
//        return panel;
//    }

//    private JComponent makeShinryouPane(List<ShinryouFullDTO> shinryouList){
//        JPanel panel = new JPanel(new MigLayout("insets 0, gapy 0", "[grow]", ""));
//        for(ShinryouFullDTO shinryou: shinryouList){
//            String label = shinryou.master.name;
//            JEditorPane ep = new JEditorPane("text/plain", label);
//            ep.setBackground(getBackground());
//            ep.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
//            panel.add(ep, "growx, wrap");
//        }
//        return panel;
//    }

//    private JComponent makeConductPane(List<ConductFullDTO> conducts){
//        JPanel panel = new JPanel(new MigLayout("insets 0", "[grow]", ""));
//        for(ConductFullDTO conduct: conducts){
//            panel.add(makeConductItemPane(conduct), "growx, wrap");
//        }
//        return panel;
//    }
//
//    private JComponent makeConductItemPane(ConductFullDTO conductFull){
//        JPanel panel = new JPanel(new MigLayout("insets 0, fillx, gapy 0", "[grow]", ""));
//        {
//            String kindRep;
//            ConductKind conductKind = ConductKind.fromCode(conductFull.conduct.kind);
//            if( conductKind != null ){
//                kindRep = "<" + conductKind.getKanjiRep() + ">";
//            } else {
//                kindRep = "<" + "不明" + ">";
//            }
//            panel.add(new JLabel(kindRep), "wrap");
//        }
//        {
//            if( conductFull.gazouLabel != null ){
//                JEditorPane ep = new JEditorPane("text/plain", conductFull.gazouLabel.label);
//                ep.setBackground(getBackground());
//                ep.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
//                panel.add(ep, "growx, wrap");
//            }
//        }
//        for(ConductShinryouFullDTO shinryou: conductFull.conductShinryouList){
//            String label = shinryou.master.name;
//            JEditorPane ep = new JEditorPane("text/plain", label);
//            ep.setBackground(getBackground());
//            ep.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
//            panel.add(ep, "growx, wrap");
//        }
//        for(ConductDrugFullDTO drug: conductFull.conductDrugs){
//            String label = DrugUtil.conductDrugRep(drug);
//            JEditorPane ep = new JEditorPane("text/plain", label);
//            ep.setBackground(getBackground());
//            ep.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
//            panel.add(ep, "growx, wrap");
//        }
//        for(ConductKizaiFullDTO kizai: conductFull.conductKizaiList){
//            String label = KizaiUtil.kizaiRep(kizai);
//            JEditorPane ep = new JEditorPane("text/plain", label);
//            ep.setBackground(getBackground());
//            ep.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
//            panel.add(ep, "growx, wrap");
//        }
//        return panel;
//    }

//    private JComponent makeChargePane(ChargeDTO charge){
//        String label;
//        if( charge != null ){
//            label = "請求額：" + NumberUtil.formatNumber(charge.charge) + "円";
//        } else {
//            label = "(未請求)";
//        }
//        return new JLabel(label);
//    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
