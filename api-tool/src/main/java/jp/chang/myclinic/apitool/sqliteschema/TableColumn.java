package jp.chang.myclinic.apitool.sqliteschema;

import jp.chang.myclinic.apitool.lib.Helper;
import jp.chang.myclinic.apitool.lib.gentablebase.Column;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TableColumn {

    private String name;
    private String type;
    private boolean isPrimary;
    private boolean isAutoinc;
    private Helper helper = Helper.getInstance();

    TableColumn(Column column, boolean printPrimary){
        this.name = helper.toSnake(column.getDtoField());
        this.type = toType(column.getDbType());
        if( printPrimary ){
            this.isPrimary = column.isPrimary();
            this.isAutoinc = column.isAutoIncrement();
        }
    }

    String output(){
        String s = "  " + name + " " + type;
        if( isPrimary ){
            s += " primary key";
        }
        if( isAutoinc ){
            s += " autoincrement";
        }
        return s;
    }

    private String toType(Class<?> cls){
        if( cls == Integer.class ){
            return "integer";
        } else if( cls == Double.class ){
            return "real";
        } else {
            return "text";
        }
    }

}
