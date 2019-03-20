package jp.chang.myclinic.apitool.lib.tables;

import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.UnknownType;

import static com.github.javaparser.ast.NodeList.nodeList;

public class DtoFieldSetterGenerator {

    public Expression generate(Class<?> dbColumnClass, Class<?> dtoFieldClass,
                               String dtoClassName, String dtoFieldName) {
        AssignExpr assign = new AssignExpr(
                new FieldAccessExpr(new NameExpr("dto"), dtoFieldName),
                generateArg(dbColumnClass, dtoFieldClass, dtoClassName, dtoFieldName),
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

    private Expression generateArg(Class<?> dbColumnClass, Class<?> dtoFieldClass,
                                   String dtoClassName, String dtoFieldName) {
        Expression colValue = new MethodCallExpr(
                new NameExpr("rs"),
                new SimpleName("getObject"),
                nodeList(
                        new NameExpr("i"),
                        new FieldAccessExpr(new NameExpr(dbColumnClass.getSimpleName()), "class")
                )
        );
        if (dtoFieldClass == dbColumnClass) {
            return colValue;
        } else if (dtoFieldClass == Double.class) {
            if (dbColumnClass == String.class) {
                return methodCall("Double", "valueOf", colValue);
            }
        } else if (dtoFieldClass == Character.class) {
            if (dbColumnClass == String.class) {
                return methodCall(colValue, "charAt", new IntegerLiteralExpr(0));
            }
        } else if (dtoFieldClass == String.class) {
            if (dbColumnClass == Integer.class) {
                return methodCall("String", "valueOf", colValue);
            }
        } else if (dtoFieldClass == Integer.class) {
            if (dbColumnClass == String.class) {
                return methodCall("TableBaseHelper", "stringToInteger", colValue);
            }
        }
        String msg = String.format("Cannot convert to %s (%s) from database column (%s)",
                dtoFieldName, dtoFieldClass.getSimpleName(), dbColumnClass.getSimpleName());
        throw new RuntimeException(msg);
    }

    protected Expression methodCall(String scope, String method, Expression arg) {
        return new MethodCallExpr(new NameExpr(scope), new SimpleName(method), nodeList(arg));
    }

    protected Expression methodCall(Expression scope, String method, Expression arg) {
        return new MethodCallExpr(scope, new SimpleName(method), nodeList(arg));
    }


}
