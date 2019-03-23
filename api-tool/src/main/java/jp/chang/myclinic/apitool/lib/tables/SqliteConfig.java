package jp.chang.myclinic.apitool.lib.tables;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
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
        if (dbColumnClass == String.class) {
            if (dtoFieldClass == Double.class || dtoFieldClass == Character.class || dtoFieldClass == Integer.class) {
                return helper.methodCall("String", "valueOf", fieldAccess);
            }
        } else if (dbColumnClass == Integer.class) {
            if (dtoFieldClass == String.class) {
                return helper.methodCall("Integer", "parseInt", fieldAccess);
            }
        }
        if (dbColumnClass == dtoFieldClass) {
            return fieldAccess;
        } else {
            return null;
        }
    }

    @Override
    public DtoFieldSetterCreator getDtoFieldSetterCreator(String tableName, Class<?> dbColumnClass, String dbColumnName,
                                                          Class<?> dtoClass, Class<?> dtoFieldClass, String dtoFieldName) {
        if (dbColumnClass == dtoFieldClass) {
            return new DtoFieldSetterCreator(
                    getResultSetGetterMethod(dbColumnClass),
                    colValue -> colValue
            );
        }
        if (dbColumnClass == String.class) {
            if (dtoFieldClass == Character.class || dtoFieldClass == char.class) {
                return new DtoFieldSetterCreator(
                        "getString",
                        colValue -> helper.methodCall(colValue, "charAt", new IntegerLiteralExpr(0))
                );
            }
        }
        return null;
    }

    private String getResultSetGetterMethod(Class<?> dbColumnClass) {
        if (dbColumnClass == Integer.class || dbColumnClass == int.class) {
            return "getInt";
        } else if (dbColumnClass == Double.class || dbColumnClass == double.class) {
            return "getDouble";
        } else {
            return "getString";
        }
    }

}
