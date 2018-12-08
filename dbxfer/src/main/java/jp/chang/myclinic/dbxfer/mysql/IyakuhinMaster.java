package jp.chang.myclinic.dbxfer.mysql;

import jp.chang.myclinic.dbxfer.db.IntegerColumn;
import jp.chang.myclinic.dbxfer.db.StringColumn;
import jp.chang.myclinic.dbxfer.db.Table;

public class IyakuhinMaster extends Table {

    //private static Logger logger = LoggerFactory.getLogger(IyakuhinMaster.class);

    public IntegerColumn iyakuhincode = new IntegerColumn("iyakuhincode");
    public StringColumn name = new StringColumn("name");
    public StringColumn yomi = new StringColumn("yomi");
    public StringColumn unit = new StringColumn("unit");
    public StringColumn yakka = new StringColumn("yakka");
    public StringColumn madoku = new StringColumn("madoku");
    public StringColumn kouhatsu = new StringColumn("kouhatsu");
    public StringColumn zaikei = new StringColumn("zaikei");
    public StringColumn validFrom = new StringColumn("valid_from");
    public StringColumn validUpto = new StringColumn("valid_upto");

    public IyakuhinMaster() {
        super("iyakuhin_master_arch");
    }

}
