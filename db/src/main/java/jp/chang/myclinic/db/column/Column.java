package jp.chang.myclinic.db.column;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface Column<T> {
    String getName();
    T rsValueByName(ResultSet rs);
    T rsValueByIndex(ResultSet rs, int index);
    void setParameter(PreparedStatement stmt, int index, T value);
}
