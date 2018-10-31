package jp.chang.myclinic.postgresql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.BiConsumer;
import java.util.function.Function;

abstract class ColumnBase<T, V> implements Column<T> {

    //private static Logger logger = LoggerFactory.getLogger(ColumnBase.class);
    private String name;
    private BiConsumer<T, V> dtoAssigner;
    private Function<T, V> dtoGetter;

    ColumnBase(String name, BiConsumer<T, V> dtoAssigner, Function<T, V> dtoGetter) {
        this.name = name;
        this.dtoAssigner = dtoAssigner;
        this.dtoGetter = dtoGetter;
    }

    abstract V getRowValue(ResultSet resultSet, int index) throws SQLException ;
    abstract void setQueryParam(PreparedStatement stmt, int index, V value) throws SQLException ;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setDTO(ResultSet resultSet, int index, T dto)  throws SQLException {
        V value = getRowValue(resultSet, index);
        dtoAssigner.accept(dto, value);
    }

    @Override
    public void setParameter(PreparedStatement stmt, int index, T dto)  throws SQLException {
        V value = dtoGetter.apply(dto);
        setQueryParam(stmt, index, value);
    }

}
