package jp.chang.myclinic.dbxfer.mysql;

import jp.chang.myclinic.dbxfer.db.OldSqldateColumn;
import jp.chang.myclinic.dbxfer.db.SqldateColumn;
import jp.chang.myclinic.dbxfer.db.StringColumn;

import static jp.chang.myclinic.dbxfer.table.ShinryouMasterEnum.*;

public class ShinryouMaster extends jp.chang.myclinic.dbxfer.table.ShinryouMaster {

    public ShinryouMaster() {
        setTableName("shinryoukoui_master_arch");
        setColumn(TENSUU, new StringColumn("tensuu"));
        setColumn(VALID_FROM, new SqldateColumn("valid_from"));
        setColumn(VALID_UPTO, new OldSqldateColumn("valid_upto"));
    }

}
