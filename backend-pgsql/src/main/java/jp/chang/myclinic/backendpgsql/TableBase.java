package jp.chang.myclinic.backendpgsql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static java.util.stream.Collectors.toList;

public abstract class TableBase<DTO> {

    abstract public String getTableName();

    abstract public List<String> getColumnNames();

    abstract public DTO toDTO(ResultSet rs) throws SQLException;

    abstract public void setForInsert(PreparedStatement stmt, DTO dto) throws SQLException;

    public String cols(String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return String.join(",", getColumnNames());
        } else {
            return String.join(",", getColumnNames().stream().map(c -> prefix + c).collect(toList()));
        }
    }

    public String cols() {
        return cols("");
    }

    String sqlForInsert() {
        List<String> cs = getColumnNames();

        return String.format("insert into %s (%s) values (%s)",
                getTableName(),
                String.join(",", cs),
                String.join(",", cs.stream().map(c -> "?").collect(toList()))
        );
    }

    public void insert(Connection conn, DTO dto) {
        Query.exec(conn, sqlForInsert(), stmt -> setForInsert(stmt, dto));
    }

}
