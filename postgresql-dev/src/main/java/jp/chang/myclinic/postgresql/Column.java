package jp.chang.myclinic.postgresql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Column<T> {

    String getName();
    void setDTO(ResultSet resultSet, int index, T dto) throws SQLException;
    void setParameter(PreparedStatement stmt, int index, T dto) throws SQLException;

}
