package jp.chang.myclinic.practice.javafx.drug;

import jp.chang.myclinic.dto.IyakuhinMasterDTO;

class MasterSearchResult implements SearchResultModel {

    private IyakuhinMasterDTO master;

    MasterSearchResult(IyakuhinMasterDTO master){
        this.master = master;
    }

    @Override
    public String rep() {
        return master.name;
    }

    @Override
    public void stuffInto(DrugInputModel model, InputConstraints constraints) {
        DrugCommon.stuffMasterInto(master, model, constraints);
    }

}
