package jp.chang.myclinic.backenddb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface TableInterface<DTO> {

    String getTableName();
    void insert(DTO dto);
    void batchCopy(List<DTO> items);
    DTO getById(Object id);
    DTO getByIdForUpdate(Object id, String suffix);
    void update(DTO dto);
    int delete(Object id);
    DTO project(ResultSet rs, Query.ResultSetContext ctx) throws SQLException;

}
