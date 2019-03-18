package jp.chang.myclinic.backenddb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface TableInterface<DTO> {

    String getTableName();
    void insert(DTO dto);
    DTO getById(Object id);
    void update(DTO dto);
    void delete(Object id);
    DTO project(ResultSet rs, Query.ResultSetContext ctx) throws SQLException;

}
