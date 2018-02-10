package jp.chang.myclinic.practice.lib.drug;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.practice.lib.PracticeLib;
import jp.chang.myclinic.util.DrugUtil;

import java.text.DecimalFormat;

public class PreviousPrescSearchResult extends MasterSearchResult {

    private DrugFullDTO drugFull;
    private String at;
    private DecimalFormat decimalFormatter = new DecimalFormat("###.##");

    public PreviousPrescSearchResult(DrugFullDTO drug, String at){
        super(drug.master);
        this.drugFull = drug;
        this.at = at;
    }

    @Override
    public String rep() {
        return DrugUtil.drugRep(drugFull);
    }

    @Override
    public void stuffInto(DrugFormSetter target, DrugFormGetter getter, DrugInputConstraints constraints) {
        int origIyakuhincode = drugFull.master.iyakuhincode;
        PracticeLib.resolveIyakuhinMaster(origIyakuhincode, at, master -> {
            super.stuffInto(target, getter, constraints);
            DrugDTO drug = drugFull.drug;
            if( !constraints.isAmountFixed() || getter.getAmount().isEmpty() ) {
                target.setAmount(decimalFormatter.format(drug.amount));
            }
            if( !constraints.isUsageFixed() || getter.getUsage().isEmpty() ) {
                target.setUsage(drug.usage);
            }
            DrugCategory category = DrugCategory.fromCode(drug.category);
            if( category != null ) {
                target.setCategory(category);
                if( category != DrugCategory.Gaiyou ) {
                    if( !constraints.isDaysFixed() || getter.getDays().isEmpty() ) {
                        target.setDays("" + drug.days);
                    }
                }
            }
            target.setComment("");
        });
    }

}
