package jp.chang.myclinic.dbxfer.table;

import jp.chang.myclinic.dbxfer.db.*;

import static jp.chang.myclinic.dbxfer.table.KizaiMasterEnum.*;

public class KizaiMaster extends Table<KizaiMasterEnum> {

    public KizaiMaster() {
        super("kizai_master");
        setColumn(KIZAICODE, new IntegerColumn("kizaicode"));
        setColumn(NAME, new StringColumn("name"));
        setColumn(YOMI, new StringColumn("yomi"));
        setColumn(UNIT, new StringColumn("unit"));
        setColumn(KINGAKU, new BigDecimalColumn("kingaku"));
        setColumn(VALID_FROM, new DateColumn("valid_from"));
        setColumn(VALID_UPTO, new DateColumn("valid_upto"));
    }

    @Override
    public KizaiMasterEnum[] listColumnEnums() {
        return KizaiMasterEnum.values();
    }
}
