package jp.chang.myclinic.dbxfer.postgresql;

import jp.chang.myclinic.dbxfer.db.*;

public class IyakuhinMaster extends Table {

    //private static Logger logger = LoggerFactory.getLogger(IyakuhinMaster.class);

    public IntegerColumn iyakuhincode = new IntegerColumn("iyakuhincode");
    public StringColumn name = new StringColumn("name");
    public StringColumn yomi = new StringColumn("yomi");
    public StringColumn unit = new StringColumn("unit");
    public BigDecimalColumn yakka = new BigDecimalColumn("yakka");
    public StringColumn madoku = new StringColumn("madoku");
    public StringColumn kouhatsu = new StringColumn("kouhatsu");
    public StringColumn zaikei = new StringColumn("zaikei");
    public DateColumn validFrom = new DateColumn("valid_from");
    public DateColumn validUpto = new DateColumn("valid_upto");

    public IyakuhinMaster() {
        super("iyakuhin_master");
    }

}
