package jp.chang.myclinic.apitool.lib.tables;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.UnknownType;
import jp.chang.myclinic.apitool.databasespecifics.DatabaseSpecifics;
import jp.chang.myclinic.apitool.lib.Helper;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.github.javaparser.ast.NodeList.nodeList;

public interface Config extends DatabaseSpecifics {

    String basePackage();

    Path baseDir();

    default Expression generateStatementSetter(String tableName, Class<?> dbColumnClass, String dbColumnName,
                                               Class<?> dtoClass, String dtoFieldName) {
        Expression fieldExpr = new FieldAccessExpr(new NameExpr("dto"), dtoFieldName);
        Class<?> dtoFieldClass = Helper.getInstance().getDTOFieldClass(dtoClass, dtoFieldName);
        Expression arg = generateStatementSetterArg(tableName, dbColumnClass, dbColumnName,
                dtoClass, dtoFieldClass, dtoFieldName, fieldExpr);
        if (arg == null) {
            String msg = String.format("Cannot convert dto field %s (%s) to db field %s (%s) in %s",
                    dtoFieldName, dtoFieldClass.getSimpleName(),
                    dbColumnName, dbColumnClass.getSimpleName(),
                    tableName);
            throw new GenerateStatementSetterException(msg);
        }
        return new LambdaExpr(
                nodeList(
                        new Parameter(new UnknownType(), "stmt"),
                        new Parameter(new UnknownType(), "i"),
                        new Parameter(new UnknownType(), "dto")
                ),
                new MethodCallExpr(
                        new NameExpr("stmt"),
                        getStatementSetterMethod(dtoFieldClass),
                        nodeList(new NameExpr("i"), arg)
                )
        );
    }

    default String getStatementSetterMethod(Class<?> dbColumnClass) {
        if (dbColumnClass == Integer.class) {
            return "setInt";
        } else if (dbColumnClass == String.class) {
            return "setString";
        } else if (dbColumnClass == Double.class) {
            return "setDouble";
        } else {
            return "setObject";
        }
    }

    default Expression generateStatementSetterArg(String tableName, Class<?> dbColumnClass, String dbColumnName,
                                                  Class<?> dtoClass, Class<?> dtoFieldClass, String dtoFieldName,
                                                  Expression fieldAccess) {
        if (dbColumnClass == dtoFieldClass) {
            return fieldAccess;
        } else {
            return null;
        }
    }

    default Expression generateDtoFieldSetter(String tableName, Class<?> dbColumnClass, String dbColumnName,
                                              Class<?> dtoClass, String dtoFieldName) {
        Class<?> dtoFieldClass = Helper.getInstance().getDTOFieldClass(dtoClass, dtoFieldName);
        DtoFieldSetterCreator setterCreator = getDtoFieldSetterCreator(tableName, dbColumnClass, dbColumnName,
                dtoClass, dtoFieldClass, dtoFieldName);
        if (setterCreator == null) {
            String msg = String.format("Cannot convert db column %s (%s) to dto field %s (%s) in %s",
                    dbColumnName, dbColumnClass.getSimpleName(),
                    dtoFieldName, dtoFieldClass.getSimpleName(),
                    dtoClass.getSimpleName());
            throw new DtoFieldSetterException(msg);
        }
        NodeList<Expression> setterArgs = nodeList(new NameExpr("i"));
        setterArgs.addAll(setterCreator.setterArgs);
                nodeList(new Expression[]{new NameExpr("i")})
                .addAll(nodeList(setterCreator.setterArgs.toArray(new Expression[]{})));
        Expression colValue = new MethodCallExpr(
                new NameExpr("rs"),
                new SimpleName(setterCreator.setterMethod),
                setterArgs);
        Expression arg = setterCreator.argMaker.apply(colValue);
        AssignExpr assign = new AssignExpr(
                new FieldAccessExpr(new NameExpr("dto"), dtoFieldName),
                arg,
                AssignExpr.Operator.ASSIGN
        );
        return new LambdaExpr(
                nodeList(
                        new Parameter(new UnknownType(), "rs"),
                        new Parameter(new UnknownType(), "i"),
                        new Parameter(new UnknownType(), "dto")
                ),
                assign
        );
    }

    DtoFieldSetterCreator getDtoFieldSetterCreator(String tableName, Class<?> dbColumnClass, String dbColumnName,
                                                   Class<?> dtoClass, Class<?> dtoFieldClass, String dtoFieldName);
}
