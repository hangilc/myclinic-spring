package jp.chang.myclinic.backendpgsql;

import java.sql.ResultSet;

public interface Mapper<T> {
    T toDTO(ResultSet rs);
}
