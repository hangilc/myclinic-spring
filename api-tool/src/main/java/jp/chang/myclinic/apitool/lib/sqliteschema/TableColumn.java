package jp.chang.myclinic.apitool.lib.sqliteschema;

import jp.chang.myclinic.apitool.lib.Helper;
import jp.chang.myclinic.apitool.lib.gentablebase.Column;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TableColumn {

    private String name;
    private String type;
    private boolean isPrimary;
    private boolean isAutoInc;
    private String dtoFieldName;

    TableColumn(String name, String type, boolean isPrimary, boolean isAutoInc, String dtoFieldName){
        this.name = name;
        this.type = type;
        this.isPrimary = isPrimary;
        this.isAutoInc = isAutoInc;
        this.dtoFieldName = dtoFieldName;
    }

    String output(boolean printPrimary){
        String s = "  " + name + " " + type;
        if( isPrimary && printPrimary ){
            s += " primary key";
            if( isAutoInc ){
                s += " autoincrement";
            }
        }
        return s;
    }

    public String getName() {
        return name;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public boolean isAutoInc() {
        return isAutoInc;
    }

    public String getDtoFieldName() {
        return dtoFieldName;
    }
}
