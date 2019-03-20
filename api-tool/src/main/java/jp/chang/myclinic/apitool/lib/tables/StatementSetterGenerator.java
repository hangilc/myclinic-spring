package jp.chang.myclinic.apitool.lib.tables;

import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.UnknownType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static com.github.javaparser.ast.NodeList.nodeList;

public class StatementSetterGenerator {

    public static Expression generate(Class<?> dbColumnClass, Class<?> dtoFieldClass, String dtoFieldName) {
        return new LambdaExpr(
                nodeList(
                        new Parameter(new UnknownType(), "stmt"),
                        new Parameter(new UnknownType(), "i"),
                        new Parameter(new UnknownType(), "dto")
                ),
                generateSetter(dbColumnClass, dtoFieldClass, dtoFieldName)
        );
    }

    private static Expression generateSetter(Class<?> dbColumnClass, Class<?> dtoFieldClass, String dtoFieldName) {
        String setterMethod = getSetterMethod(dbColumnClass);
        Expression arg = generateArg(dbColumnClass, dtoFieldClass, dtoFieldName);
        return new MethodCallExpr(
                new NameExpr("stmt"),
                setterMethod,
                nodeList(new NameExpr("i"), arg)
        );
    }

    private static Expression generateArg(Class<?> dbColumnClass, Class<?> dtoFieldClass, String dtoFieldName) {
        Expression fieldExpr = new FieldAccessExpr(new NameExpr("dto"), dtoFieldName);
        if( dtoFieldName.equals("validUpto") ){
            if( dbColumnClass == LocalDate.class ){
                return methodCall("TableBaseHelper", "validUptoFromStringToLocalDate", fieldExpr);
            }
        }
        if (dbColumnClass == dtoFieldClass) {
            return fieldExpr;
        } else if (dbColumnClass == Integer.class) {
            if (dtoFieldClass == String.class) {
                return methodCall("Integer", "parseInt", fieldExpr);
            }
        } else if( dbColumnClass == String.class ){
            if( dtoFieldClass == Integer.class || dtoFieldClass == Double.class || dtoFieldClass == Character.class ){
                return methodCall("String", "valueOf", fieldExpr);
            }
        }
        String msg = String.format("Cannot convert %s (%s) to database column (%s)", dtoFieldName,
                dtoFieldClass.getSimpleName(), dbColumnClass.getSimpleName());
        throw new RuntimeException(msg);
    }

    private static Expression methodCall(String scope, String method, Expression arg) {
        return new MethodCallExpr(new NameExpr(scope), new SimpleName(method), nodeList(arg));
    }

    private static String getSetterMethod(Class<?> dbColumnClass) {
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

}
