package jp.chang.myclinic.apitool;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.UnknownType;
import com.google.googlejavaformat.java.Formatter;
import jp.chang.myclinic.apitool.lib.DtoClassList;
import jp.chang.myclinic.apitool.lib.Helper;
import jp.chang.myclinic.apitool.lib.tables.*;
import picocli.CommandLine;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.github.javaparser.ast.Modifier.Keyword;
import static com.github.javaparser.ast.NodeList.nodeList;
import static java.util.stream.Collectors.toList;

@CommandLine.Command(name = "tables")
class Tables implements Runnable {

    @CommandLine.Option(names = {"--table"}, description = "Processes only one table.")
    private String singleTable;

    @CommandLine.Option(names = {"--save"}, description = "Actually saves generated files.")
    private boolean save;

    @CommandLine.Option(names = {"--check-types"}, description = "Checks db-dto type inconsistencies.")
    private boolean checkTypes;

    private Formatter formatter = new Formatter();
    private Helper helper = Helper.getInstance();

    @Override
    public void run() {
        List<Class<?>> dtoClasses;
        if (singleTable != null) {
            Class<?> cls = DtoClassList.getDtoClassByName(singleTable);
            if (cls == null) {
                System.err.printf("Cannot find DTO table: %s\n", singleTable);
                System.exit(1);
            }
            dtoClasses = List.of(cls);
        } else {
            dtoClasses = DtoClassList.getList();
        }
        Config config = new SqliteConfig();
        Supplier<Connection> connSupplier = new SqliteConnectionProvider();
        try (Connection conn = connSupplier.get()) {
            outputTableBases(conn, dtoClasses, config);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void outputTableBases(Connection conn, List<Class<?>> dtoClasses, Config config) throws Exception {
        List<String> errs = new ArrayList<>();
        for (Class<?> dtoClass : dtoClasses) {
            String dtoClassName = dtoClass.getSimpleName();
            String dtoBaseName = dtoClass.getSimpleName().replaceAll("DTO$", "");
            String baseClassName = dtoBaseName + "TableBase";
            CompilationUnit unit = new CompilationUnit();
            unit.setPackageDeclaration(config.basePackage() + ".tablebase");
            unit.addImport("jp.chang.myclinic.backenddb.Column");
            unit.addImport("jp.chang.myclinic.backenddb.Table");
            unit.addImport("jp.chang.myclinic.backenddb.TableBaseHelper");
            unit.addImport("jp.chang.myclinic.backenddb.tableinterface." + dtoBaseName + "TableInterface");
            unit.addImport("java.time.*");
            unit.addImport("java.util.*");
            unit.addImport("java.math.BigDecimal");
            switch (dtoClassName) {
                case "PracticeLogDTO": {
                    unit.addImport("jp.chang.myclinic.logdto.practicelog.PracticeLogDTO");
                    break;
                }
                case "HotlineLogDTO": {
                    unit.addImport("jp.chang.myclinic.logdto.hotline.HotlineLogDTO");
                    break;
                }
                default: {
                    unit.addImport("jp.chang.myclinic.dto." + dtoClassName);
                    break;
                }
            }
            ClassOrInterfaceDeclaration classDecl = unit.addClass(baseClassName);
            classDecl.addExtendedType(new ClassOrInterfaceType(null, new SimpleName("Table"),
                    nodeList(new ClassOrInterfaceType(null, dtoClassName))));
            classDecl.addImplementedType(dtoBaseName + "TableInterface");
            {
                Type fieldType = helper.createGenericType("List", "Column", dtoClassName);
                classDecl.addField(fieldType, "columns", Keyword.PRIVATE, Keyword.STATIC);
            }
            String tableName = config.dtoClassToDbTableName(dtoClass);
            DatabaseMetaData meta = conn.getMetaData();
            Table table = new Table(tableName, meta, config);
            {
                BlockStmt block = classDecl.addStaticInitializer();
                try {
                    List<Expression> args = table.getColumns().stream()
                            .map(c -> generateColumnCreator(tableName, c, dtoClass, config))
                            .collect(toList());
                    block.addStatement(new ExpressionStmt(new AssignExpr(
                            new NameExpr("columns"),
                            new MethodCallExpr(new NameExpr("List"), new SimpleName("of"), nodeList(args)),
                            AssignExpr.Operator.ASSIGN
                    )));
                } catch(GenerateStatementSetterException e){
                    if( checkTypes ){
                        errs.add(e.getRawMessage());
                    } else {
                        throw e;
                    }
                }
            }
            {
                MethodDeclaration methodDecl = classDecl.addMethod("getTableName", Keyword.PUBLIC);
                methodDecl.setType(new ClassOrInterfaceType(null, "String"));
                methodDecl.addAnnotation("Override");
                methodDecl.setBody(new BlockStmt(nodeList(
                        new ReturnStmt(new StringLiteralExpr(table.getTableName()))
                )));
            }
            {
                MethodDeclaration methodDecl = classDecl.addMethod("getClassDTO", Keyword.PROTECTED);
                methodDecl.setType(helper.createGenericType("Class", dtoClassName));
                methodDecl.addAnnotation("Override");
                methodDecl.setBody(new BlockStmt(nodeList(
                        new ReturnStmt(new FieldAccessExpr(new NameExpr(dtoClassName), "class"))
                )));
            }
            {
                MethodDeclaration methodDecl = classDecl.addMethod("getColumns", Keyword.PROTECTED);
                methodDecl.setType(helper.createGenericType("List", "Column", dtoClassName));
                methodDecl.addAnnotation("Override");
                methodDecl.setBody(new BlockStmt(nodeList(
                        new ReturnStmt(new NameExpr("columns"))
                )));
            }
            if( !checkTypes ) {
                String src = formatter.formatSource(unit.toString());
                Path savePath = config.baseDir().resolve("tablebase").resolve(baseClassName + ".java");
                save(savePath, src);
            }
        }
        if( checkTypes ){
            errs.forEach(System.err::println);
            System.exit(0);
        }
    }

    private Expression generateColumnCreator(String tableName, Column column, Class<?> dtoClass, Config config) {
        List<Expression> args = new ArrayList<>();
        args.add(new StringLiteralExpr(column.getDbColumnName()));
        args.add(new StringLiteralExpr(column.getDtoFieldName()));
        args.add(new BooleanLiteralExpr(column.isPrimary()));
        args.add(new BooleanLiteralExpr(column.isAutoIncrement()));
        Class<?> dtoFieldClass = helper.getDTOFieldClass(dtoClass, column.getDtoFieldName());
        args.add(config.generateStatementSetter(
                tableName,
                column.getDbColumnClass(),
                column.getDbColumnName(),
                dtoClass,
                column.getDtoFieldName()));
        args.add(config.generateDtoFieldSetter(column.getDbColumnClass(), dtoFieldClass,
                dtoClass.getSimpleName(), column.getDtoFieldName()));
        return new ObjectCreationExpr(null,
                new ClassOrInterfaceType(null, new SimpleName("Column"), nodeList(new UnknownType())),
                nodeList(args));
    }

    private void save(Path path, String src) {
        System.err.println("saving to: " + path.toString());
        if (save) {
            try {
                Files.write(path, src.getBytes());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        } else {
            System.out.println(src);
        }
    }

}
