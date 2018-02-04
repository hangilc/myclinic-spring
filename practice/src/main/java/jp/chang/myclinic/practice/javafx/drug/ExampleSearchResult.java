package jp.chang.myclinic.practice.javafx.drug;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.PrescExampleDTO;
import jp.chang.myclinic.dto.PrescExampleFullDTO;
import jp.chang.myclinic.practice.lib.PracticeLib;
import jp.chang.myclinic.util.PrescExampleUtil;

class ExampleSearchResult implements SearchResultModel {

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
    public void stuffInto(DrugInputModel model) {
        int origIyakuhincode = example.master.iyakuhincode;
        PracticeLib.resolveIyakuhinMaster(origIyakuhincode, at, master -> {
            DrugCommon.stuffMasterInto(master, model);
            PrescExampleDTO prescExample = example.prescExample;
            model.setAmount(prescExample.amount);
            model.setUsage(prescExample.usage);
            DrugCategory category = DrugCategory.fromCode(prescExample.category);
            if( category != null ) {
                model.setCategory(category);
                if( category != DrugCategory.Gaiyou ) {
                    model.setDays("" + prescExample.days);
                }
            }
            model.setComment(prescExample.comment);
        });
    }

}
