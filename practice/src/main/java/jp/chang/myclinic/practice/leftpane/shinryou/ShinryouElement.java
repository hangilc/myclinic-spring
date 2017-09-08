package jp.chang.myclinic.practice.leftpane.shinryou;

import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.WrappedText;

import java.awt.*;

class ShinryouElement {

    private int width;
    private ShinryouFullDTO shinryouFull;
    private VisitDTO visit;
    private Component disp;

    ShinryouElement(int width, ShinryouFullDTO shinryouFull, VisitDTO visit){
        this.width = width;
        this.shinryouFull = shinryouFull;
        this.visit = visit;
        disp = makeDisp();
    }

    Component getComponent(){
        return disp;
    }

    int getShinryoucode(){
        return shinryouFull.shinryou.shinryoucode;
    }

    ShinryouFullDTO getShinryouFull(){
        return shinryouFull;
    }

    private Component makeDisp(){
        WrappedText wt = new WrappedText(width, shinryouFull.master.name);
        return wt;
    }
}
