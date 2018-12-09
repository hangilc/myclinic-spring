package jp.chang.myclinic.dbxfer.table;

import jp.chang.myclinic.dbxfer.db.*;
import static jp.chang.myclinic.dbxfer.table.IyakuhinMasterEnum.*;

public class IyakuhinMaster extends Table<IyakuhinMasterEnum> {

    public IyakuhinMaster() {
        super("iyakuhin_master");
        setColumn(IYAKUHINCODE, new IntegerColumn("iyakuhincode"));
        setColumn(NAME, new StringColumn("name"));
        setColumn(YOMI, new StringColumn("yomi"));
        setColumn(UNIT, new StringColumn("unit"));
        setColumn(YAKKA, new BigDecimalColumn("yakka"));
        setColumn(MADOKU, new StringColumn("madoku"));
        setColumn(KOUHATSU, new StringColumn("kouhatsu"));
        setColumn(ZAIKEI, new StringColumn("zaikei"));
        setColumn(VALID_FROM, new DateColumn("valid_from"));
        setColumn(VALID_UPTO, new DateColumn("valid_upto"));
    }

    @Override
    public IyakuhinMasterEnum[] listColumnEnums() {
        return IyakuhinMasterEnum.values();
    }

}
