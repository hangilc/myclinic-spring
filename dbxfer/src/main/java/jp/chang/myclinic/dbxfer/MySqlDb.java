package jp.chang.myclinic.dbxfer;

import jp.chang.myclinic.dbxfer.db.Table;
import jp.chang.myclinic.dbxfer.mysql.IyakuhinMaster;

class MySqlDb implements MyclinicDb {

    //private static Logger logger = LoggerFactory.getLogger(MySqlDb.class);

    private IyakuhinMaster iyakuhinMaster = new IyakuhinMaster();
    @Override
    public Table getIyakuhinMaster(){
        return iyakuhinMaster;
    }

}
