package jp.chang.myclinic.practice.leftpane.conduct;

import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.ConductDrugFullDTO;
import jp.chang.myclinic.dto.ConductFullDTO;
import jp.chang.myclinic.dto.ConductKizaiFullDTO;
import jp.chang.myclinic.dto.ConductShinryouFullDTO;
import jp.chang.myclinic.practice.FixedWidthLayout;
import jp.chang.myclinic.practice.MainExecContext;
import jp.chang.myclinic.util.DrugUtil;
import jp.chang.myclinic.util.KizaiUtil;

import javax.swing.*;

class ConductDispWrapper extends JPanel {

    ConductDispWrapper(ConductFullDTO conductFull){
//        super(new MigLayout("insets 0, fillx, gapy 0", "[grow]", ""));
//        {
//            String kindRep;
//            ConductKind conductKind = ConductKind.fromCode(conductFull.conduct.kind);
//            if( conductKind != null ){
//                kindRep = "<" + conductKind.getKanjiRep() + ">";
//            } else {
//                kindRep = "<" + "不明" + ">";
//            }
//            add(new JLabel(kindRep), "wrap");
//        }
//        {
//            if( conductFull.gazouLabel != null ){
//                JEditorPane ep = new JEditorPane("text/plain", conductFull.gazouLabel.label);
//                ep.setBackground(getBackground());
//                ep.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
//                add(ep, "growx, wrap");
//            }
//        }
//        for(ConductShinryouFullDTO shinryou: conductFull.conductShinryouList){
//            String label = shinryou.master.name;
//            JEditorPane ep = new JEditorPane("text/plain", label);
//            ep.setBackground(getBackground());
//            ep.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
//            add(ep, "growx, wrap");
//        }
//        for(ConductDrugFullDTO drug: conductFull.conductDrugs){
//            String label = DrugUtil.conductDrugRep(drug);
//            JEditorPane ep = new JEditorPane("text/plain", label);
//            ep.setBackground(getBackground());
//            ep.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
//            add(ep, "growx, wrap");
//        }
//        for(ConductKizaiFullDTO kizai: conductFull.conductKizaiList){
//            String label = KizaiUtil.kizaiRep(kizai);
//            JEditorPane ep = new JEditorPane("text/plain", label);
//            ep.setBackground(getBackground());
//            ep.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
//            add(ep, "growx, wrap");
//        }
    }

    ConductDispWrapper(ConductFullDTO conductFull, int width, MainExecContext mainExecContext){
        super(new FixedWidthLayout(width));
        //super(new MigLayout("insets 0, gapy 0", String.format("[%dpx!]", width), ""));
        {
            String kindRep;
            ConductKind conductKind = ConductKind.fromCode(conductFull.conduct.kind);
            if( conductKind != null ){
                kindRep = "<" + conductKind.getKanjiRep() + ">";
            } else {
                kindRep = "<" + "不明" + ">";
            }
            add(new JLabel(kindRep), "wrap");
        }
        {
            if( conductFull.gazouLabel != null ){
                JEditorPane ep = makeDispPane(conductFull.gazouLabel.label, width);
                add(ep, "wrap");
            }
        }
        for(ConductShinryouFullDTO shinryou: conductFull.conductShinryouList){
            String label = shinryou.master.name;
            JEditorPane ep = makeDispPane(label, width);
            add(ep, "wrap");
        }
        for(ConductDrugFullDTO drug: conductFull.conductDrugs){
            String label = DrugUtil.conductDrugRep(drug);
            JEditorPane ep = makeDispPane(label, width);
            add(ep, "wrap");
        }
        for(ConductKizaiFullDTO kizai: conductFull.conductKizaiList){
            String label = KizaiUtil.kizaiRep(kizai);
            JEditorPane ep = makeDispPane(label, width);
            add(ep, "wrap");
        }
    }

    private JEditorPane makeDispPane(String text, int width){
        JEditorPane ep = new JEditorPane();
        ep.setContentType("text/plain");
        ep.setSize(width, Integer.MAX_VALUE);
        ep.setText(text);
        ep.setEditable(false);
        ep.setBackground(getBackground());
        ep.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        return ep;
    }

}
