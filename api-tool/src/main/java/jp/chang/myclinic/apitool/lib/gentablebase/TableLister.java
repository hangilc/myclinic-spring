package jp.chang.myclinic.apitool.lib.gentablebase;

import jp.chang.myclinic.apitool.databasespecifics.DatabaseSpecifics;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TableLister {

//    private DatabaseSpecifics dbSpecs;
//
//    public TableLister(DatabaseSpecifics dbSpecs) {
//        this.dbSpecs = dbSpecs;
//    }
//
//    public List<Table> listTables(Connection conn) throws SQLException {
//        DatabaseMetaData meta = conn.getMetaData();
//        List<Table> tables = new ArrayList<>();
//        for (String table : listTableNames(meta)) {
//            List<Column> columns = listColumns(meta, table);
//            tables.add(new Table(table, columns));
//        }
//        return tables;
//    }
//
//    private List<String> listTableNames(DatabaseMetaData meta) throws SQLException {
//        ResultSet rs = meta.getTables(null, "public", "%", new String[]{"TABLE"});
//        List<String> tables = new ArrayList<>();
//        while (rs.next()) {
//            tables.add(rs.getString("TABLE_NAME"));
//        }
//        rs.close();
//        return tables;
//    }
//
//    private List<Column> listColumns(DatabaseMetaData meta, String table) throws SQLException {
//        List<Column> cols = new ArrayList<>();
//        ResultSet rs = meta.getColumns(null, "public", table, "%");
//        while (rs.next()) {
//            String name = rs.getString("COLUMN_NAME");
//            boolean isAutoIncrement = rs.getString("IS_AUTOINCREMENT").equals("YES");
//            String dbTypeName = rs.getString("TYPE_NAME");
//            int dataType = rs.getInt("DATA_TYPE");
//            Class<?> jdbcClass = dbSpecs.mapDatabaseClass(dataType, dbTypeName);
//            if (jdbcClass == null) {
//                String msg = String.format("Cannot handle sql type (%s:%s %s %d[%s]).", table, name,
//                        dbTypeName, dataType, JDBCType.valueOf(dataType).getName());
//                throw new RuntimeException(msg);
//            }
//            String dtoFieldName = dbSpecs.resolveDtoFieldName(table, name);
//            cols.add(new Column(name, isAutoIncrement, jdbcClass, dtoFieldName));
//        }
//        rs.close();
//        rs = meta.getPrimaryKeys(null, "public", table);
//        while (rs.next()) {
//            String name = rs.getString("COLUMN_NAME");
//            for (Column c : cols) {
//                if (c.getName().equals(name)) {
//                    c.setPrimary(true);
//                    break;
//                }
//            }
//        }
//        rs.close();
//        return cols;
//    }
}
