package jp.chang.myclinic.postgresqldev;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class IntColumn extends Column {

    //private static Logger logger = LoggerFactory.getLogger(IntColumn.class);

    IntColumn(String name) {
        super(name);
    }

    @Override
    void setParam(PreparedStatement stmt, int index, ResultSet rs) throws SQLException {
        stmt.setInt(index, rs.getInt(getName()));
    }

}
