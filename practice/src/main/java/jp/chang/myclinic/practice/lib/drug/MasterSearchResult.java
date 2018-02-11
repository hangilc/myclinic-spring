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
        DrugFormHelper.setMaster(setter, master, getter, constraints);
    }

}
