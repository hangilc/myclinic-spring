package jp.chang.myclinic.dbxfer.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract public class Table<E extends Enum<E>> {

    //private static Logger logger = LoggerFactory.getLogger(Table.class);

    private String name;
    private Map<E, Column> colMap = new HashMap<>();

    public Table(String name) {
        this.name = name;
    }

    public String getTableName(){
        return name;
    }

    public void setTableName(String name){
        this.name = name;
    }

    public void setColumn(E e, Column c){
        colMap.put(e, c);
    }

    public Column getColumn(E e){
        return colMap.get(e);
    }

    public List<Column> getColumns(){
        List<Column> cols = new ArrayList<>();
        for(E e: listColumnEnums()){
            Column c = getColumn(e);
            if( c != null ){
                cols.add(c);
            }
        }
        return cols;
    }

    abstract public E[] listColumnEnums();
}
