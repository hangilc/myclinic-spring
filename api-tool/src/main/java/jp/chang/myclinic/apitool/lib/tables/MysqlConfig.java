package jp.chang.myclinic.apitool.lib.tables;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import jp.chang.myclinic.apitool.databasespecifics.MysqlSpecifics;
import jp.chang.myclinic.apitool.lib.Helper;
import jp.chang.myclinic.apitool.types.ShinryouTensuu;
import jp.chang.myclinic.apitool.types.ValidUptoDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MysqlConfig extends MysqlSpecifics implements Config {

    private Helper helper = Helper.getInstance();

    @Override
    public String basePackage() {
        return "jp.chang.myclinic.backendmysql";
    }

    @Override
    public Path baseDir() {
        return Paths.get("backend-mysql/src/main/java/jp/chang/myclinic/backendmysql");
    }

    @Override
    public Expression generateStatementSetterArg(String tableName, Class<?> dbColumnClass, String dbColumnName,
                                                 Class<?> dtoClass, Class<?> dtoFieldClass, String dtoFieldName,
                                                 Expression fieldAccess) {
        if (dtoFieldClass == String.class && dbColumnClass == LocalDate.class) {
            return helper.methodCall("LocalDate", "parse", fieldAccess);
        }
        if (dtoFieldClass == String.class && dbColumnClass == ValidUptoDate.class) {
            return helper.methodCall("TableBaseHelper", "validUptoFromStringToLocalDate", fieldAccess);
        }
        if (dtoFieldClass == Double.class && dbColumnClass == String.class) {
            return helper.methodCall("String", "valueOf", fieldAccess);
        }
        if (dtoFieldClass == String.class && dbColumnClass == LocalDateTime.class) {
            return helper.methodCall("TableBaseHelper", "stringToLocalDateTime", fieldAccess);
        }
        if (dtoFieldClass == String.class && dbColumnClass == Character.class) {
            return helper.methodCall(fieldAccess, "charAt", new IntegerLiteralExpr(0));
        }
        if (dtoFieldClass == Integer.class && dbColumnClass == ShinryouTensuu.class) {
            return helper.methodCall("String", "valueOf", fieldAccess);
        }
        if( dtoFieldClass == Character.class && dbColumnClass == String.class) {
            return helper.methodCall("String", "valueOf", fieldAccess);
        }
        if (dbColumnClass == dtoFieldClass) {
            return fieldAccess;
        }
        return null;
    }

    @Override
    public DtoFieldSetterCreator getDtoFieldSetterCreator(String tableName, Class<?> dbColumnClass,
                                                          String dbColumnName, Class<?> dtoClass,
                                                          Class<?> dtoFieldClass, String dtoFieldName) {
        if (dbColumnClass == LocalDate.class && dtoFieldClass == String.class) {
            return new DtoFieldSetterCreator(
                    "getObject",
                    List.of(helper.classLiteral(LocalDate.class)),
                    colValue -> helper.methodCall(colValue, "toString")
            );
        }
        if (dbColumnClass == ValidUptoDate.class && dtoFieldClass == String.class) {
            return new DtoFieldSetterCreator(
                    "getObject",
                    List.of(helper.classLiteral(LocalDate.class)),
                    colValue -> helper.methodCall("TableBaseHelper", "validUptoFromLocalDateToString", colValue)
            );
        }
        if (dbColumnClass == String.class && dtoFieldClass == Double.class) {
            return new DtoFieldSetterCreator(
                    "getString",
                    colValue -> helper.methodCall("Double", "parseDouble", colValue)
            );
        }
        if (dbColumnClass == LocalDateTime.class && dtoFieldClass == String.class) {
            return new DtoFieldSetterCreator(
                    "getObject",
                    List.of(helper.classLiteral(LocalDateTime.class)),
                    colValue -> helper.methodCall("TableBaseHelper", "localDateTimeToString", colValue)
            );
        }
        if (dbColumnClass == Character.class && dtoFieldClass == Character.class) {
            return new DtoFieldSetterCreator(
                    "getString",
                    colValue -> helper.methodCall(colValue, "charAt", new IntegerLiteralExpr(0))
            );
        }
        if (dbColumnClass == Character.class && dtoFieldClass == String.class) {
            return new DtoFieldSetterCreator(
                    "getString",
                    colValue -> colValue
            );
        }
        if (dbColumnClass == ShinryouTensuu.class && dtoFieldClass == Integer.class) {
            return new DtoFieldSetterCreator(
                    "getString",
                    colValue -> helper.methodCall("TableBaseHelper", "tensuuToInteger", colValue)
            );
        }
        if( dbColumnClass == String.class && dtoFieldClass == Character.class ){
            return new DtoFieldSetterCreator(
                    "getString",
                    colValue -> helper.methodCall(colValue, "charAt", new IntegerLiteralExpr(0))
            );
        }
        if (dbColumnClass == dtoFieldClass) {
            return new DtoFieldSetterCreator(
                    getResultSetGetterMethod(dbColumnClass),
                    colValue -> colValue
            );
        }
        return null;
    }

    private String getResultSetGetterMethod(Class<?> dbColumnClass) {
        if (dbColumnClass == Integer.class || dbColumnClass == int.class) {
            return "getInt";
        } else if (dbColumnClass == Double.class || dbColumnClass == double.class) {
            return "getDouble";
        } else if (dbColumnClass == String.class) {
            return "getString";
        } else {
            return "getObject";
        }
    }

}
