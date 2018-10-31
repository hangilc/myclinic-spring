package jp.chang.myclinic.postgresql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.BiConsumer;
import java.util.function.Function;

class DoubleColumn<T> extends ColumnBase<T, Double> {

    private static Logger logger = LoggerFactory.getLogger(DoubleColumn.class);

    DoubleColumn(String name, BiConsumer<T, Double> dtoAssigner, Function<T, Double> dtoGetter) {
        super(name, dtoAssigner, dtoGetter);
    }

    @Override
    Double getRowValue(ResultSet resultSet, int index) throws SQLException {
        return resultSet.getDouble(index);
    }

    @Override
    void setQueryParam(PreparedStatement stmt, int index, Double value) throws SQLException {
        stmt.setDouble(index, value);
    }
}
