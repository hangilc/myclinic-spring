package jp.chang.myclinic.dbxfer.table;

import jp.chang.myclinic.dbxfer.db.IntegerColumn;
import jp.chang.myclinic.dbxfer.db.StringColumn;
import jp.chang.myclinic.dbxfer.db.Table;

import static jp.chang.myclinic.dbxfer.table.ShuushokugoMasterEnum.NAME;
import static jp.chang.myclinic.dbxfer.table.ShuushokugoMasterEnum.SHUUSHOKUGOCODE;

public class ShuushokugoMaster extends Table<ShuushokugoMasterEnum> {

    public ShuushokugoMaster() {
        super("shuushokugo_master");
        setColumn(SHUUSHOKUGOCODE, new IntegerColumn("shuushokugocode"));
        setColumn(NAME, new StringColumn("name"));
    }

    @Override
    public ShuushokugoMasterEnum[] listColumnEnums() {
        return ShuushokugoMasterEnum.values();
    }
}