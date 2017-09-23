package jp.chang.myclinic.practice.rightpane.disease.addpane;

import jp.chang.myclinic.dto.ByoumeiMasterDTO;

class ByoumeiResultData implements SearchResultData {

    private ByoumeiMasterDTO master;

    ByoumeiResultData(ByoumeiMasterDTO master){
        this.master = master;
    }

    ByoumeiMasterDTO getMaster(){
        return master;
    }

    @Override
    public String getRep() {
        return master.name;
    }
}
