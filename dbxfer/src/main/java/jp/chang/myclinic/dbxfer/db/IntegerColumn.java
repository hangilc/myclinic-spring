package jp.chang.myclinic.dbxfer.db;

public class IntegerColumn extends Column {

    //private static Logger logger = LoggerFactory.getLogger(IntegerColumn.class);

    public IntegerColumn(String name) {
        super(name, Integer.class, Integer.class);
    }

}
