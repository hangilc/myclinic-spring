package jp.chang.myclinic.dbxfer;

import jp.chang.myclinic.dbxfer.db.Table;
import jp.chang.myclinic.dbxfer.table.*;

public class MyclinicDb {
    Table<IyakuhinMasterEnum> iyakuhinMaster;
    public Table<IyakuhinMasterEnum> getIyakuhinMaster(){
        return iyakuhinMaster;
    }

    Table<ShinryouMasterEnum> shinryouMaster;
    public Table<ShinryouMasterEnum> getShinryouMaster(){
        return shinryouMaster;
    }

    Table<KizaiMasterEnum> kizaiMaster;
    public Table<KizaiMasterEnum> getKizaiMaster(){
        return kizaiMaster;
    }

    Table<ByoumeiMasterEnum> byoumeiMaster;
    public Table<ByoumeiMasterEnum> getByoumeiMaster(){
        return byoumeiMaster;
    }

    Table<ShuushokugoMasterEnum> shuushokugoMaster = new ShuushokugoMaster();
    public Table<ShuushokugoMasterEnum> getShuushokugoMaster(){
        return shuushokugoMaster;
    }

    Table<PatientEnum> patient = new Patient();
    public Table<PatientEnum> getPatient(){
        return patient;
    }

}
