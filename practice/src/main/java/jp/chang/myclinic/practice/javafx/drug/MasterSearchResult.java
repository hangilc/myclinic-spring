package jp.chang.myclinic.practice.javafx.drug;

import jp.chang.myclinic.dto.IyakuhinMasterDTO;

public class MasterSearchResult implements DrugSearchResultModel {

    private IyakuhinMasterDTO master;

    public MasterSearchResult(IyakuhinMasterDTO master){
        this.master = master;
    }

    @Override
    public String rep() {
        return master.name;
    }

    public IyakuhinMasterDTO getMaster(){
        return master;
    }

}
