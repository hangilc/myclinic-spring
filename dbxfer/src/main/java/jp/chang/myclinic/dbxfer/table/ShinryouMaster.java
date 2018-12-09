package jp.chang.myclinic.dbxfer.table;

import jp.chang.myclinic.dbxfer.db.*;

import static jp.chang.myclinic.dbxfer.table.ShinryouMasterEnum.*;

public class ShinryouMaster extends Table<ShinryouMasterEnum>  {

    public ShinryouMaster() {
        super("shinryou_master");
        setColumn(SHINRYOUCODE, new IntegerColumn("shinryoucode"));
        setColumn(NAME, new StringColumn("name"));
        setColumn(TENSUU, new BigDecimalColumn("tensuu"));
        setColumn(TENSUU_SHIKIBETSU, new StringColumn("tensuu_shikibetsu"));
        setColumn(SHUUKEISAKI, new StringColumn("shuukeisaki"));
        setColumn(HOUKATSUKENSA, new StringColumn("houkatsukensa"));
        setColumn(KENSAGROUP, new StringColumn("kensagroup"));
        setColumn(VALID_FROM, new DateColumn("valid_from"));
        setColumn(VALID_UPTO, new DateColumn("valid_upto"));
    }

    @Override
    public ShinryouMasterEnum[] listColumnEnums() {
        return ShinryouMasterEnum.values();
    }

}
