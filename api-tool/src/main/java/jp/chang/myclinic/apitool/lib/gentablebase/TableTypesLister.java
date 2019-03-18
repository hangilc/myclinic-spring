package jp.chang.myclinic.apitool.lib.gentablebase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TableTypesLister {

    public static class ColumnType {
        public String columnName;
        public int sqlType;
        public String sqlTypeName;
        public String dbTypeName;

        @Override
        public String toString() {
            return "ColumnType{" +
                    "columnName='" + columnName + '\'' +
                    ", sqlType=" + sqlType +
                    ", sqlTypeName='" + sqlTypeName + '\'' +
                    ", dbTypeName='" + dbTypeName + '\'' +
                    '}';
        }
    }

    public Map<String, List<ColumnType>> listTypes(Connection conn) throws SQLException {
        Map<String, List<ColumnType>> result = new LinkedHashMap<>();
        DatabaseMetaData meta = conn.getMetaData();
        for (String table : listTableNames(meta)) {
            result.put(table, listColumns(meta, table));
        }
        return result;
    }

    private List<String> listTableNames(DatabaseMetaData meta) throws SQLException {
        ResultSet rs = meta.getTables(null, "public", "%", new String[]{"TABLE"});
        List<String> tables = new ArrayList<>();
        while (rs.next()) {
            tables.add(rs.getString("TABLE_NAME"));
        }
        rs.close();
        return tables;
    }

    private List<ColumnType> listColumns(DatabaseMetaData meta, String table) throws SQLException {
        List<ColumnType> cols = new ArrayList<>();
        ResultSet rs = meta.getColumns(null, "public", table, "%");
        while (rs.next()) {
            ColumnType ct = new ColumnType();
            ct.columnName = rs.getString("COLUMN_NAME");
            ct.dbTypeName = rs.getString("TYPE_NAME");
            ct.sqlType = rs.getInt("DATA_TYPE");
            ct.sqlTypeName = JDBCType.valueOf(ct.sqlType).getName();
            cols.add(ct);
        }
        rs.close();
        return cols;
    }

}
