package jp.chang.myclinic.practice.lib.drug;

import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.practice.lib.PracticeUtil;

public class MasterSearchResult implements DrugSearchResultModel {

    private IyakuhinMasterDTO master;

    public MasterSearchResult(IyakuhinMasterDTO master){
        this.master = master;
    }

    @Override
    public String rep() {
        return master.name;
    }

    @Override
    public void stuffInto(DrugFormSetter setter, DrugFormGetter getter, DrugInputConstraints constraints) {
        setter.setIyakuhincode(master.iyakuhincode);
        setter.setDrugName(master.name);
        if( !constraints.isAmountFixed() ) {
            setter.setAmount("");
        }
        setter.setAmountUnit(master.unit);
        if( !constraints.isUsageFixed() ) {
            setter.setUsage("");
        }
        if( !constraints.isDaysFixed() ) {
            setter.setDays("");
        }
        setter.setCategory(PracticeUtil.zaikeiToCategory(master.zaikei));
        setter.setComment("");
    }

}
