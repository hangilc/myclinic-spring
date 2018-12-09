package jp.chang.myclinic.dbxfer.mysql;

import jp.chang.myclinic.dbxfer.db.OldSqldateColumn;
import jp.chang.myclinic.dbxfer.db.SqldateColumn;
import jp.chang.myclinic.dbxfer.db.StringColumn;
import static jp.chang.myclinic.dbxfer.table.IyakuhinMasterEnum.*;

public class IyakuhinMaster extends jp.chang.myclinic.dbxfer.table.IyakuhinMaster {

    public IyakuhinMaster() {
        setTableName("iyakuhin_master_arch");
        setColumn(YAKKA, new StringColumn("yakka"));
        setColumn(VALID_FROM, new SqldateColumn("valid_from"));
        setColumn(VALID_UPTO, new OldSqldateColumn("valid_upto"));
    }

}
