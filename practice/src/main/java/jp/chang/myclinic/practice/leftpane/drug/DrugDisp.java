package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.practice.WrappedText;
import jp.chang.myclinic.util.DrugUtil;

class DrugDisp extends WrappedText {

    private String drugRep;

    DrugDisp(int index, DrugFullDTO drug, int width){
        super(width);
        drugRep = DrugUtil.drugRep(drug);
        update(index);
    }

    void modifyIndex(int index){
        update(index);
    }

    private void update(int index){
        setText(index + ")" + drugRep);
    }

}
