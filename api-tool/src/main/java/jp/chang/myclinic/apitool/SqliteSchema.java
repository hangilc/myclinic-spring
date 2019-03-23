package jp.chang.myclinic.apitool;

import com.sun.jdi.DoubleType;
import com.sun.jdi.IntegerType;
import jp.chang.myclinic.apitool.databasespecifics.PgsqlSpecifics;
import jp.chang.myclinic.apitool.databasespecifics.SqliteSpecifics;
import jp.chang.myclinic.apitool.lib.DtoClassList;
import jp.chang.myclinic.apitool.lib.Helper;
import jp.chang.myclinic.apitool.lib.sqliteschema.Create;
import jp.chang.myclinic.apitool.lib.sqliteschema.TableColumn;
import jp.chang.myclinic.apitool.lib.tables.Column;
import jp.chang.myclinic.apitool.lib.tables.Config;
import jp.chang.myclinic.apitool.lib.tables.Table;
import jp.chang.myclinic.dto.annotation.AutoInc;
import jp.chang.myclinic.dto.annotation.Primary;
import picocli.CommandLine;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.joining;

@CommandLine.Command(name = "sqlite-schema")
public class SqliteSchema implements Runnable {

    @CommandLine.Option(names = {"--output"}, description = "Saves to file.")
    private String outputFile;

    @CommandLine.Option(names = {"--check"}, description = "Checks columns against Postgresql database.")
    private boolean check;

    private Helper helper = Helper.getInstance();

    @Override
    public void run() {
        List<Class<?>> dtoClasses = DtoClassList.getList();
        SqliteSpecifics dbSpecs = new SqliteSpecifics();
        List<Create> tables = new ArrayList<>();
        for(Class<?> dtoClass: dtoClasses){
            String tableName = helper.toSnake(dtoClass.getSimpleName().replaceAll("DTO$", ""));
            Create table = new Create(tableName);
            tables.add(table);
            Set<String> primaries = new HashSet<>();
            Set<String> autoIncs = new HashSet<>();
            for(Field field: dtoClass.getFields()){
                if( (field.getModifiers() & Modifier.PUBLIC) != 0 ){
                    boolean isPrimary = false;
                    boolean isAutoInc = false;
                    String fieldName = helper.toSnake(field.getName());
                    if( field.isAnnotationPresent(Primary.class) ){
                        primaries.add(fieldName);
                        isPrimary = true;
                    }
                    if( field.isAnnotationPresent(AutoInc.class) ){
                        autoIncs.add(fieldName);
                        isAutoInc = true;
                    }
                    String type = getDbType(field.getType());
                    table.addColumn(fieldName, type, isPrimary, isAutoInc, field.getName());
                }
            }
            if( primaries.isEmpty() ){
                System.err.printf("No primary key: %s\n", tableName);
                System.exit(1);
            }
            if( autoIncs.size() > 1 ){
                System.err.printf("Multiple autoincs: %s\n", tableName);
                System.exit(1);
            }
            if( !check ){
                String schema = tables.stream().map(Create::output).collect(joining("\n"));
                save(outputFile, schema);
            }
        }
        if( check ){
            Set<String> primaries = new HashSet<>();
            Set<String> autoIncs = new HashSet<>();
            try(Connection conn = new PgsqlConnectionProvider().get()){
                PgsqlSpecifics pgsqlSpecs = new PgsqlSpecifics();
                for(Class<?> dtoClass: dtoClasses){
                    String tableName = pgsqlSpecs.dtoClassToDbTableName(dtoClass);
                    DatabaseMetaData meta = conn.getMetaData();
                    Table table = new Table(tableName, meta, pgsqlSpecs);
                    String sqlTableName = helper.toSnake(dtoClass.getSimpleName().replaceAll("DTO$", ""));
                    for(Column tc: table.getColumns()){
                        if( tc.isPrimary() ){
                            primaries.add(sqlTableName + ":" + tc.getDtoFieldName());
                        }
                        if( tc.isAutoIncrement() ){
                            autoIncs.add(sqlTableName + ":" + tc.getDtoFieldName());
                        }
                    }
                }
            } catch(SQLException e){
                throw new RuntimeException(e);
            }
            Set<String> sqlitePrimaries = new HashSet<>();
            Set<String> sqliteAutoIncs = new HashSet<>();
            for(Create table: tables){
                for(TableColumn tc: table.getColumns()){
                    if( tc.isPrimary() ){
                        sqlitePrimaries.add(table.getTableName() + ":" + tc.getDtoFieldName());
                    }
                    if( tc.isAutoInc() ){
                        sqliteAutoIncs.add(table.getTableName() + ":" + tc.getDtoFieldName());
                    }
                }
            }
            if( !primaries.equals(sqlitePrimaries) ){
                Set<String> diff = new HashSet<>(primaries);
                diff.removeAll(sqlitePrimaries);
                if( diff.size() > 0 ){
                    System.out.println("Sqlite schema does not contain primary keys: " + diff);
                }
                diff = new HashSet<>(sqlitePrimaries);
                diff.removeAll(primaries);
                if( diff.size() > 0 ){
                    System.out.println("Pgsql schema does not contain primary keys: " + diff);
                }
            }
            if( !autoIncs.equals(sqliteAutoIncs) ){
                Set<String> diff = new HashSet<>(autoIncs);
                diff.removeAll(sqliteAutoIncs);
                if( diff.size() > 0 ){
                    System.out.println("Sqlite schema does not contain autoincs " + diff);
                }
                diff = new HashSet<>(sqliteAutoIncs);
                diff.removeAll(autoIncs);
                if( diff.size() > 0 ){
                    System.out.println("Pgsql schema does not contain autoincs: " + diff);
                }
            }
        }
    }

    private String getDbType(Class<?> dtoFieldType){
        if( dtoFieldType == Integer.class || dtoFieldType == int.class ){
            return "integer";
        } else if( dtoFieldType == Double.class || dtoFieldType == double.class ){
            return "real";
        } else {
            return "string";
        }
    }

    private void save(String file, String src){
        if( file == null ){
            System.out.println(src);
        } else {
            try {
                Files.write(Paths.get(file), src.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
