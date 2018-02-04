package jp.chang.myclinic.practice.javafx.drug;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.util.DrugUtil;

class PreviousPrescSearchResult implements SearchResultModel {

    private DrugFullDTO drugFull;

    PreviousPrescSearchResult(DrugFullDTO drug){
        this.drugFull = drug;
    }

    @Override
    public String rep() {
        return DrugUtil.drugRep(drugFull);
    }

    @Override
    public void stuffInto(DrugInputModel model) {
        DrugCommon.stuffMasterInto(drugFull.master, model);
        DrugDTO drug = drugFull.drug;
        model.setAmount("" + drug.amount);
        model.setUsage(drug.usage);
        DrugCategory category = DrugCategory.fromCode(drug.category);
        if( category != null ) {
            model.setCategory(category);
            if( category != DrugCategory.Gaiyou ) {
                model.setDays("" + drug.days);
            }
        }
        model.setComment("");

    }
}
