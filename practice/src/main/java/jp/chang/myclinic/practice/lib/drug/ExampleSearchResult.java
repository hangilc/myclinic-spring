package jp.chang.myclinic.practice.lib.drug;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.PrescExampleDTO;
import jp.chang.myclinic.dto.PrescExampleFullDTO;
import jp.chang.myclinic.practice.javafx.drug.DrugCommon;
import jp.chang.myclinic.practice.javafx.drug.DrugInputModel;
import jp.chang.myclinic.practice.lib.drug.DrugInputConstraints;
import jp.chang.myclinic.practice.lib.PracticeLib;
import jp.chang.myclinic.practice.lib.drug.DrugSearchResultModel;
import jp.chang.myclinic.util.PrescExampleUtil;

public class ExampleSearchResult extends MasterSearchResult {

    private PrescExampleFullDTO example;
    private String at;

    public ExampleSearchResult(PrescExampleFullDTO example, String at){
        super(example.master);
        this.example = example;
        this.at = at;
    }

    @Override
    public String rep() {
        return PrescExampleUtil.rep(example);
    }

    @Override
    public void stuffInto(DrugFormSetter target, DrugFormGetter getter, DrugInputConstraints constraints) {
        int origIyakuhincode = example.master.iyakuhincode;
        PracticeLib.resolveIyakuhinMaster(origIyakuhincode, at, master -> {
            super.stuffInto(target, getter, constraints);
            PrescExampleDTO prescExample = example.prescExample;
            if( !constraints.isAmountFixed() || getter.getAmount().isEmpty() ) {
                target.setAmount(prescExample.amount);
            }
            if( !constraints.isUsageFixed() || getter.getUsage().isEmpty() ) {
                target.setUsage(prescExample.usage);
            }
            DrugCategory category = DrugCategory.fromCode(prescExample.category);
            if( category != null ) {
                target.setCategory(category);
                if( category != DrugCategory.Gaiyou ) {
                    if( !constraints.isDaysFixed() || getter.getDays().isEmpty() ) {
                        target.setDays("" + prescExample.days);
                    }
                }
            }
            target.setComment(prescExample.comment);
        });
    }

}
