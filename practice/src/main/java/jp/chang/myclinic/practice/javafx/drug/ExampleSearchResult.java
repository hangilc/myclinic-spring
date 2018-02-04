package jp.chang.myclinic.practice.javafx.drug;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.PrescExampleDTO;
import jp.chang.myclinic.dto.PrescExampleFullDTO;
import jp.chang.myclinic.util.PrescExampleUtil;

class ExampleSearchResult implements SearchResultModel {

    private PrescExampleFullDTO example;

    ExampleSearchResult(PrescExampleFullDTO example){
        this.example = example;
    }

    @Override
    public String rep() {
        return PrescExampleUtil.rep(example);
    }

    @Override
    public void stuffInto(DrugInputModel model) {
        DrugCommon.stuffMasterInto(example.master, model);
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
    }

}
