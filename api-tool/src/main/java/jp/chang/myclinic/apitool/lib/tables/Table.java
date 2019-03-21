package jp.chang.myclinic.apitool.lib.tables;

import jp.chang.myclinic.apitool.databasespecifics.DatabaseSpecifics;

import java.sql.DatabaseMetaData;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Table {

    private String tableName;
    private List<Column> columns = new ArrayList<>();

    public Table(String tableName, DatabaseMetaData meta, DatabaseSpecifics config) throws SQLException {
        this.tableName = tableName;
        ResultSet rs = meta.getColumns(null, "public", tableName, "%");
        while (rs.next()) {
            String dbColumnName = rs.getString("COLUMN_NAME");
            boolean isAutoIncrement = rs.getString("IS_AUTOINCREMENT").equals("YES");
            String dbTypeName = rs.getString("TYPE_NAME");
            int sqlType = rs.getInt("DATA_TYPE");
            Class<?> dbColumnClass = config.getDbColumnClass(tableName, dbColumnName, sqlType, dbTypeName);
            if (dbColumnClass == null) {
                String msg = String.format("Cannot handle sql type (%s:%s %s %d[%s]).",
                        tableName, dbColumnName,
                        dbTypeName, sqlType, JDBCType.valueOf(sqlType).getName());
                throw new RuntimeException(msg);
            }
            String dtoFieldName = config.getDtoFieldName(tableName, dbColumnName);
            columns.add(new Column(dbColumnName, isAutoIncrement, dbColumnClass, dtoFieldName));
        }
        rs.close();
        rs = meta.getPrimaryKeys(null, "public", tableName);
        while (rs.next()) {
            String name = rs.getString("COLUMN_NAME");
            for (Column c : columns) {
                if (c.getDbColumnName().equals(name)) {
                    c.setPrimary(true);
                    break;
                }
            }
        }
        rs.close();
    }

    public String getTableName() {
        return tableName;
    }

    public List<Column> getColumns() {
        return columns;
    }
}