package jp.chang.myclinic.practice.javafx.drug.lib;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.DrugDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DrugEditInputState extends DrugInputState {

    private DrugDTO origDrug;
    private boolean allFixed;

    void assignTo(DrugEditInputState dst){
        super.assignTo(dst);
        dst.setOrigDrug(getOrigDrug());
        dst.setAllFixed(isAllFixed());
    }

    private void adjustInputs(){
        if( isAllFixed() ){
            DrugCategory origCategory = DrugCategory.fromCode(origDrug.category);
            if( origCategory == getCategory() ){
                DrugHelper helper = new DrugHelper();
                setAmount(helper.formatAmount(origDrug.amount));
            }
        }
    }

    DrugDTO getOrigDrug() {
        return origDrug;
    }

    void setOrigDrug(DrugDTO origDrug) {
        this.origDrug = origDrug;
    }

    boolean isAllFixed() {
        return allFixed;
    }

    void setAllFixed(boolean allFixed) {
        this.allFixed = allFixed;
    }
}
