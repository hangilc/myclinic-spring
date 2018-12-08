package jp.chang.myclinic.dbxfer.db;

public class Table {

    //private static Logger logger = LoggerFactory.getLogger(Table.class);

    private String name;

    public Table(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

}
