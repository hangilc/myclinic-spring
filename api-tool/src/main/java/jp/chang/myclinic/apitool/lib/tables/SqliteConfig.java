package jp.chang.myclinic.apitool.lib.tables;

import com.github.javaparser.ast.expr.Expression;
import jp.chang.myclinic.apitool.databasespecifics.SqliteSpecifics;
import jp.chang.myclinic.apitool.lib.Helper;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SqliteConfig extends SqliteSpecifics implements Config {

    private Helper helper = Helper.getInstance();

    @Override
    public String basePackage() {
        return "jp.chang.myclinic.backendsqlite";
    }

    @Override
    public Path baseDir() {
        return Paths.get("backend-sqlite/src/main/java/jp/chang/myclinic/backendsqlite");
    }

    @Override
    public Expression generateStatementSetterArg(String tableName, Class<?> dbColumnClass, String dbColumnName,
                                                 Class<?> dtoClass, Class<?> dtoFieldClass, String dtoFieldName,
                                                 Expression fieldAccess) {
        if( dbColumnClass == String.class ){
            if( dtoFieldClass == Double.class || dtoFieldClass == Character.class || dtoFieldClass == Integer.class ) {
                return helper.methodCall("String", "valueOf", fieldAccess);
            }
        } else if( dbColumnClass == Integer.class ){
            if( dtoFieldClass == String.class ){
                return helper.methodCall("Integer", "parseInt", fieldAccess);
            }
        }
        return Config.super.generateStatementSetterArg(tableName, dbColumnClass, dbColumnName, dtoClass,
                dtoFieldClass, dtoFieldName, fieldAccess);
    }

    @Override
    public Expression generateDtoFieldSetterArg(String tableName, Class<?> dbColumnClass, String dbColumnName,
                                                Class<?> dtoClass, Class<?> dtoFieldClass, String dtoFieldName,
                                                Expression colValue) {
        return Config.super.generateDtoFieldSetterArg(tableName, dbColumnClass, dbColumnName, dtoClass,
                dtoFieldClass, dtoFieldName, colValue);
    }
}
