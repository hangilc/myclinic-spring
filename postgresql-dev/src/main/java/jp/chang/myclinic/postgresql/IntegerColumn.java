package jp.chang.myclinic.postgresql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.BiConsumer;
import java.util.function.Function;

class IntegerColumn<T> extends ColumnBase<T, Integer> {

    private static Logger logger = LoggerFactory.getLogger(IntegerColumn.class);

    IntegerColumn(String name, BiConsumer<T, Integer> dtoAssigner, Function<T, Integer> dtoGetter) {
        super(name, dtoAssigner, dtoGetter);
    }

    @Override
    Integer getRowValue(ResultSet resultSet, int index) throws SQLException {
        return resultSet.getInt(index);
    }

    @Override
    void setQueryParam(PreparedStatement stmt, int index, Integer value)  throws SQLException {
        stmt.setInt(index, value);
    }
}
