package jp.chang.myclinic.apitool.lib.tables;

import com.github.javaparser.ast.expr.Expression;
import jp.chang.myclinic.apitool.databasespecifics.PgsqlSpecifics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class PgsqlConfig extends PgsqlSpecifics implements Config {


    @Override
    public String basePackage() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Path baseDir() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Expression generateStatementSetterArg(String tableName, Class<?> dbColumnClass, String dbColumnName, Class<?> dtoClass, Class<?> dtoFieldClass, String dtoFieldName, Expression fieldAccess) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public DtoFieldSetterCreator getDtoFieldSetterCreator(String tableName, Class<?> dbColumnClass, String dbColumnName, Class<?> dtoClass, Class<?> dtoFieldClass, String dtoFieldName) {
        throw new RuntimeException("not implemented");
    }
}
