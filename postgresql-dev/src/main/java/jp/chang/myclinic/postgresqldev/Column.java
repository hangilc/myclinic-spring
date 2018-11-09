package jp.chang.myclinic.postgresqldev;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

abstract class Column {

    //private static Logger logger = LoggerFactory.getLogger(Column.class);
    private String name;

    Column(String name) {
        this.name = name;
    }

    String getName(){
        return name;
    }

    abstract void setParam(PreparedStatement stmt, int index, ResultSet rs) throws SQLException;

}
