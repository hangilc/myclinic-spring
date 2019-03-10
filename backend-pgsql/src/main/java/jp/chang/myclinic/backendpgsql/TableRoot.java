package jp.chang.myclinic.backendpgsql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public abstract class TableRoot<DTO> {

    abstract public String getTableName();

    abstract public List<String> getColumnNames();

    abstract public DTO toDTO(ResultSet rs) throws SQLException;

    abstract public void setForInsert(PreparedStatement stmt, DTO dto) throws SQLException;

    public String cols(String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return String.join(",", getColumnNames());
        } else {
            return getColumnNames().stream().map(c -> prefix + c).collect(Collectors.joining(","));
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
                cs.stream().map(c -> "?").collect(Collectors.joining(","))
        );
    }

    public void insert(Connection conn, DTO dto) {
        Query.exec(conn, sqlForInsert(), stmt -> setForInsert(stmt, dto));
    }

}
