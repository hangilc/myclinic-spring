package jp.chang.myclinic.dbxfer.db;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class DateColumn extends Column<LocalDate> {

    //private static Logger logger = LoggerFactory.getLogger(DateColumn.class);

    public DateColumn(String name) {
        super(name);
    }

    @Override
    LocalDate getValueFromResultSet(ResultSet rs) {
        return rsValue(name -> rs.getObject(name, LocalDate.class));
    }

    @Override
    void setParameter(PreparedStatement stmt, int index, LocalDate value) {
        stmtPrep(() -> stmt.setDate(index, Date.valueOf(value)));
    }
}
