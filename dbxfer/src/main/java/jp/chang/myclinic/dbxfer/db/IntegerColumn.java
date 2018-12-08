package jp.chang.myclinic.dbxfer.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class IntegerColumn extends Column<Integer> {

    //private static Logger logger = LoggerFactory.getLogger(IntegerColumn.class);

    public IntegerColumn(String name) {
        super(name);
    }

    @Override
    Integer getValueFromResultSet(ResultSet rs) {
        return rsValue(rs::getInt);
    }

    @Override
    void setParameter(PreparedStatement stmt, int index, Integer value){
        stmtPrep(() -> stmt.setInt(index, value));
    }
}
