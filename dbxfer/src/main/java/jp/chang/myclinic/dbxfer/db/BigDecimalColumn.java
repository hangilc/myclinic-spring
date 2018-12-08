package jp.chang.myclinic.dbxfer.db;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BigDecimalColumn extends Column<BigDecimal>{

    //private static Logger logger = LoggerFactory.getLogger(BigDecimalColumn.class);

    public BigDecimalColumn(String name) {
        super(name);
    }

    @Override
    BigDecimal getValueFromResultSet(ResultSet rs) {
        return rsValue(rs::getBigDecimal);
    }

    @Override
    void setParameter(PreparedStatement stmt, int index, BigDecimal value) {
        stmtPrep(stmt::setBigDecimal, index, value);
    }

}
