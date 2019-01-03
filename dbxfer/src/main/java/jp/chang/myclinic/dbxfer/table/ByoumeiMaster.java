package jp.chang.myclinic.dbxfer.table;

import jp.chang.myclinic.dbxfer.db.DateColumn;
import jp.chang.myclinic.dbxfer.db.IntegerColumn;
import jp.chang.myclinic.dbxfer.db.StringColumn;
import jp.chang.myclinic.dbxfer.db.Table;

import static jp.chang.myclinic.dbxfer.table.ByoumeiMasterEnum.*;

public class ByoumeiMaster extends Table<ByoumeiMasterEnum> {

    public ByoumeiMaster() {
        super("byoumei_master");
        setColumn(SHOUBYOUMEICODE, new IntegerColumn("shoubyoumeicode"));
        setColumn(NAME, new StringColumn("name"));
        setColumn(VALID_FROM, new DateColumn("valid_from"));
        setColumn(VALID_UPTO, new DateColumn("valid_upto"));
    }

    @Override
    public ByoumeiMasterEnum[] listColumnEnums() {
        return ByoumeiMasterEnum.values();
    }
}