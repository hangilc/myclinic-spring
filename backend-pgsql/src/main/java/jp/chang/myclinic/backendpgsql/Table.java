package jp.chang.myclinic.backendpgsql;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public abstract class Table<DTO> extends TableBase<DTO> {

    public abstract String getPrimaryKey();

    public abstract int extractPrimaryKeyFrom(DTO dto);

    public abstract void putPrimaryKeyInto(DTO dto, int primaryKey);

    public DTO getById(Connection conn, int id) {
        String primaryKey = getPrimaryKey();
        if (primaryKey == null) {
            throw new RuntimeException("Cannot find primary key.");
        }
        String sql = String.format("select %s from %s where %s = ?", cols(), getTableName(),
                getPrimaryKey());
        return Query.get(conn, sql, stmt -> stmt.setInt(1, id), this::toDTO);
    }

    private List<String> copyListExcept(List<String> list, String item) {
        List<String> copy = new ArrayList<>(list);
        copy.remove(item);
        return Collections.unmodifiableList(copy);
    }

    @Override
    String sqlForInsert() {
        List<String> cs = copyListExcept(getColumnNames(), getPrimaryKey());

        return String.format("insert into %s (%s) values (%s) returning %s", getTableName(),
                String.join(",", cs),
                String.join(",", cs.stream().map(c -> "?").collect(toList())),
                getPrimaryKey()
        );
    }

    @Override
    public void insert(Connection conn, DTO dto) {
        int i = Query.get(conn, sqlForInsert(), stmt -> setForInsert(stmt, dto), rs -> rs.getInt(1));
        putPrimaryKeyInto(dto, i);
    }

    private String sqlForUpdate() {
        List<String> cs = copyListExcept(getColumnNames(), getPrimaryKey());

        return String.format("update %s set %s where %s = ?", getTableName(),
                cs.stream().map(c -> c + " = ?").collect(joining(",")),
                getPrimaryKey());
    }

    public void update(Connection conn, DTO dto) {
        Query.exec(conn, sqlForUpdate(), stmt -> {
            setForInsert(stmt, dto);
            stmt.setInt(getColumnNames().size(), extractPrimaryKeyFrom(dto));
        });
    }

    private String sqlForDelete(){
        return String.format("delete from %s where %s = ?", getTableName(), getPrimaryKey());
    }

    public void delete(Connection conn, int primaryKey){
        Query.exec(conn, sqlForDelete(), stmt -> stmt.setInt(1, primaryKey));
    }

}
