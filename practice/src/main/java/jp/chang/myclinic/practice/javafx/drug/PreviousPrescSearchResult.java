package jp.chang.myclinic.practice.javafx.drug;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.practice.lib.PracticeLib;
import jp.chang.myclinic.util.DrugUtil;

class PreviousPrescSearchResult implements SearchResultModel {

    private DrugFullDTO drugFull;
    private String at;

    PreviousPrescSearchResult(DrugFullDTO drug, String at){
        this.drugFull = drug;
        this.at = at;
    }

    @Override
    public String rep() {
        return DrugUtil.drugRep(drugFull);
    }

    @Override
    public void stuffInto(DrugInputModel model, InputConstraints constraints) {
        int origIyakuhincode = drugFull.master.iyakuhincode;
        PracticeLib.resolveIyakuhinMaster(origIyakuhincode, at, master -> {
            DrugCommon.stuffMasterInto(master, model, constraints);
            DrugDTO drug = drugFull.drug;
            if( !constraints.isAmountFixed() ) {
                model.setAmount("" + drug.amount);
            }
            if( !constraints.isUsageFixed() ) {
                model.setUsage(drug.usage);
            }
            DrugCategory category = DrugCategory.fromCode(drug.category);
            if( category != null ) {
                model.setCategory(category);
                if( category != DrugCategory.Gaiyou ) {
                    if( !constraints.isDaysFixed() ) {
                        model.setDays("" + drug.days);
                    }
                }
            }
            model.setComment("");
        });
    }
}
