package jp.chang.myclinic.backendpgsql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public abstract class Table<DTO> {

    abstract String getTableName();

    abstract List<String> getColumnNames();

    abstract DTO toDTO(ResultSet rs) throws SQLException;

    abstract void setForInsert(PreparedStatement stmt, DTO dto) throws SQLException;

    public String cols(String prefix){
        if( prefix == null || prefix.isEmpty() ){
            return String.join(",", getColumnNames());
        } else {
            return String.join(",", getColumnNames().stream().map(c -> prefix + c).collect(toList()));
        }
    }

    public String cols() {
        return cols("");
    }

    public String getPrimaryKey() {
        return null;
    }

    public  DTO getById(Connection conn, int id) {
        String primaryKey = getPrimaryKey();
        if (primaryKey == null) {
            throw new RuntimeException("Cannot find primary key.");
        }
        String sql = String.format("select %s from %s where %s = ?", cols(), getTableName(),
                getPrimaryKey());
        return Query.get(conn, sql, stmt -> stmt.setInt(1, id), this::toDTO);
    }

    private List<String> copyListExcept(List<String> list, String item){
        List<String> copy = new ArrayList<>(list);
        copy.remove(item);
        return Collections.unmodifiableList(copy);
    }

    private String sqlForInsert(){
        List<String> cs = copyListExcept(getColumnNames(), getPrimaryKey());

        return String.format("insert into %s (%s) values (%s) returning %s", getTableName(),
                    String.join(",", cs),
                    String.join(",", cs.stream().map(c -> "?").collect(toList())),
                getPrimaryKey()
        );
    }

    public int insert(Connection conn, DTO dto){
        return Query.get(conn, sqlForInsert(), stmt -> setForInsert(stmt, dto),  rs -> rs.getInt(1));
    }

}
