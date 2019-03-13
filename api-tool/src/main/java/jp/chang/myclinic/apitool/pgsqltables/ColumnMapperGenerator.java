package jp.chang.myclinic.apitool.pgsqltables;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.UnknownType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.github.javaparser.ast.NodeList.nodeList;

class ColumnMapperGenerator {

    private String dtoFieldName;
    private Class<?> dbClass;
    private String dbColumnName;
    private Class<?> dtoFieldClass;
    private String tableName;

    ColumnMapperGenerator(String dtoFieldName, Class<?> dbClass, String dbColumnName,
                          Class<?> dtoFieldClass, String tableName) {
        this.dtoFieldName = dtoFieldName;
        this.dbClass = dbClass;
        this.dbColumnName = dbColumnName;
        this.dtoFieldClass = dtoFieldClass;
        this.tableName = tableName;
    }

    Expression generate() {
        if (dtoFieldClass == dbClass) {
            return generateSimple();
        } else if (dbClass == LocalDate.class && dtoFieldClass == String.class) {
            return generateLocalDateToString();
        } else if( dbClass == BigDecimal.class && dtoFieldClass == Double.class) {
            return generateBigDecimalToDouble();
        } else if( dbClass == BigDecimal.class && dtoFieldClass == String.class) {
            return generateBigDecimalToString();
        } else if( dbClass == BigDecimal.class && dtoFieldClass == Integer.class) {
            return generateBigDecimalToInteger();
        } else if( dbClass == String.class && dtoFieldClass == Character.class) {
            return generateStringToCharacter();
        } else if( dbClass == LocalDateTime.class && dtoFieldClass == String.class) {
            return generateLocalDateTimeToString();
        } else if( dbClass == Integer.class && dtoFieldClass == String.class ) {
            return generateIntegerToString();
        } else {
            String msg = String.format("Not suported type conversion (%s:%s %s -> %s)",
                    tableName, dtoFieldName,
                    dbClass.getSimpleName(), dtoFieldClass.getSimpleName());
            throw new RuntimeException(msg);
        }
    }

    private Expression generateBigDecimalToInteger() {
        String src = String.format("rs.getObject(\"%s\", %s.class).intValue()",
                dbColumnName, dbClass.getSimpleName());
        Expression right = StaticJavaParser.parseExpression(src);
        return generateExprLambda(right);
    }

    private Expression generateBigDecimalToString() {
        String src = String.format("rs.getObject(\"%s\", %s.class).toString()",
                dbColumnName, dbClass.getSimpleName());
        Expression right = StaticJavaParser.parseExpression(src);
        return generateExprLambda(right);
    }

    private Expression generateIntegerToString() {
        String src = String.format("rs.getObject(\"%s\", %s.class).toString()",
                dbColumnName, dbClass.getSimpleName());
        Expression right = StaticJavaParser.parseExpression(src);
        return generateExprLambda(right);
    }

    private Expression generateLocalDateTimeToString() {
        String src = String.format("TableBaseHelper.localDateTimeToString(rs.getObject(\"%s\", %s.class))",
                dbColumnName, dbClass.getSimpleName());
        Expression right = StaticJavaParser.parseExpression(src);
        return generateExprLambda(right);
    }

    private Expression generateStringToCharacter() {
        String src = String.format("rs.getObject(\"%s\", %s.class).charAt(0)",
                dbColumnName, dbClass.getSimpleName());
        Expression right = StaticJavaParser.parseExpression(src);
        return generateExprLambda(right);
    }

    private Expression generateBigDecimalToDouble() {
        String src = String.format("rs.getObject(\"%s\", %s.class).doubleValue()",
                dbColumnName, dbClass.getSimpleName());
        Expression right = StaticJavaParser.parseExpression(src);
        return generateExprLambda(right);
    }

    private Expression generateLocalDateToString(){
        if( dbColumnName.equals("valid_upto") ){
            String src = String.format("TableBaseHelper.getValidUpto(rs.getObject(\"%s\", %s.class))",
                    dbColumnName, dbClass.getSimpleName());
            Expression right = StaticJavaParser.parseExpression(src);
            return generateExprLambda(right);
        } else {
            String src = String.format("rs.getObject(\"%s\", %s.class).toString()",
                    dbColumnName, dbClass.getSimpleName());
            Expression right = StaticJavaParser.parseExpression(src);
            return generateExprLambda(right);
        }
    }

    private Expression generateSimple() {
        String src = String.format("rs.getObject(\"%s\", %s.class)",
                dbColumnName, dbClass.getSimpleName());
        Expression right =  StaticJavaParser.parseExpression(src);
        return generateExprLambda(right);
    }

    private Expression generateExprLambda(Expression right){
        Expression left = new FieldAccessExpr(new NameExpr("dto"), dtoFieldName);
        AssignExpr assign = new AssignExpr(left, right, AssignExpr.Operator.ASSIGN);
        return new LambdaExpr(createLambdaParams("rs", "dto"), assign);
    }

    private NodeList<Parameter> createLambdaParams(String a, String b) {
        Parameter param1 = new Parameter(new UnknownType(), a);
        Parameter param2 = new Parameter(new UnknownType(), b);
        return nodeList(param1, param2);
    }

}
