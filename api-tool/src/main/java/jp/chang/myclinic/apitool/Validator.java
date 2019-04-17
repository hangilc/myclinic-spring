package jp.chang.myclinic.apitool;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.expr.AssignExpr.Operator;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import jp.chang.myclinic.apitool.lib.DtoClassList;
import jp.chang.myclinic.apitool.lib.Helper;
import picocli.CommandLine.*;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static com.github.javaparser.ast.NodeList.nodeList;

@Command(name = "validator", description = "Creates validator.")
public class Validator implements Runnable {

    @Parameters(paramLabel = "DTO class name", arity = "1")
    private String dtoClassName;

    @Option(names = "--save")
    private boolean save;

    private Helper helper = Helper.getInstance();

    @Override
    public void run() {
        CompilationUnit unit = new CompilationUnit();
        unit.setPackageDeclaration("jp.chang.myclinic.util.validator.dto");
        unit.addImport(new ImportDeclaration("jp.chang.myclinic.util.validator.Validated", false, false));
        unit.addImport(new ImportDeclaration("jp.chang.myclinic.util.validator.Validated.success", true, false));
        unit.addImport(new ImportDeclaration("jp.chang.myclinic.util.validator.Validators", true, true));
        unit.addImport(new ImportDeclaration("jp.chang.myclinic.dto." + dtoClassName  , false, false));
        Class<?> dtoClass = DtoClassList.getDtoClassByName(dtoClassName);
        String validatorClassName = dtoClassName.replaceAll("DTO$", "") + "Validator";
        ClassOrInterfaceDeclaration classDecl = unit.addClass(validatorClassName);
        for (Field field : Objects.requireNonNull(dtoClass).getFields()) {
            validatedFieldDecl(classDecl, field);
        }
        for (Field field : dtoClass.getFields()) {
            validatorMethod(classDecl, field);
        }
        validateMethod(classDecl, dtoClass);
        Formatter formatter = new Formatter();
        try {
            String output = formatter.formatSource(unit.toString());
            if (save) {
                Path savePath = Paths.get("myclinic-util/src/main/java/jp/chang/myclinic/util/validator/dto",
                        validatorClassName + ".java");
                if( Files.exists(savePath) ){
                    System.err.printf("%s already exists.", savePath.toString());
                    System.exit(1);
                }
                try {
                    Files.write(savePath, output.getBytes());
                } catch(IOException e){
                    throw new UncheckedIOException(e);
                }
            } else {
                System.out.println(output);
            }
        } catch (FormatterException e) {
            throw new RuntimeException(e);
        }
    }

    private void validatedFieldDecl(ClassOrInterfaceDeclaration classDecl, Field field) {
        Class<?> fieldClass = helper.getBoxedClass(field.getType());
        ClassOrInterfaceType validatedType = helper.createGenericType("Validated", fieldClass.getSimpleName());
        String fieldName = "validated" + helper.capitalize(field.getName());
        classDecl.addField(validatedType, fieldName, Keyword.PRIVATE);
    }

    private void validatorMethod(ClassOrInterfaceDeclaration classDecl, Field field) {
        String methodName = "validate" + helper.snakeToCapital(field.getName());
        MethodDeclaration methodDecl = classDecl.addMethod(methodName, Keyword.PUBLIC);
        methodDecl.setType(classDecl.getNameAsString());
        methodDecl.addParameter(field.getType().getSimpleName(), field.getName());
        String fieldName = "validated" + helper.capitalize(field.getName());
        Expression successCall = new MethodCallExpr(null, "success",
                nodeList(new NameExpr(field.getName())));
        Expression assignExpr = new AssignExpr(new FieldAccessExpr(new NameExpr("this"), fieldName),
                successCall, Operator.ASSIGN);
        Statement stmt = new ExpressionStmt(assignExpr);
        Statement returnThis = new ReturnStmt(new NameExpr("this"));
        methodDecl.setBody(new BlockStmt(nodeList(stmt, returnThis)));
    }

    private void validateMethod(ClassOrInterfaceDeclaration classDecl, Class<?> dtoClass) {
        MethodDeclaration methodDecl = classDecl.addMethod("validate", Keyword.PUBLIC);
        methodDecl.setType(helper.createGenericType("Validated", dtoClass.getSimpleName()));
        Expression newInstanceExpr = new ObjectCreationExpr(null,
                new ClassOrInterfaceType(null, dtoClass.getSimpleName()), nodeList());
        MethodCallExpr call = new MethodCallExpr("success", newInstanceExpr);
        for (Field field : dtoClass.getFields()) {
            Expression assignLambda = new LambdaExpr(helper.createParameters("dto", field.getName()),
                    new AssignExpr(
                            new FieldAccessExpr(new NameExpr("dto"), field.getName()),
                            new NameExpr(field.getName()),
                            Operator.ASSIGN
                    ));
            Statement returnThis = new ReturnStmt(new NameExpr("this"));
            call = new MethodCallExpr(call, "extend", nodeList(new StringLiteralExpr(field.getName()),
                    new NameExpr("validated" + helper.capitalize(field.getName())),
                    assignLambda));
        }
        Statement returnStmt = new ReturnStmt(call);
        methodDecl.setBody(new BlockStmt(nodeList(returnStmt)));
    }
}
