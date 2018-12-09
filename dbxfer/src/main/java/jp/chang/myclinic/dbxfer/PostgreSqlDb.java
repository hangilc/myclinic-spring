package jp.chang.myclinic.dbxfer;

import jp.chang.myclinic.dbxfer.db.Table;
import jp.chang.myclinic.dbxfer.table.IyakuhinMaster;

class PostgreSqlDb implements MyclinicDb {

    private IyakuhinMaster iyakuhinMaster = new IyakuhinMaster();
    @Override
    public Table getIyakuhinMaster(){
        return iyakuhinMaster;
    }
}
