package jp.chang.myclinic.dbxfer;

import jp.chang.myclinic.dbxfer.db.Table;
import jp.chang.myclinic.dbxfer.table.IyakuhinMasterEnum;

public interface MyclinicDb {
    Table<IyakuhinMasterEnum> getIyakuhinMaster();
}
