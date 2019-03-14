package jp.chang.myclinic.apitool.pgsqltables;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.WildcardType;
import jp.chang.myclinic.apitool.lib.Helper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.github.javaparser.ast.Modifier.Keyword;
import static com.github.javaparser.ast.NodeList.nodeList;
import static java.util.stream.Collectors.toList;

public class SourceCodeGenerator {

    private CompilationUnit unit;
    private ClassOrInterfaceDeclaration classDecl;
    private String className;
    private String dtoClassName;
    private Class<?> dtoClass;
    private Helper helper = new Helper();
    private static Path baseDir = Paths.get("backend-pgsql/src/main/java/jp/chang/myclinic/backendpgsql/tablebase");

    CompilationUnit create(Table table, Class<?> dtoClass) {
        this.unit = new CompilationUnit();
        setPackage("jp.chang.myclinic.backendpgsql.tablebase");
        List.of("jp.chang.myclinic.backendpgsql.Column",
                "jp.chang.myclinic.backendpgsql.Table",
                "jp.chang.myclinic.dto.*",
                "jp.chang.myclinic.logdto.practicelog.*",
                "java.sql.Connection",
                "java.time.*",
                "java.util.*",
                "java.math.BigDecimal",
                "jp.chang.myclinic.backendpgsql.tablebasehelper.TableBaseHelper"
        ).forEach(this::addImport);
        this.className = helper.snakeToCapital(table.getName()) + "TableBase";
        this.dtoClassName = dtoClass.getSimpleName();
        this.dtoClass = dtoClass;
        addClass(className);
        declareColumnField();
        addColumnInitializer(table.getColumns(), table.getName());
        addGetTableName(table.getName());
        addGetClassDTO();
        addGetColumns();
        return unit;
    }

    void save(String src) {
        Path path = baseDir.resolve(className + ".java");
        System.out.println(path);
        try {
            Files.write(path, src.getBytes());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void addGetColumns() {
        MethodDeclaration method = classDecl.addMethod("getColumns", Keyword.PROTECTED);
        method.setType(createGenericType("List", "Column", dtoClassName));
        method.addAnnotation(new MarkerAnnotationExpr("Override"));
        Statement stmt = StaticJavaParser.parseStatement("return columns;");
        method.setBody(new BlockStmt(nodeList(stmt)));

    }

    private void addGetClassDTO() {
        MethodDeclaration method = classDecl.addMethod("getClassDTO", Keyword.PROTECTED);
        method.setType(createGenericType("Class", dtoClassName));
        method.addAnnotation(new MarkerAnnotationExpr("Override"));
        Statement stmt = StaticJavaParser.parseStatement(
                String.format("return %s.class;", dtoClassName));
        method.setBody(new BlockStmt(nodeList(stmt)));
    }

    private void addGetTableName(String tableName) {
        MethodDeclaration method = classDecl.addMethod("getTableName", Keyword.PROTECTED);
        method.setType(new ClassOrInterfaceType(null, "String"));
        method.addAnnotation(new MarkerAnnotationExpr("Override"));
        Statement stmt = StaticJavaParser.parseStatement(
                String.format("return \"%s\";", tableName));
        method.setBody(new BlockStmt(nodeList(stmt)));
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

    private void addColumnInitializer(List<Column> columns, String tableName) {
        List<Expression> args = columns.stream()
                .map(c -> columnCreator(c, tableName))
                .collect(toList());
        MethodCallExpr methodCall = new MethodCallExpr(new NameExpr("List"), "of",
                NodeList.nodeList(args));
        AssignExpr assignExpr = new AssignExpr(new NameExpr("columns"), methodCall,
                AssignExpr.Operator.ASSIGN);
        BlockStmt block = classDecl.addStaticInitializer();
        block.addStatement(new ExpressionStmt(assignExpr));
    }

    private Expression columnCreator(Column column, String tableName) {
        List<Expression> args = new ArrayList<>();
        args.add(new StringLiteralExpr(column.getName()));
        args.add(new BooleanLiteralExpr(column.isPrimary()));
        args.add(new BooleanLiteralExpr(column.isAutoIncrement()));
        args.add(new ColumnMapperGenerator(column.getDTOField(), column.getJdbcType(),
                column.getName(), getDTOFieldClass(column.getDTOField()), tableName).generate());
        args.add(createColumnGetter(column.getDTOField()));
        return new ObjectCreationExpr(null, createGenericType("Column", dtoClassName),
                NodeList.nodeList(args));
    }

    private Expression createColumnGetter(String dtoFieldName) {
        String src = String.format("dto -> dto.%s", dtoFieldName);
        return StaticJavaParser.parseExpression(src);
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
            String msg = String.format("Cannot find %s in %s", name, dtoClass.getSimpleName());
            throw new RuntimeException(msg);
        }
    }

    private ClassOrInterfaceType createGenericType(String name, String paramType) {
        return new ClassOrInterfaceType(null, new SimpleName(name),
                NodeList.nodeList(new ClassOrInterfaceType(null, paramType)));
    }

    private ClassOrInterfaceType createGenericType(String name, Type paramType) {
        return new ClassOrInterfaceType(null, new SimpleName(name),
                NodeList.nodeList(paramType));
    }

    private ClassOrInterfaceType createGenericType(String name, String paramType1, String paramType2) {
        return createGenericType(name, createGenericType(paramType1, paramType2));
    }

}
