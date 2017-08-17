package jp.chang.myclinic.practice.leftpane.conduct;

import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.ConductDrugFullDTO;
import jp.chang.myclinic.dto.ConductFullDTO;
import jp.chang.myclinic.dto.ConductKizaiFullDTO;
import jp.chang.myclinic.dto.ConductShinryouFullDTO;
import jp.chang.myclinic.util.DrugUtil;
import jp.chang.myclinic.util.KizaiUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class ConductDispWrapper extends JPanel {

    ConductDispWrapper(ConductFullDTO conductFull){
        super(new MigLayout("insets 0, fillx, gapy 0", "[grow]", ""));
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
                JEditorPane ep = new JEditorPane("text/plain", conductFull.gazouLabel.label);
                ep.setBackground(getBackground());
                ep.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
                add(ep, "growx, wrap");
            }
        }
        for(ConductShinryouFullDTO shinryou: conductFull.conductShinryouList){
            String label = shinryou.master.name;
            JEditorPane ep = new JEditorPane("text/plain", label);
            ep.setBackground(getBackground());
            ep.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            add(ep, "growx, wrap");
        }
        for(ConductDrugFullDTO drug: conductFull.conductDrugs){
            String label = DrugUtil.conductDrugRep(drug);
            JEditorPane ep = new JEditorPane("text/plain", label);
            ep.setBackground(getBackground());
            ep.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            add(ep, "growx, wrap");
        }
        for(ConductKizaiFullDTO kizai: conductFull.conductKizaiList){
            String label = KizaiUtil.kizaiRep(kizai);
            JEditorPane ep = new JEditorPane("text/plain", label);
            ep.setBackground(getBackground());
            ep.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            add(ep, "growx, wrap");
        }
    }
}
