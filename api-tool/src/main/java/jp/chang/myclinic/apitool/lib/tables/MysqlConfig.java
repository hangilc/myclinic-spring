package jp.chang.myclinic.apitool.lib.tables;

import com.github.javaparser.ast.expr.Expression;
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
        if( dbColumnClass == LocalDate.class && dtoFieldClass == String.class ){
                return helper.methodCall("LocalDate", "parse", fieldAccess);
        }
        if( dbColumnClass == ValidUptoDate.class && dtoFieldClass == String.class ){
                return helper.methodCall("TableBaseHelper", "validUptoFromLocalDateToString", fieldAccess);
        }
        if( dbColumnClass == String.class && dtoFieldClass == Double.class ){
                return helper.methodCall("Double", "parseDouble", fieldAccess);
        }
        if( dbColumnClass == LocalDateTime.class && dtoFieldClass == String.class ){
                return helper.methodCall("TableBaseHelper", "localDateTimeToString", fieldAccess);
        }
        if( dbColumnClass == Character.class && dtoFieldClass == String.class ){
                return helper.methodCall("String", "valueOf", fieldAccess);
        }
        if( dbColumnClass == ShinryouTensuu.class && dtoFieldClass == Integer.class ){
                return helper.methodCall("TableBaseHelper", "tensuuToInteger", fieldAccess);
        }
        if( dbColumnClass == dtoFieldClass ){
            return fieldAccess;
        }
        return null;
    }

    @Override
    public DtoFieldSetterCreator getDtoFieldSetterCreator(String tableName, Class<?> dbColumnClass, String dbColumnName, Class<?> dtoClass, Class<?> dtoFieldClass, String dtoFieldName) {
        return null;
    }
}
