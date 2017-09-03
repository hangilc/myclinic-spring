package jp.chang.myclinic.practice.leftpane.conduct;

import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.ConductDrugFullDTO;
import jp.chang.myclinic.dto.ConductFullDTO;
import jp.chang.myclinic.dto.ConductKizaiFullDTO;
import jp.chang.myclinic.dto.ConductShinryouFullDTO;
import jp.chang.myclinic.practice.FixedWidthLayout;
import jp.chang.myclinic.practice.WrappedText;
import jp.chang.myclinic.util.DrugUtil;
import jp.chang.myclinic.util.KizaiUtil;

import javax.swing.*;
import java.awt.*;

class ConductElement {

    private int width;
    private ConductFullDTO conductFull;
    private Component disp;

    ConductElement(int width, ConductFullDTO conductFull){
        this.width = width;
        this.conductFull = conductFull;
        this.disp = makeDisp();
    }

    Component getComponent(){
        return this.disp;
    }

    private Component makeDisp(){
        JPanel panel = new JPanel(new FixedWidthLayout(width));
        {
            String kindRep;
            ConductKind conductKind = ConductKind.fromCode(conductFull.conduct.kind);
            if( conductKind != null ){
                kindRep = "<" + conductKind.getKanjiRep() + ">";
            } else {
                kindRep = "<" + "不明" + ">";
            }
            panel.add(new JLabel(kindRep));
        }
        if( conductFull.gazouLabel != null ){
            panel.add(new WrappedText(width, conductFull.gazouLabel.label));
        }
        for(ConductShinryouFullDTO shinryou: conductFull.conductShinryouList){
            String label = shinryou.master.name;
            panel.add(new WrappedText(width, label));
        }
        for(ConductDrugFullDTO drug: conductFull.conductDrugs){
            String label = DrugUtil.conductDrugRep(drug);
            panel.add(new WrappedText(width, label));
        }
        for(ConductKizaiFullDTO kizai: conductFull.conductKizaiList){
            String label = KizaiUtil.kizaiRep(kizai);
            panel.add(new WrappedText(width, label));
        }
        return panel;
    }
}
