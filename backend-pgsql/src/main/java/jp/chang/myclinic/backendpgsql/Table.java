package jp.chang.myclinic.backendpgsql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public interface Table<DTO> {

    String getTableName();

    List<String> getColumnNames();

    default String cols(String prefix){
        if( prefix == null || prefix.isEmpty() ){
            return String.join(",", getColumnNames());
        } else {
            return String.join(",", getColumnNames().stream().map(c -> prefix + c).collect(toList()));
        }
    }

    default String cols() {
        return cols("");
    }

    default String getPrimaryKey() {
        return null;
    }

    DTO toDTO(ResultSet rs);

    void setForInsert(PreparedStatement stmt, DTO dto) throws SQLException;

    default DTO getById(int id) {
        String primaryKey = getPrimaryKey();
        if (primaryKey == null) {
            throw new RuntimeException("Cannot find primary key.");
        }
        String sql = String.format("select %s from %s where %s = ?", cols(), getTableName(),
                getPrimaryKey());
        return DB.get(sql, stmt -> stmt.setInt(1, id), this::toDTO);
    }

    default List<String> copyListExcept(List<String> list, String item){
        List<String> copy = new ArrayList<>(list);
        copy.remove(item);
        return Collections.unmodifiableList(copy);
    }

    default String sqlForInsert(){
        List<String> cs = copyListExcept(getColumnNames(), getPrimaryKey());

        return String.format("insert into %s (%s) values (%s) returning %s", getTableName(),
                    String.join(",", cs),
                    String.join(",", cs.stream().map(c -> "?").collect(toList())),
                getPrimaryKey()
        );
    }

    default int insert(DTO dto){
        return DB.get(sqlForInsert(), stmt -> setForInsert(stmt, dto),  rs -> rs.getInt(1));
    }

}
