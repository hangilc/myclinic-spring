package jp.chang.myclinic.apitool.lib.gentablebase;

import com.github.javaparser.ast.CompilationUnit;

import static com.github.javaparser.ast.Modifier.Keyword;
import static java.util.stream.Collectors.toList;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;

import static com.github.javaparser.ast.NodeList.nodeList;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.UnknownType;
import jp.chang.myclinic.apitool.databasespecifics.DatabaseSpecifics;
import jp.chang.myclinic.apitool.lib.Helper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TableBaseGenerator {

    private Table table;
    private DatabaseSpecifics dbSpecs;
    private String basePackage;
    private Class<?> dtoClass;
    private String dtoClassName;
    private Helper helper = Helper.getInstance();

    public TableBaseGenerator(Table table, DatabaseSpecifics dbSpecs) {
        this.table = table;
        this.dbSpecs = dbSpecs;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public CompilationUnit generate() {
        CompilationUnit unit = new CompilationUnit();
        unit.setPackageDeclaration(basePackage + ".tablebase");
        List.of(basePackage + ".Column",
                basePackage + ".Table",
                basePackage + ".tablebasehelper.TableBaseHelper",
                "jp.chang.myclinic.dto.*",
                "jp.chang.myclinic.logdto.practicelog.*",
                "java.sql.Connection",
                "java.time.*",
                "java.util.*",
                "java.math.BigDecimal"
        ).forEach(unit::addImport);
        String tableClassName = helper.snakeToCapital(table.getName()) + "TableBase";
        this.dtoClass = dbSpecs.mapTableNameToDtoClass(table.getName());
        this.dtoClassName = dtoClass.getSimpleName();
        ClassOrInterfaceDeclaration classDecl = addClass(unit, tableClassName);
        declareColumnField(classDecl);
        addColumnInitializer(classDecl, table.getColumns(), table.getName());
        addGetTableNameMethod(classDecl, table.getName());
        addGetClassDtoMethod(classDecl, dtoClassName);
        addGetColumnsMethod(classDecl, dtoClassName);
        return unit;
    }

    private ClassOrInterfaceDeclaration addClass(CompilationUnit unit, String className) {
        ClassOrInterfaceDeclaration classDecl = unit.addClass(className, Modifier.Keyword.PUBLIC);
        classDecl.addExtendedType(helper.createGenericType("Table", dtoClassName));
        return classDecl;
    }

    private void declareColumnField(ClassOrInterfaceDeclaration classDecl) {
        Type fieldType = helper.createGenericType("List", "Column", dtoClassName);
        classDecl.addField(fieldType, "columns", Keyword.PRIVATE, Keyword.STATIC);
    }

    private void addColumnInitializer(ClassOrInterfaceDeclaration classDecl,
                                      List<Column> columns, String tableName) {
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
        args.add(createParamSetter(column));
        args.add(createFieldExtractor(column));
        return new ObjectCreationExpr(null, helper.createGenericType("Column", new UnknownType()),
                NodeList.nodeList(args));
    }

    private Expression createParamSetter(Column column) {
        Class<?> dtoFieldType = getDTOFieldClass(column.getDtoField());
        return StatementSetterGenerator.generate(column, dtoFieldType);
    }

    private Expression createFieldExtractor(Column column){
        Class<?> dtoFieldType = getDTOFieldClass(column.getDtoField());
        return DtoExtractorGenerator.generate(column, dtoFieldType);
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

    private void addGetTableNameMethod(ClassOrInterfaceDeclaration classDecl, String tableName){
        MethodDeclaration methodDecl = classDecl.addMethod("getTableName", Keyword.PROTECTED);
        methodDecl.setType(new ClassOrInterfaceType(null, "String"));
        methodDecl.addAnnotation("Override");
        methodDecl.setBody(new BlockStmt(nodeList(
                new ReturnStmt(new StringLiteralExpr(tableName))
        )));
    }

    private void addGetClassDtoMethod(ClassOrInterfaceDeclaration classDecl, String dtoClassName){
        MethodDeclaration methodDecl = classDecl.addMethod("getClassDTO", Keyword.PROTECTED);
        methodDecl.setType(helper.createGenericType("Class", dtoClassName));
        methodDecl.addAnnotation("Override");
        methodDecl.setBody(new BlockStmt(nodeList(
                new ReturnStmt(new FieldAccessExpr(new NameExpr(dtoClassName), "class"))
        )));
    }

    private void addGetColumnsMethod(ClassOrInterfaceDeclaration classDecl, String dtoClassName){
        MethodDeclaration methodDecl = classDecl.addMethod("getColumns", Keyword.PROTECTED);
        methodDecl.setType(helper.createGenericType("List", "Column", dtoClassName));
        methodDecl.addAnnotation("Override");
        methodDecl.setBody(new BlockStmt(nodeList(
                new ReturnStmt(new NameExpr("columns"))
        )));
    }

}
