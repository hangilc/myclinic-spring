package jp.chang.myclinic.apitool.lib.gentablebase;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.*;
import jp.chang.myclinic.apitool.lib.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

class DtoExtractorGenerator {

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

    private static class ValidUpto {

    }

    private static HashMap<Pair, Function<String, Expression>> genMap = new HashMap<>();

    private static void addMap(Class<?> dbType, Class<?> fieldType, Function<Expression, Expression> expr) {
        genMap.put(new Pair(dbType, fieldType), field -> genLambda(field, expr.apply(getObjectExpr(dbType))));
    }

    static {
        addMap(LocalDate.class, String.class, obj -> new MethodCallExpr(obj, "toString", NodeList.nodeList()));
        addMap(LocalDate.class, ValidUpto.class, obj -> new MethodCallExpr(
                new NameExpr("TableBaseHelper"), "validUptoFromLocalDateToString", NodeList.nodeList(obj)));
        addMap(BigDecimal.class, Double.class, obj -> new MethodCallExpr(
                obj, "doubleValue", NodeList.nodeList()));
        addMap(BigDecimal.class, Integer.class, obj -> new MethodCallExpr(
                obj, "intValue", NodeList.nodeList()));
        addMap(BigDecimal.class, String.class, obj -> new MethodCallExpr(
                obj, "toString", NodeList.nodeList()));
        addMap(String.class, Character.class, obj -> new MethodCallExpr(
                obj, "charAt", NodeList.nodeList(new IntegerLiteralExpr(0))));
        addMap(String.class, Integer.class, obj -> new MethodCallExpr(
                new NameExpr("Integer"), "parseInt", NodeList.nodeList(obj)));
        addMap(LocalDateTime.class, String.class, obj -> new MethodCallExpr(
                new NameExpr("TableBaseHelper"), "localDateTimeToString", NodeList.nodeList(obj)));
        addMap(Integer.class, String.class, obj -> new MethodCallExpr(
                obj, "toString", NodeList.nodeList()));
        addMap(String.class, Double.class, obj -> new MethodCallExpr(
                new NameExpr("TableBaseHelper"), "formatNumber", NodeList.nodeList(obj)));
    }

    private static Helper helper = Helper.getInstance();

    private static Expression genLambda(String fieldName, Expression value) {
        FieldAccessExpr fieldAccess = new FieldAccessExpr(new NameExpr("dto"), fieldName);
        AssignExpr assign = new AssignExpr(fieldAccess, value, AssignExpr.Operator.ASSIGN);
        return new LambdaExpr(helper.createParameters("rs", "i", "dto"), assign);
    }

    private static Expression getObjectExpr(Class<?> dbType) {
        return new MethodCallExpr(new NameExpr("rs"), "getObject", NodeList.nodeList(
                new NameExpr("i"),
                new FieldAccessExpr(new NameExpr(dbType.getSimpleName()), "class")
        ));
    }

    public static Expression generate(Column column, Class<?> fieldType) {
        Class<?> dbType = column.getDbType();
        if (dbType == fieldType) {
            return genLambda(column.getDtoField(), getObjectExpr(dbType));
        } else {
            if (dbType == LocalDate.class && fieldType == String.class &&
                    column.getDtoField().equals("validUpto")) {
                fieldType = ValidUpto.class;
            }
            Pair key = new Pair(dbType, fieldType);
            Function<String, Expression> gen = genMap.get(key);
            if (gen == null) {
                String msg = String.format("Cannto extract dto field: %s -> %s",
                        dbType.getSimpleName(), fieldType.getSimpleName());
                throw new RuntimeException(msg);
            }
            return gen.apply(column.getDtoField());
        }
    }

}
