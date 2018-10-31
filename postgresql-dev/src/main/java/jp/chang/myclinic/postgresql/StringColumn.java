package jp.chang.myclinic.postgresql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class StringColumn<T> extends ColumnBase<T, String> {

    private static Logger logger = LoggerFactory.getLogger(StringColumn.class);

    public StringColumn(String name, BiConsumer<T, String> dtoAssigner, Function<T, String> dtoGetter) {
        super(name, dtoAssigner, dtoGetter);
    }

    @Override
    String getRowValue(ResultSet resultSet, int index) throws SQLException {
        return resultSet.getString(index);
    }

    @Override
    void setQueryParam(PreparedStatement stmt, int index, String value)  throws SQLException {
        stmt.setString(index, value);
    }
}
