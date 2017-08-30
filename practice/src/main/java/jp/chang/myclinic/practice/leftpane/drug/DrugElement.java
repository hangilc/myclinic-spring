package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.dto.DrugFullDTO;

import java.awt.*;

class DrugElement {

    enum Kind { DISP, EDIT }

    private Kind kind;
    private int index;
    private DrugFullDTO drug;
    private int width;
    private DrugDisp disp;

    DrugElement(int index, DrugFullDTO drug, int width){
        this.index = index;
        this.drug = drug;
        this.width = width;
        this.kind = Kind.DISP;
        this.disp = new DrugDisp(index, drug, width);
    }

    DrugDisp getDisp(){
        return disp;
    }

    Component getComponent(){
        switch(this.kind){
            case DISP: return disp;
            default: throw new RuntimeException("cannot find component");
        }
    }
}
