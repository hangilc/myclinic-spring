package jp.chang.myclinic.postgresql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.BiConsumer;
import java.util.function.Function;

class CharacterColumn<T> extends ColumnBase<T, Character> {

    private static Logger logger = LoggerFactory.getLogger(CharacterColumn.class);

    CharacterColumn(String name, BiConsumer<T, Character> dtoAssigner, Function<T, Character> dtoGetter) {
        super(name, dtoAssigner, dtoGetter);
    }

    @Override
    Character getRowValue(ResultSet resultSet, int index) throws SQLException {
        String s = resultSet.getString(index);
        return s.charAt(0);
    }

    @Override
    void setQueryParam(PreparedStatement stmt, int index, Character value) throws SQLException {
        String s = String.format("%c", value);
        stmt.setString(index, s);
    }

}
