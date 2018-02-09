package jp.chang.myclinic.practice.javafx.drug;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.PrescExampleDTO;
import jp.chang.myclinic.dto.PrescExampleFullDTO;
import jp.chang.myclinic.practice.lib.DrugInputConstraints;
import jp.chang.myclinic.practice.lib.PracticeLib;
import jp.chang.myclinic.practice.lib.DrugSearchResultModel;
import jp.chang.myclinic.util.PrescExampleUtil;

class ExampleSearchResult implements DrugSearchResultModel {

    private PrescExampleFullDTO example;
    private String at;

    ExampleSearchResult(PrescExampleFullDTO example, String at){
        this.example = example;
        this.at = at;
    }

    @Override
    public String rep() {
        return PrescExampleUtil.rep(example);
    }

    @Override
    public void stuffInto(DrugInputModel model, DrugInputConstraints constraints) {
        int origIyakuhincode = example.master.iyakuhincode;
        PracticeLib.resolveIyakuhinMaster(origIyakuhincode, at, master -> {
            DrugCommon.stuffMasterInto(master, model, constraints);
            PrescExampleDTO prescExample = example.prescExample;
            if( !constraints.isAmountFixed() || model.getAmount().isEmpty() ) {
                model.setAmount(prescExample.amount);
            }
            if( !constraints.isUsageFixed() || model.getUsage().isEmpty() ) {
                model.setUsage(prescExample.usage);
            }
            DrugCategory category = DrugCategory.fromCode(prescExample.category);
            if( category != null ) {
                model.setCategory(category);
                if( category != DrugCategory.Gaiyou ) {
                    if( !constraints.isDaysFixed() || model.getDays().isEmpty() ) {
                        model.setDays("" + prescExample.days);
                    }
                }
            }
            model.setComment(prescExample.comment);
        });
    }

}
