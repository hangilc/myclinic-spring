package jp.chang.myclinic.dbxfer;

import jp.chang.myclinic.dbxfer.db.Table;
import jp.chang.myclinic.dbxfer.table.IyakuhinMasterEnum;
import jp.chang.myclinic.dbxfer.table.KizaiMasterEnum;
import jp.chang.myclinic.dbxfer.table.ShinryouMasterEnum;

public interface MyclinicDb {
    Table<IyakuhinMasterEnum> getIyakuhinMaster();
    Table<ShinryouMasterEnum> getShinryouMaster();
    Table<KizaiMasterEnum> getKizaiMaster();
}
