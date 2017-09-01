package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.practice.WrappedText;
import jp.chang.myclinic.util.DrugUtil;

class DrugDisp extends WrappedText {

    private String drugRep;

    DrugDisp(int index, DrugFullDTO drug, int width){
        super(width);
        update(index, drug);
    }

    void update(int index, DrugFullDTO drugFull){
        String text = String.format("%d)%s", index, DrugUtil.drugRep(drugFull));
        setText(text);
    }

}
