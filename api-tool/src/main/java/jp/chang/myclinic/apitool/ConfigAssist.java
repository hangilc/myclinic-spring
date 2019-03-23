package jp.chang.myclinic.apitool;

import jp.chang.myclinic.apitool.databasespecifics.DatabaseSpecifics;
import jp.chang.myclinic.apitool.databasespecifics.MysqlSpecifics;
import jp.chang.myclinic.apitool.databasespecifics.PgsqlSpecifics;
import jp.chang.myclinic.apitool.databasespecifics.SqliteSpecifics;
import jp.chang.myclinic.apitool.lib.DtoClassList;
import jp.chang.myclinic.apitool.lib.Helper;
import jp.chang.myclinic.apitool.lib.tables.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.*;
import java.util.*;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

@CommandLine.Command(name = "config")
public class ConfigAssist implements Runnable {

    @CommandLine.Parameters(arity = "1")
    private String database;

    private Helper helper = Helper.getInstance();

    @Override
    public void run() {
        Config dbSpecs = getSpecifics();
        try (Connection conn = getConnectionProvider().get()) {
            checkTableExists(dbSpecs, conn);
            checkFieldColumnRelations(dbSpecs, conn);
            checkColumnTypes(dbSpecs, conn);
            checkTypeConvert(dbSpecs, conn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void checkTypeConvert(Config dbSpecs, Connection conn) throws SQLException {
        List<Class<?>> dtoClasses = DtoClassList.getList();
        DatabaseMetaData meta = conn.getMetaData();
        for (Class<?> dtoClass : dtoClasses) {
            String tableName = dbSpecs.dtoClassToDbTableName(dtoClass);
            ResultSet rs = meta.getColumns(null, "public", tableName, "%");
            while (rs.next()) {
                String colName = rs.getString("COLUMN_NAME");
                String fieldName = dbSpecs.getDtoFieldName(tableName, colName);
                if (fieldName == null) {
                    continue;
                }
                String dbTypeName = rs.getString("TYPE_NAME");
                int sqlType = rs.getInt("DATA_TYPE");
                Class<?> colClass = dbSpecs.getDbColumnClass(tableName, colName, sqlType, dbTypeName);
                try {
                    dbSpecs.generateStatementSetter(tableName, colClass, colName, dtoClass, fieldName);
                } catch (GenerateStatementSetterException e) {
                    System.out.printf("Cannot generate setter method: %s:%s (%s) -> %s:%s (%s)\n",
                            dtoClass.getSimpleName(), fieldName,
                            helper.getDTOFieldClass(dtoClass, fieldName).getSimpleName(),
                            tableName, colName, colClass.getSimpleName()
                    );
                }
            }
            rs.close();
        }
        for (Class<?> dtoClass : dtoClasses) {
            String tableName = dbSpecs.dtoClassToDbTableName(dtoClass);
            ResultSet rs = meta.getColumns(null, "public", tableName, "%");
            while (rs.next()) {
                String colName = rs.getString("COLUMN_NAME");
                String fieldName = dbSpecs.getDtoFieldName(tableName, colName);
                if (fieldName == null) {
                    continue;
                }
                String dbTypeName = rs.getString("TYPE_NAME");
                int sqlType = rs.getInt("DATA_TYPE");
                Class<?> colClass = dbSpecs.getDbColumnClass(tableName, colName, sqlType, dbTypeName);
                try {
                    dbSpecs.generateDtoFieldSetter(tableName, colClass, colName, dtoClass, fieldName);
                } catch (DtoFieldSetterException e) {
                    System.out.printf("Cannot generate field setter method: %s:%s (%s) -> %s:%s (%s)\n",
                            tableName, colName, colClass.getSimpleName(),
                            dtoClass.getSimpleName(), fieldName,
                            helper.getDTOFieldClass(dtoClass, fieldName).getSimpleName()
                    );
                }
            }
            rs.close();
        }
    }

    private void checkColumnTypes(DatabaseSpecifics dbSpecs, Connection conn) throws SQLException {
        System.out.println("Checking for column types...");
        List<Class<?>> dtoClasses = DtoClassList.getList();
        DatabaseMetaData meta = conn.getMetaData();
        for (Class<?> dtoClass : dtoClasses) {
            String tableName = dbSpecs.dtoClassToDbTableName(dtoClass);
            ResultSet rs = meta.getColumns(null, "public", tableName, "%");
            while (rs.next()) {
                String colName = rs.getString("COLUMN_NAME");
                String fieldName = dbSpecs.getDtoFieldName(tableName, colName);
                if (fieldName == null) {
                    continue;
                }
                String dbTypeName = rs.getString("TYPE_NAME");
                int sqlType = rs.getInt("DATA_TYPE");
                Class<?> colClass = dbSpecs.getDbColumnClass(tableName, colName, sqlType, dbTypeName);
                if (colClass == null) {
                    System.out.printf("Cannot map %s:%s (%s, %s) to %s\n",
                            tableName, colName,
                            JDBCType.valueOf(sqlType).getName(),
                            dbTypeName,
                            helper.getDTOFieldClass(dtoClass, fieldName)
                    );
                }
            }
            rs.close();
        }
    }

    private void checkFieldColumnRelations(DatabaseSpecifics dbSpecs, Connection conn) throws SQLException {
        System.out.println("Checking for column-field relations...");
        List<Class<?>> dtoClasses = DtoClassList.getList();
        DatabaseMetaData meta = conn.getMetaData();
        class ColumnNames {
            private String dbName;
            private String fieldName;

            private ColumnNames(String dbName, String fieldName) {
                this.dbName = dbName;
                this.fieldName = fieldName;
            }
        }
        for (Class<?> dtoClass : dtoClasses) {
            String tableName = dbSpecs.dtoClassToDbTableName(dtoClass);
            List<ColumnNames> colNames = new ArrayList<>();
            List<String> fields = listDtoFields(dtoClass);
            ResultSet rs = meta.getColumns(null, "public", tableName, "%");
            while (rs.next()) {
                String dbColumnName = rs.getString("COLUMN_NAME");
                String name = dbSpecs.getDtoFieldName(tableName, dbColumnName);
                if (name != null) {
                    colNames.add(new ColumnNames(dbColumnName, name));
                }
            }
            rs.close();
            for (String field : new ArrayList<>(fields)) {
                ColumnNames match = null;
                for (ColumnNames cn : colNames) {
                    if (cn.fieldName.equals(field)) {
                        match = cn;
                        break;
                    }
                }
                if (match != null) {
                    colNames.remove(match);
                    fields.remove(field);
                }
            }
            if (fields.size() > 0) {
                System.out.printf("Table (%s) columns related to following %s fields area missing.\n",
                        tableName, dtoClass.getSimpleName());
                fields.forEach(System.out::println);
                System.out.println("Remaining columns are...");
                colNames.forEach(cn -> {
                    System.out.printf("  %s (%s)\n", cn.dbName, cn.fieldName);
                });
            }
            if (colNames.size() > 0) {
                System.out.println("Following column mapping should be removed.");
                colNames.forEach(cn -> System.out.printf("%s:%s -> %s\n", tableName, cn.dbName, cn.fieldName));
            }
        }
    }

    private List<String> listDtoFields(Class<?> dtoClass) {
        return Arrays.stream(dtoClass.getFields()).filter(f -> (f.getModifiers() & Modifier.PUBLIC) != 0)
                .map(Field::getName).collect(toList());
    }

    private void checkTableExists(DatabaseSpecifics dbSpecs, Connection conn) throws SQLException {
        System.out.println("Checking for database table existence...");
        List<Class<?>> dtoClasses = DtoClassList.getList();
        DatabaseMetaData meta = conn.getMetaData();
        for (Class<?> dtoClass : dtoClasses) {
            String tableName = dbSpecs.dtoClassToDbTableName(dtoClass);
            ResultSet rs = meta.getTables(null, "public", tableName, new String[]{"Table"});
            if (!rs.next()) {
                System.out.printf("Table %s is missing.\n", tableName);
            }
            rs.close();
        }
    }

    private Config getSpecifics() {
        switch (database) {
            case "mysql":
                return new MysqlConfig();
            case "sqlite":
                return new SqliteConfig();
//            case "pgsql":
//                return new PgsqlConfig();
            default:
                throw new RuntimeException("Cannot find database specifics: " + database);
        }
    }

    private Supplier<Connection> getConnectionProvider() {
        switch (database) {
            case "mysql":
                return new MysqlConnectionProvider();
            case "sqlite":
                return new SqliteConnectionProvider();
            case "pgsql":
                return new PgsqlConnectionProvider();
            default:
                throw new RuntimeException("Cannot find database specifics: " + database);
        }
    }
}
