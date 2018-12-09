package jp.chang.myclinic.dbxfer;

import jp.chang.myclinic.dbxfer.db.Table;
import jp.chang.myclinic.dbxfer.table.*;

class PostgreSqlDb implements MyclinicDb {

    private IyakuhinMaster iyakuhinMaster = new IyakuhinMaster();
    @Override
    public Table<IyakuhinMasterEnum> getIyakuhinMaster(){
        return iyakuhinMaster;
    }

    private ShinryouMaster shinryouMaster = new ShinryouMaster();
    @Override
    public Table<ShinryouMasterEnum> getShinryouMaster(){
        return shinryouMaster;
    }

    private KizaiMaster kizaiMaster = new KizaiMaster();
    @Override
    public Table<KizaiMasterEnum> getKizaiMaster(){
        return kizaiMaster;
    }


}
