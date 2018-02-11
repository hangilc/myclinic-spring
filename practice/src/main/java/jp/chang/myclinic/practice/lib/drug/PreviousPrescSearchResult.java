package jp.chang.myclinic.practice.lib.drug;

import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.practice.lib.PracticeLib;
import jp.chang.myclinic.util.DrugUtil;

public class PreviousPrescSearchResult implements DrugSearchResultModel {

    private DrugFullDTO drugFull;
    private String at;

    public PreviousPrescSearchResult(DrugFullDTO drug, String at){
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
            DrugFullDTO resolved = DrugFullDTO.copy(drugFull);
            resolved.master = master;
            resolved.drug = DrugDTO.copy(resolved.drug);
            resolved.drug.iyakuhincode = master.iyakuhincode;
            resolved.drug.drugId = 0;
            resolved.drug.visitId = 0;
            resolved.drug.prescribed = 0;
            DrugFormHelper.setDrug(target, resolved, getter, constraints);
        });
    }

}
