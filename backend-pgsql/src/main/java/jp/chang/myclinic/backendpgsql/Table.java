package jp.chang.myclinic.backendpgsql;

import java.sql.ResultSet;

public interface Table<DTO> {

    String getTableName();
    String cols(String prefix);
    DTO toDTO(ResultSet rs);

}
