package jp.chang.myclinic.apitool.pgsqltables;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import jp.chang.myclinic.apitool.lib.Helper;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.github.javaparser.ast.Modifier.Keyword;
import static java.util.stream.Collectors.toList;

public class SourceCodeGenerator {

    private CompilationUnit unit;
    private ClassOrInterfaceDeclaration classDecl;
    private String className;
    private String dtoClassName;
    private Class<?> dtoClass;
    private Helper helper = new Helper();

    CompilationUnit create(Table table, Class<?> dtoClass) {
        this.unit = new CompilationUnit();
        setPackage("jp.chang.myclinic.backendpgsql.tablebase");
        List.of("jp.chang.myclinic.backendpgsql.Column",
                "jp.chang.myclinic.backendpgsql.Table",
                "jp.chang.myclinic.dto.*",
                "java.sql.Connection",
                "java.time.*",
                "java.util.*"
        ).forEach(this::addImport);
        this.className = helper.snakeToCapital(table.getName()) + "TableBase";
        this.dtoClassName = dtoClass.getSimpleName();
        this.dtoClass = dtoClass;
        addClass(className);
        declareColumnField();
        addColumnInitializer(table.getColumns());
        return unit;
    }

    private void setPackage(String packageName) {
        unit.setPackageDeclaration(packageName);
    }

    private void addImport(String importName) {
        unit.addImport(importName);
    }

    private void addClass(String className) {
        this.classDecl = unit.addClass(className, Keyword.PUBLIC);
        classDecl.addExtendedType(createGenericType("Table", dtoClassName));
    }

    private void declareColumnField() {
        Type fieldType = createGenericType("List", createGenericType("Column", dtoClassName));
        classDecl.addField(fieldType, "columns", Keyword.PRIVATE, Keyword.STATIC);
    }

    private void addColumnInitializer(List<Column> columns) {
        List<Expression> args = columns.stream()
                .map(this::columnCreator)
                .collect(toList());
        MethodCallExpr methodCall = new MethodCallExpr(new NameExpr("List"), "of",
                NodeList.nodeList(args));
        AssignExpr assignExpr = new AssignExpr(new NameExpr("columns"), methodCall,
                AssignExpr.Operator.ASSIGN);
        BlockStmt block = classDecl.addStaticInitializer();
        block.addStatement(new ExpressionStmt(assignExpr));
    }

    private Expression columnCreator(Column column) {
        List<Expression> args = new ArrayList<>();
        args.add(new StringLiteralExpr(column.getName()));
        args.add(new BooleanLiteralExpr(column.isPrimary()));
        args.add(new BooleanLiteralExpr(column.isAutoIncrement()));
        args.add(createSetterLambda(column));
        return new ObjectCreationExpr(null, createGenericType("Column", dtoClassName),
                NodeList.nodeList(args));
    }

    private Class<?> getDTOFieldClass(String name) {
        try {
            Field field = dtoClass.getField(name);
            Class<?> c = field.getType();
            if (c == int.class) {
                return Integer.class;
            } else if (c == double.class) {
                return Double.class;
            } else if (c == char.class) {
                return Character.class;
            } else {
                return c;
            }
        } catch (NoSuchFieldException e) {
            System.err.println(name);
            throw new RuntimeException(e);
        }
    }

    private Expression createSetterLambda(Column column) {
        Class<?> sqlType = column.getJdbcType();
        Class<?> dtoFieldType = getDTOFieldClass(column.getDtoField());
        Expression lefthandExpr = new FieldAccessExpr(new NameExpr("dto"), column.getDtoField());
        Expression value;
        System.out.println(sqlType);
        System.out.println(dtoFieldType);
        if (sqlType == dtoFieldType) {
            value = new CastExpr(new ClassOrInterfaceType(null, dtoFieldType.getSimpleName()),
                    new NameExpr("o"));
        } else if( dtoFieldType == LocalDate.class ){
            value = new BooleanLiteralExpr(true);
        } else {
            value = new BooleanLiteralExpr(true);
        }
        AssignExpr assignExpr = new AssignExpr(lefthandExpr, value, AssignExpr.Operator.ASSIGN);
        ExpressionStmt assignStmt = new ExpressionStmt(assignExpr);
        return new LambdaExpr(
                NodeList.nodeList(new Parameter(new ClassOrInterfaceType(null, "Object"), "o"),
                        new Parameter(new ClassOrInterfaceType(null, dtoClassName), "dto")),
                new BlockStmt(NodeList.nodeList(assignStmt)));
    }

    private ClassOrInterfaceType createGenericType(String name, String paramType) {
        return new ClassOrInterfaceType(null, new SimpleName(name),
                NodeList.nodeList(new ClassOrInterfaceType(null, paramType)));
    }

    private ClassOrInterfaceType createGenericType(String name, Type paramType) {
        return new ClassOrInterfaceType(null, new SimpleName(name),
                NodeList.nodeList(paramType));
    }

}
