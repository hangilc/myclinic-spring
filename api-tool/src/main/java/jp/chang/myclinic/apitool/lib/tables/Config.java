package jp.chang.myclinic.apitool.lib.tables;

import com.github.javaparser.ast.expr.Expression;
import jp.chang.myclinic.apitool.databasespecifics.DatabaseSpecifics;

import java.nio.file.Path;

public interface Config extends DatabaseSpecifics {

    String basePackage();
    Path baseDir();
    Expression generateStatementSetter(Class<?> dbColumnClass, Class<?> dtoFieldClass,
                                       String dtoClassName, String dtoFieldName);
    Expression generateDtoFieldSetter(Class<?> dbColumnClass, Class<?> dtoFieldClass,
                                       String dtoClassName, String dtoFieldName);
}
