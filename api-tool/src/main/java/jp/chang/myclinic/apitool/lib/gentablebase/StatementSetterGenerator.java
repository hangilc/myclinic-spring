package jp.chang.myclinic.apitool.lib.gentablebase;

import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import jp.chang.myclinic.apitool.lib.Helper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.github.javaparser.ast.NodeList.nodeList;

class StatementSetterGenerator {

    private static class Pair {
        Class<?> dbClass;
        Class<?> fieldClass;

        Pair(Class<?> dbClass, Class<?> fieldClass) {
            this.dbClass = dbClass;
            this.fieldClass = fieldClass;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return Objects.equals(dbClass, pair.dbClass) &&
                    Objects.equals(fieldClass, pair.fieldClass);
        }

        @Override
        public int hashCode() {
            return Objects.hash(dbClass, fieldClass);
        }
    }

    private interface Generator {
        Expression generate(String fieldName);
    }

    private class ValidUpto {

    }

    private static Map<Pair, Generator> genMap = new HashMap<>();

    static {
        genMap.put(new Pair(Integer.class, Integer.class),
                fieldName -> genLambda("setInt", dtoFieldExpr(fieldName)));
        genMap.put(new Pair(Integer.class, String.class),
                fieldName -> genLambda("setInt", funcallExpr("Integer", "parseInt", fieldName)));
        genMap.put(new Pair(String.class, String.class),
                fieldName -> genLambda("setString", dtoFieldExpr(fieldName)));
        genMap.put(new Pair(String.class, Character.class),
                fieldName -> genLambda("setString", funcallExpr("String", "valueOf", fieldName)));
        genMap.put(new Pair(LocalDate.class, String.class),
                fieldName -> genLambda("setObject", funcallExpr("LocalDate", "parse", fieldName)));
        genMap.put(new Pair(LocalDate.class, ValidUpto.class),
                fieldName -> genLambda("setObject",
                        funcallExpr("TableBaseHelper", "validUptoFromStringToLocalDate", fieldName)));
        Generator genBigDecimal = fieldName -> genLambda("setBigDecimal", ctorExpr("BigDecimal", fieldName));
        genMap.put(new Pair(BigDecimal.class, Double.class), genBigDecimal);
        genMap.put(new Pair(BigDecimal.class, String.class), genBigDecimal);
        genMap.put(new Pair(BigDecimal.class, Integer.class), genBigDecimal);
        genMap.put(new Pair(LocalDateTime.class, String.class),
                fieldName -> genLambda("setObject",
                        funcallExpr("TableBaseHelper", "stringToLocalDateTime", fieldName)));
    }

    private static Helper helper = Helper.getInstance();

    private static Expression dtoFieldExpr(String fieldName){
        return new FieldAccessExpr(new NameExpr("dto"), fieldName);
    }

    private static Expression funcallExpr(String scope, String name, String fieldName){
        return new MethodCallExpr(new NameExpr(scope), name, nodeList(dtoFieldExpr(fieldName)));
    }

    private static Expression ctorExpr(String ctorName, String fieldName){
        return new ObjectCreationExpr(
                null, new ClassOrInterfaceType(null, ctorName), nodeList(dtoFieldExpr(fieldName)));
    }

    private static Expression genLambda(String stmtMethod, Expression value){
        return new LambdaExpr(
                helper.createParameters("stmt", "i", "dto"),
                new MethodCallExpr(new NameExpr("stmt"), stmtMethod,
                        nodeList(new NameExpr("i"), value))
        );
    }

    public static Expression generate(Column column, Class<?> fieldClass){
        Class<?> dbType = column.getDbType();
        if( dbType == LocalDate.class && fieldClass == String.class &&
                column.getDtoField().equals("validUpto") ){
            fieldClass = ValidUpto.class;
        }
        Pair key = new Pair(dbType, fieldClass);
        Generator gen = genMap.get(key);
        if( gen == null ){
            String msg = String.format("Cannot generate statement setter: %s -> %s",
                    dbType.getSimpleName(),
                    fieldClass.getSimpleName());
            throw new RuntimeException(msg);
        }
        return gen.generate(column.getDtoField());
    }

}
