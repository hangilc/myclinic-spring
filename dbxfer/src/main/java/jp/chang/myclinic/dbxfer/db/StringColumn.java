package jp.chang.myclinic.dbxfer.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StringColumn extends Column<String>{

    //private static Logger logger = LoggerFactory.getLogger(StringColumn.class);

    public StringColumn(String name) {
        super(name);
    }

    @Override
    String getValueFromResultSet(ResultSet rs) {
        return rsValue(rs::getString);
    }

    @Override
    void setParameter(PreparedStatement stmt, int index, String value) {
        stmtPrep(() -> stmt.setString(index, value));
    }
}
