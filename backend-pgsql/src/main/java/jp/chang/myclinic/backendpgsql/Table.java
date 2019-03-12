package jp.chang.myclinic.backendpgsql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;
import static jp.chang.myclinic.backendpgsql.Query.SqlConsumer;
import static jp.chang.myclinic.backendpgsql.Query.SqlMapper;

public abstract class Table<DTO> {

    private final Connection conn;

    public Table(Connection conn) {
        this.conn = conn;
    }

    protected abstract String getTableName();

    protected abstract Class<DTO> getClassDTO();

    protected abstract List<Column<DTO>> getColumns();

    public DTO newInstanceDTO() {
        try {
            return getClassDTO().getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected Connection getConnection() {
        return conn;
    }

    public void insert(DTO dto) {
        Map<Boolean, List<Column<DTO>>> colmap = getColumns().stream().collect(groupingBy(Column::isAutoIncrement));
        if (colmap.get(true).size() == 0) {
            String sql = String.format("insert into %s (%s) values (%s)",
                    getTableName(),
                    colmap.get(false).stream().map(Column::getName).collect(joining(",")),
                    colmap.get(false).stream().map(c -> "?").collect(joining(","))
            );
            SqlConsumer<PreparedStatement> setter = stmt -> {
                int i = 1;
                for (Column<DTO> c : colmap.get(false)) {
                    stmt.setObject(i++, c.getFromDTO().apply(dto));
                }
            };
            Query.exec(conn, sql, setter);
        } else {
            String sql = String.format("insert into %s (%s) values (%s) returning %s",
                    getTableName(),
                    colmap.get(false).stream().map(Column::getName).collect(joining(",")),
                    colmap.get(false).stream().map(c -> "?").collect(joining(",")),
                    colmap.get(true).stream().map(Column::getName).collect(joining(","))
            );
            SqlConsumer<PreparedStatement> setter = stmt -> {
                int i = 1;
                for (Column<DTO> c : colmap.get(false)) {
                    stmt.setObject(i++, c.getFromDTO().apply(dto));
                }
            };
            SqlMapper<DTO> mapper = rs -> {
                List<Column<DTO>> autoCols = colmap.get(true);
                for (int i = 0; i < autoCols.size(); i++) {
                    Object o = rs.getObject(i + 1);
                    autoCols.get(i).putIntoDTO().putIntoDTO(rs, dto);
                }
                return null;
            };
            Query.get(conn, sql, setter, mapper);
        }
    }

    public DTO getById(Object id) {
        List<Column<DTO>> primaries = getColumns().stream().filter(Column::isPrimary).collect(toList());
        if (primaries.size() != 1) {
            throw new RuntimeException("Not table with single primary key.");
        }
        Column<DTO> primary = primaries.get(0);
        String sql = String.format("select %s from %s where %s = ?",
                getColumns().stream().map(Column::getName).collect(joining(",")),
                getTableName(),
                primary.getName()
        );
        SqlConsumer<PreparedStatement> setter = stmt -> {
            stmt.setObject(1, id);
        };
        SqlMapper<DTO> mapper = rs -> {
            DTO result = newInstanceDTO();
            for (int i = 0; i < getColumns().size(); i++) {
                Column<DTO> c = getColumns().get(i);
                Object o = rs.getObject(i + 1);
                c.putIntoDTO().putIntoDTO(rs, result);
            }
            return result;
        };
        return Query.get(conn, sql, setter, mapper);
    }

    public void update(DTO dto) {
        Map<Boolean, List<Column<DTO>>> colmap = getColumns().stream().collect(groupingBy(Column::isPrimary));
        List<Column<DTO>> primaries = colmap.get(true);
        List<Column<DTO>> nonPrimaries = colmap.get(false);
        if (primaries.size() == 0) {
            throw new RuntimeException("No primary keys.");
        }
        String sql = String.format("update %s set %s where %s",
                getTableName(),
                nonPrimaries.stream().map(c -> c.getName() + "=?").collect(joining(",")),
                primaries.stream().map(c -> c.getName() + "=?").collect(joining(" and "))
        );
        SqlConsumer<PreparedStatement> setter = stmt -> {
            int index = 1;
            for (Column<DTO> c : nonPrimaries) {
                Object o = c.getFromDTO().apply(dto);
                stmt.setObject(index++, o);
            }
            for (Column<DTO> c : primaries) {
                Object o = c.getFromDTO().apply(dto);
                stmt.setObject(index++, o);
            }
        };
        Query.exec(conn, sql, setter);
    }

    public void delete(Object id) {
        List<Column<DTO>> primaries = getColumns().stream().filter(Column::isPrimary).collect(toList());
        if (primaries.size() != 1) {
            throw new RuntimeException("Not table with single primary key.");
        }
        Column<DTO> primary = primaries.get(0);
        String sql = String.format("delete from %s where %s = ?",
                getTableName(),
                primary.getName()
        );
        SqlConsumer<PreparedStatement> setter = stmt -> {
            stmt.setObject(1, id);
        };
        Query.exec(conn, sql, setter);
    }

    private String colsCache;

    public String cols() {
        if( colsCache == null ){
            this.colsCache = getColumns().stream().map(Column::getName).collect(joining(","));
        }
        return colsCache;
    }

    public DTO mapper(ResultSet rs) throws SQLException {
        DTO result = newInstanceDTO();
        for (Column<DTO> c : getColumns()) {
            Object o = rs.getObject(c.getName());
            c.putIntoDTO().putIntoDTO(rs, result);
        }
        return result;
    }

    public List<DTO> selectFromTable(String sqlWhere, SqlConsumer<PreparedStatement> setter) {
        return Query.select(getConnection(), "select " + cols() + " " + sqlWhere,
                setter, this::mapper);
    }

    public String cols(String prefix){
        return getColumns().stream()
                .map(c -> String.format("%s.%s as %s_%s", prefix, c.getName(), prefix, c.getName()))
                .collect(joining(","));
    }

    public SqlMapper<DTO> makeMapper(String prefix){
        return rs -> {
            DTO result = newInstanceDTO();
            for (Column<DTO> c : getColumns()) {
                Object o = rs.getObject(prefix + "_" + c.getName());
                c.putIntoDTO().putIntoDTO(rs, result);
            }
            return result;
        };
    }

}
