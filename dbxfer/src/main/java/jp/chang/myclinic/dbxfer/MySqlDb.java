package jp.chang.myclinic.dbxfer;

import jp.chang.myclinic.dbxfer.db.Table;
import jp.chang.myclinic.dbxfer.mysql.*;
import jp.chang.myclinic.dbxfer.table.IyakuhinMasterEnum;
import jp.chang.myclinic.dbxfer.table.KizaiMasterEnum;
import jp.chang.myclinic.dbxfer.table.ShinryouMasterEnum;

class MySqlDb implements MyclinicDb {

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
