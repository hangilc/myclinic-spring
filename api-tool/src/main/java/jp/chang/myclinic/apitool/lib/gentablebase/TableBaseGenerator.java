package jp.chang.myclinic.apitool.lib.gentablebase;

import com.github.javaparser.ast.CompilationUnit;

import static com.github.javaparser.ast.Modifier.Keyword;
import static java.util.stream.Collectors.toList;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;

import static com.github.javaparser.ast.NodeList.nodeList;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.UnknownType;
import jp.chang.myclinic.apitool.lib.Helper;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
//        args.add(new ColumnMapperGenerator(column.getDtoField(), column.getDbType(),
//                column.getName(), getDTOFieldClass(column.getDtoField()), tableName).generate());
        return new ObjectCreationExpr(null, helper.createGenericType("Column", new UnknownType()),
                NodeList.nodeList(args));
    }

    private Expression createParamSetter(Column column) {
        return new LambdaExpr(helper.createParameters("stmt", "i", "dto"),
                stmtSetterExpr(column.getDbType(), column.getDtoField()));
    }

    private Expression stmtSetterExpr(Class<?> dbType, String dtoFieldName) {
        Class<?> dtoFieldType = getDTOFieldClass(dtoFieldName);
        Expression index = new NameExpr("i");
        FieldAccessExpr value = new FieldAccessExpr(new NameExpr("dto"), dtoFieldName);
        if (dbType == Integer.class) {
            if (dtoFieldType == Integer.class) {
                return new MethodCallExpr(new NameExpr("stmt"), "setInt", nodeList(index, value));
            } else if( dtoFieldType == String.class ){
                // stmt.setInt(i, Integer.parseInt(dto.field))
                Expression cvt = new MethodCallExpr(new NameExpr("Integer"), "parseInt", nodeList(value));
                return new MethodCallExpr(new NameExpr("stmt"), "setInt", nodeList(index, cvt));
            }
        } else if(dbType == String.class){
            if (dtoFieldType == String.class) {
                return new MethodCallExpr(new NameExpr("stmt"), "setString", nodeList(index, value));
            } else if( dtoFieldType == Character.class ){
                // stmt.setString(i, String.valueOf(dto.field)
                Expression cvt = new MethodCallExpr(new NameExpr("String"), "valueOf", nodeList(value));
                return new MethodCallExpr(new NameExpr("stmt"), "setString", nodeList(index, cvt));
            }
        } else if( dbType == LocalDate.class ){
            if( dtoFieldType == String.class ){
                if( dtoFieldName.equals("validUpto") ) {
                    // stmt.setObject(i, TableBaseHelper.validUptoFromStringToLocalDate(dto.field)
                    Expression cvt = new MethodCallExpr(new NameExpr("TableBaseHelper"),
                            "validUptoFromStringToLocalDate", nodeList(value));
                    return new MethodCallExpr(new NameExpr("stmt"), "setObject", nodeList(index, cvt));
                } else {
                    // stmt.setObject(i, LocalDate.parse(dto.field)
                    Expression cvt = new MethodCallExpr(new NameExpr("LocalDate"), "parse", nodeList(value));
                    return new MethodCallExpr(new NameExpr("stmt"), "setObject", nodeList(index, cvt));
                }
            }
        } else if( dbType == BigDecimal.class ) {
            if( dtoFieldType == Double.class || dtoFieldType == String.class || dtoFieldType == Integer.class ){
                // stmt.setBigDecimal(i, new BigDecimal(dto.field))
                Expression cvt = new ObjectCreationExpr(
                        null, new ClassOrInterfaceType(null, "BigDecimal"), nodeList(value));
                return new MethodCallExpr(new NameExpr("stmt"), "setBigDecimal", nodeList(index, cvt));
            }
        } else if( dbType == LocalDateTime.class ){
            if( dtoFieldType == String.class ){
                // stmt.setObject(i, TableBaseHelper.stringToLocalDateTime(dto.field)
                Expression cvt = new MethodCallExpr(new NameExpr("TableBaseHelper"),
                        "stringToLocalDateTime", nodeList(value));
                return new MethodCallExpr(new NameExpr("stmt"), "setObject", nodeList(index, cvt));
            }
        }
        String msg = String.format("Cannot create parameter setter: %s, %s",
                dbType.getSimpleName(), dtoFieldType.getSimpleName());
        throw new RuntimeException(msg);
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

}
