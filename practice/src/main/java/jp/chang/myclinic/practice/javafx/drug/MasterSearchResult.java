package jp.chang.myclinic.practice.javafx.drug;

import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.practice.lib.DrugInputConstraints;
import jp.chang.myclinic.practice.lib.DrugSearchResultModel;

class MasterSearchResult implements DrugSearchResultModel {

    private IyakuhinMasterDTO master;

    MasterSearchResult(IyakuhinMasterDTO master){
        this.master = master;
    }

    @Override
    public String rep() {
        return master.name;
    }

    @Override
    public void stuffInto(DrugInputModel model, DrugInputConstraints constraints) {
        DrugCommon.stuffMasterInto(master, model, constraints);
    }

}
