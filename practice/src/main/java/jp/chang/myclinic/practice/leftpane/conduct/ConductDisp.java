package jp.chang.myclinic.practice.leftpane.conduct;

import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.ConductFullDTO;
import jp.chang.myclinic.practice.FixedWidthLayout;

import javax.swing.*;
import java.awt.*;

class ConductDisp extends JPanel {

    ConductDisp(int width, ConductFullDTO conductFull){
        setLayout(new FixedWidthLayout(width));
        add(makeKindArea(conductFull.conduct.kind));

    }

    private Component makeKindArea(int kindCode){
        String kindRep;
        ConductKind conductKind = ConductKind.fromCode(kindCode);
        if( conductKind != null ){
            kindRep = "<" + conductKind.getKanjiRep() + ">";
        } else {
            kindRep = "<" + "不明" + ">";
        }
        return new JLabel(kindRep);
    }
}
