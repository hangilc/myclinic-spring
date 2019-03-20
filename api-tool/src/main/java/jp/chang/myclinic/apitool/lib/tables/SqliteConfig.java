package jp.chang.myclinic.apitool.lib.tables;

import com.github.javaparser.ast.expr.Expression;
import jp.chang.myclinic.apitool.databasespecifics.SqliteSpecifics;
import jp.chang.myclinic.apitool.lib.Helper;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SqliteConfig extends SqliteSpecifics implements Config {

    private Helper helper = Helper.getInstance();
    private StatementSetterGenerator statementSetterGenerator = new StatementSetterGenerator();
    private DtoFieldSetterGenerator dtoFieldSetterGenerator = new DtoFieldSetterGenerator();

    @Override
    public String basePackage() {
        return "jp.chang.myclinic.backendsqlite";
    }

    @Override
    public Path baseDir() {
        return Paths.get("backend-sqlite/src/main/java/jp/chang/myclinic/backendsqlite");
    }

    @Override
    public String getDtoFieldName(String table, String dbColumnName) {
        return helper.snakeToCamel(dbColumnName);
    }

    @Override
    public Expression generateStatementSetter(Class<?> dbColumnClass, Class<?> dtoFieldClass,
                                              String dtoClassName, String dtoFieldName) {
        return statementSetterGenerator.generate(dbColumnClass, dtoFieldClass, dtoClassName, dtoFieldName);
    }

    @Override
    public Expression generateDtoFieldSetter(Class<?> dbColumnClass, Class<?> dtoFieldClass,
                                             String dtoClassName, String dtoFieldName) {
        return dtoFieldSetterGenerator.generate(dbColumnClass, dtoFieldClass,
                dtoClassName, dtoFieldName);
    }
}
