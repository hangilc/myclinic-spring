package jp.chang.myclinic.backendpgsql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

public class Column<DTO> {

    public interface ResultSetPutter<T> {
        void putIntoDTO(ResultSet source, T target) throws SQLException;
    }

    private final String name;
    private final boolean isPrimary;
    private final boolean isAutoIncrement;
    private final ResultSetPutter<DTO> putIntoDTO;
    private final Function<DTO, Object> getFromDTO;

    public Column(String name, boolean isPrimary, boolean isAutoIncrement,
                  ResultSetPutter<DTO> putIntoDTO, Function<DTO, Object> getFromDTO) {
        this.name = name;
        this.isPrimary = isPrimary;
        this.isAutoIncrement = isAutoIncrement;
        this.putIntoDTO = putIntoDTO;
        this.getFromDTO = getFromDTO;
    }

    public Column(String name, ResultSetPutter<DTO> putIntoDTO, Function<DTO, Object> getFromDTO){
        this(name, false, false, putIntoDTO, getFromDTO);
    }

    public String getName() {
        return name;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public boolean isAutoIncrement() {
        return isAutoIncrement;
    }

    public ResultSetPutter<DTO> putIntoDTO() {
        return putIntoDTO;
    }

    public Function<DTO, Object> getFromDTO() {
        return getFromDTO;
    }

}
