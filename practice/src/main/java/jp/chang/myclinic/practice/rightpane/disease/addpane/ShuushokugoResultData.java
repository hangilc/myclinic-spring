package jp.chang.myclinic.practice.rightpane.disease.addpane;

import jp.chang.myclinic.dto.ShuushokugoMasterDTO;

class ShuushokugoResultData implements SearchResultData {

    private ShuushokugoMasterDTO master;

    ShuushokugoResultData(ShuushokugoMasterDTO master){
        this.master = master;
    }

    ShuushokugoMasterDTO getMaster(){
        return master;
    }

    @Override
    public String getRep() {
        return master.name;
    }
}
