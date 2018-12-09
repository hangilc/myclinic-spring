package jp.chang.myclinic.dbxfer.mysql;

import jp.chang.myclinic.dbxfer.db.OldSqldateColumn;
import jp.chang.myclinic.dbxfer.db.SqldateColumn;
import jp.chang.myclinic.dbxfer.db.StringColumn;

import static jp.chang.myclinic.dbxfer.table.KizaiMasterEnum.*;

public class KizaiMaster extends jp.chang.myclinic.dbxfer.table.KizaiMaster {

    public KizaiMaster() {
        setTableName("tokuteikizai_master_arch");
        setColumn(KINGAKU, new StringColumn("kingaku"));
        setColumn(VALID_FROM, new SqldateColumn("valid_from"));
        setColumn(VALID_UPTO, new OldSqldateColumn("valid_upto"));
    }

}
