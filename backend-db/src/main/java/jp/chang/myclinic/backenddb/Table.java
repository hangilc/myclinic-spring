package jp.chang.myclinic.backenddb;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;
import static jp.chang.myclinic.backenddb.Query.*;

public abstract class Table<DTO> implements Query.Projector<DTO>, TableInterface<DTO> {

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
                    colmap.get(false).stream().map(Column::getPlaceHolder).collect(joining(","))
            );
            SqlConsumer<PreparedStatement> setter = stmt -> {
                int i = 1;
                for (Column<DTO> c : colmap.get(false)) {
                    c.getFromDTO().set(stmt, i++, dto);
                }
            };
            Query.exec(sql, setter);
        } else {
            String sql = String.format("insert into %s (%s) values (%s)",
                    getTableName(),
                    colmap.get(false).stream().map(Column::getName).collect(joining(",")),
                    colmap.get(false).stream().map(Column::getPlaceHolder).collect(joining(","))
            );
            List<Column<DTO>> autoIncs = colmap.get(true);
            SqlConsumer<PreparedStatement> setter = stmt -> {
                int i = 1;
                for (Column<DTO> c : colmap.get(false)) {
                    c.getFromDTO().set(stmt, i++, dto);
                }
            };
            SqlConsumer<ResultSet> mapper = rs -> {
                int i = 1;
                for (Column<DTO> c : autoIncs) {
                    c.putIntoDTO().getFromResultSet(rs, i++, dto);
                }
            };
            int n = Query.update(sql, PreparedStatement.RETURN_GENERATED_KEYS,
                    mapper, setter);
            if (n != 1) {
                throw new RuntimeException("insert affected non-signle row: " + n);
            }
        }
    }

    public DTO getById(Object id) {
        List<Column<DTO>> primaries = getColumns().stream().filter(Column::isPrimary).collect(toList());
        if (primaries.size() != 1) {
            throw new RuntimeException("Number of primary key is not one.");
        }
        Column<DTO> primary = primaries.get(0);
        String sql = String.format("select * from %s where %s = ?",
                getTableName(), primary.getName());
        return Query.get(sql, this, id);
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
                nonPrimaries.stream().map(c -> c.getName() + "=" + c.getPlaceHolder()).collect(joining(",")),
                primaries.stream().map(c -> c.getName() + "=" + c.getPlaceHolder()).collect(joining(" and "))
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
        Query.update(sql, setter);
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
        Query.update(sql, setter);
    }

    public DTO project(ResultSet rs, Query.ResultSetContext ctx) throws SQLException {
        DTO dto = newInstanceDTO();
        for (Column<DTO> c : getColumns()) {
            c.putIntoDTO().getFromResultSet(rs, ctx.nextIndex(), dto);
        }
        return dto;
    }

    public Column<DTO> getColumnByDbColumnName(String dbColumnName) {
        for (Column<DTO> c : getColumns()) {
            if (c.getName().equals(dbColumnName)) {
                return c;
            }
        }
        return null;
    }

}
