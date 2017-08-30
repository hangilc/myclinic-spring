package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.practice.FixedWidthLayout;

import java.awt.*;

class DrugElement {

    enum Kind { DISP, EDIT }

    private Kind kind;
    private int index;
    private DrugFullDTO drugFull;
    private int width;
    private DrugDisp disp;

    DrugElement(int index, DrugFullDTO drugFull, int width){
        this.index = index;
        this.drugFull = drugFull;
        this.width = width;
        this.kind = Kind.DISP;
        this.disp = new DrugDisp(index, drugFull, width);
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

    int getDrugId(){
        return drugFull.drug.drugId;
    }

    void onDrugModified(DrugFullDTO modifiedDrug){
        this.drugFull = modifiedDrug;
        if( kind == Kind.DISP ){
            DrugDisp origDisp = this.disp;
            this.disp = new DrugDisp(index, this.drugFull, width);
            origDisp.getParent().add(this.disp, new FixedWidthLayout.Replace(origDisp));
        }
    }
}
