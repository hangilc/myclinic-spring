package jp.chang.myclinic.dbxfer.mysql;

import jp.chang.myclinic.dbxfer.db.OldSqldateColumn;
import jp.chang.myclinic.dbxfer.db.SqldateColumn;

import static jp.chang.myclinic.dbxfer.table.ByoumeiMasterEnum.*;

public class ByoumeiMaster extends jp.chang.myclinic.dbxfer.table.ByoumeiMaster {

    public ByoumeiMaster() {
        setTableName("shoubyoumei_master_arch");
        setColumn(VALID_FROM, new SqldateColumn("valid_from"));
        setColumn(VALID_UPTO, new OldSqldateColumn("valid_upto"));
    }

}