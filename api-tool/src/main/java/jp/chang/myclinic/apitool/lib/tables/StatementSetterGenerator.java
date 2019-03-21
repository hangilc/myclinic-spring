package jp.chang.myclinic.apitool.lib.tables;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.UnknownType;
import org.openjdk.tools.javac.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static com.github.javaparser.ast.NodeList.nodeList;

public class StatementSetterGenerator {

    public Expression generate(Class<?> dbColumnClass, Class<?> dtoFieldClass,
                                      String dtoClassName, String dtoFieldName) {
        return new LambdaExpr(generateParameters(),
                generateSetter(dbColumnClass, dtoFieldClass, dtoClassName, dtoFieldName)
        );
    }

    public Expression generate(String setterMethod, Expression arg){
        return new LambdaExpr(
                nodeList(
                        new Parameter(new UnknownType(), "stmt"),
                        new Parameter(new UnknownType(), "i"),
                        new Parameter(new UnknownType(), "dto")
                ),
                new MethodCallExpr(
                        new NameExpr("stmt"),
                        setterMethod,
                        nodeList(new NameExpr("i"), arg)
                )
        );
    }

    private NodeList<Parameter> generateParameters(){
        return nodeList(
                new Parameter(new UnknownType(), "stmt"),
                new Parameter(new UnknownType(), "i"),
                new Parameter(new UnknownType(), "dto")
        );
    }

    protected Expression generateSetter(Class<?> dbColumnClass, Class<?> dtoFieldClass,
                                        String dtoClassName, String dtoFieldName) {
        String setterMethod = getSetterMethod(dbColumnClass);
        Expression arg = generateArg(dbColumnClass, dtoFieldClass, dtoClassName, dtoFieldName);
        return new MethodCallExpr(
                new NameExpr("stmt"),
                setterMethod,
                nodeList(new NameExpr("i"), arg)
        );
    }

    protected Expression generateArg(Class<?> dbColumnClass, Class<?> dtoFieldClass,
                                     String dtoClassName, String dtoFieldName) {
        Expression fieldExpr = new FieldAccessExpr(new NameExpr("dto"), dtoFieldName);
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

    protected Expression methodCall(String scope, String method, Expression arg) {
        return new MethodCallExpr(new NameExpr(scope), new SimpleName(method), nodeList(arg));
    }

    protected String getSetterMethod(Class<?> dbColumnClass) {
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
