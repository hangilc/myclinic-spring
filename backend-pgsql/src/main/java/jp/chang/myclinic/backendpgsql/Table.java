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

    private static final ThreadLocal<Connection> threadLocalConnection = new ThreadLocal<>();

    public static void setConnection(Connection conn){
        threadLocalConnection.set(conn);
    }

    public static Connection getConnection(){
        return threadLocalConnection.get();
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
                    c.getFromDTO().set(stmt, i++, dto);
                }
            };
            Query.exec(getConnection(), sql, setter);
        } else {
//            String sql = String.format("insert into %s (%s) values (%s) returning %s",
//                    getTableName(),
//                    colmap.get(false).stream().map(Column::getName).collect(joining(",")),
//                    colmap.get(false).stream().map(c -> "?").collect(joining(",")),
//                    colmap.get(true).stream().map(Column::getName).collect(joining(","))
//            );
//            SqlConsumer<PreparedStatement> setter = stmt -> {
//                int i = 1;
//                for (Column<DTO> c : colmap.get(false)) {
//                    stmt.setObject(i++, c.getFromDTO().apply(dto));
//                }
//            };
//            SqlMapper<DTO> mapper = rs -> {
//                List<Column<DTO>> autoCols = colmap.get(true);
//                for (int i = 0; i < autoCols.size(); i++) {
//                    Object o = rs.getObject(i + 1);
//                    autoCols.get(i).getFromResultSet().getFromResultSet(rs, dto);
//                }
//                return null;
//            };
//            Query.get(getConnection(), sql, setter, mapper);
            String sql = String.format("insert into %s (%s) values (%s)",
                    getTableName(),
                    colmap.get(false).stream().map(Column::getName).collect(joining(",")),
                    colmap.get(false).stream().map(c -> "?").collect(joining(","))
            );
            List<Column<DTO>> autoIncs = colmap.get(true);
            SqlConsumer<PreparedStatement> setter = stmt -> {
                int i = 1;
                for (Column<DTO> c : colmap.get(false)) {
                    c.getFromDTO().set(stmt, i++, dto);
                }
            };
            SqlConsumer<ResultSet> mapper = rs -> {
                for(Column<DTO> c: autoIncs){
                    c.putIntoDTO().getFromResultSet(rs, dto);
                }
            };
            int n = Query.update(getConnection(), sql, PreparedStatement.RETURN_GENERATED_KEYS,
                    setter, mapper);
            if( n != 1 ){
                throw new RuntimeException("insert affected non-signle row: " + n);
            }
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
                c.putIntoDTO().getFromResultSet(rs, result);
            }
            return result;
        };
        return Query.get(getConnection(), sql, setter, mapper);
    }

    public int update(DTO dto) {
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
                c.getFromDTO().set(stmt, index++, dto);
            }
            for (Column<DTO> c : primaries) {
                c.getFromDTO().set(stmt, index++, dto);
            }
        };
        return Query.update(getConnection(), sql, setter);
    }

    public int delete(Object id) {
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
        return Query.update(getConnection(), sql, setter);
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
            c.putIntoDTO().getFromResultSet(rs, result);
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
                c.putIntoDTO().getFromResultSet(rs, result);
            }
            return result;
        };
    }

}
