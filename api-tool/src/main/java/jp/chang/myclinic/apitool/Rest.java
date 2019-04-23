package jp.chang.myclinic.apitool;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Modifier.*;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.Type;
import jp.chang.myclinic.apitool.lib.Helper;
import picocli.CommandLine.*;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.github.javaparser.ast.NodeList.nodeList;
import static java.util.stream.Collectors.toList;

@Command(name = "rest")
public class Rest implements Runnable {

    @Option(names = "--save")
    private boolean save;

    private Helper helper = Helper.getInstance();

    @Override
    public void run() {
        try {
            Path backendSource = helper.getBackendSourcePath();
            CompilationUnit backendUnit = StaticJavaParser.parse(backendSource);
            ClassOrInterfaceDeclaration backendDecl = backendUnit.getClassByName("Backend")
                    .orElseThrow(() -> new RuntimeException("Cannot find class Backend."));
            Path serverSource = helper.getBackendServerSourcePath();
            CompilationUnit serverUnit = StaticJavaParser.parse(serverSource);
            ClassOrInterfaceDeclaration serverDecl = serverUnit.getClassByName("RestServer")
                    .orElseThrow(() -> new RuntimeException("Cannot find class RestServer."));
            List<MethodDeclaration> unimplementedMethods = helper.listUnimplementedMethods(backendDecl, serverDecl);
            for (MethodDeclaration backendMethodDecl : unimplementedMethods) {
                MethodDeclaration serverMethod = createServerMethod(backendMethodDecl);
                serverDecl.addMember(serverMethod);
                if( !save ) {
                    System.out.println(serverMethod);
                }
            }
            if( save ){
                helper.saveToFile(serverSource, serverUnit.toString(), true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private boolean isComplexType(Type type) {
        if (type.isPrimitiveType()) {
            return false;
        } else if (type.isClassOrInterfaceType()) {
            switch (type.toString()) {
                case "String":
                case "LocalDate":
                case "LocalDateTime": {
                    return false;
                }
                default: {
                    return true;
                }
            }
        } else {
            return true;
        }
    }

    private MethodDeclaration createServerMethod(MethodDeclaration backendMethod) {
        MethodDeclaration method = new MethodDeclaration();
        method.addModifier(Keyword.PUBLIC);
        method.setType(backendMethod.getType());
        method.setName(backendMethod.getName());
        Parameter complexParam = null;
        for (Parameter param : backendMethod.getParameters()) {
            Parameter p = new Parameter(param.getType(), param.getName());
            method.addParameter(p);
            Type t = p.getType();
            if (isComplexType(t)) {
                if (complexParam != null) {
                    System.err.println("Cannot handle method: " + backendMethod);
                    System.exit(1);
                } else {
                    complexParam = p;
                }
            }
        }
        String name = backendMethod.getNameAsString();
        method.addAnnotation(createPathAnnotation(name));
        method.addAnnotation(createProducesAnnotation());
        if (name.startsWith("enter")) {
            method.addAnnotation(new MarkerAnnotationExpr("POST"));
            AutoIncInfo autoIncInfo = new AutoIncInfo();
            if (isAutoIncMethod(method, complexParam, autoIncInfo)) {
                method.addAnnotation(createConsumesAnnotation());
                method.setType(autoIncInfo.autoIncField.getType().getSimpleName());
                method.setBody(createAutoIncBlock(name, complexParam.getNameAsString(),
                        autoIncInfo.autoIncField.getName()));
            } else {
                if (method.getType().isVoidType()) {
                    method.setBody(createVoidBlock("txProc", name, method.getParameters()));
                } else {
                    method.setBody(createBlock("tx", name, method.getParameters()));
                }
            }
        } else if(name.startsWith("get") || name.startsWith("list") ||
                name.startsWith("search") || name.startsWith("find") ||
                name.startsWith("count") || name.startsWith("resolve") ||
                name.startsWith("batchResolve")){
            method.addAnnotation(new MarkerAnnotationExpr("GET"));
            method.setBody(createBlock("query", name, method.getParameters()));
        } else {
            method.addAnnotation(new MarkerAnnotationExpr("GET"));
            if( method.getType().isVoidType() ){
                method.setBody(createVoidBlock("txProc", name, method.getParameters()));
            } else {
                method.setBody(createBlock("tx", name, method.getParameters()));
            }
        }
        return method;
    }

    private AnnotationExpr createPathAnnotation(String methodName) {
        return new SingleMemberAnnotationExpr(new Name("Path"),
                new StringLiteralExpr(helper.toHyphenChain(methodName)));
    }

    private AnnotationExpr createProducesAnnotation() {
        return new SingleMemberAnnotationExpr(new Name("Produces"),
                new FieldAccessExpr(new NameExpr("MediaType"), "APPLICATION_JSON"));
    }

    private AnnotationExpr createConsumesAnnotation() {
        return new SingleMemberAnnotationExpr(new Name("Consumes"),
                new FieldAccessExpr(new NameExpr("MediaType"), "APPLICATION_JSON"));
    }

    private static class AutoIncInfo {
        private Class<?> dtoClass;
        private Field autoIncField;
    }

    private boolean isAutoIncMethod(MethodDeclaration m, Parameter complexParam, AutoIncInfo info) {
        if (m.getType().isVoidType() && complexParam != null) {
            Class<?> dtoClass = helper.getDtoClassNamed(complexParam.getType().toString());
            if (dtoClass != null) {
                List<Field> autoIncFields = helper.getAutoIncs(dtoClass);
                if (autoIncFields.size() >= 2) {
                    System.err.println("Too many autoinc fields: " + m);
                    System.exit(1);
                } else if (autoIncFields.size() == 1) {
                    info.dtoClass = dtoClass;
                    info.autoIncField = autoIncFields.get(0);
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    private BlockStmt createAutoIncBlock(String methodName, String paramName, String fieldName) {
        Expression backendCallExpr = new MethodCallExpr(new NameExpr("backend"), methodName,
                nodeList(new NameExpr(paramName)));
        LambdaExpr lambda = new LambdaExpr(helper.createSingleLambdaParameter("backend"),
                new BlockStmt(nodeList(
                        new ExpressionStmt(backendCallExpr),
                        new ReturnStmt(new FieldAccessExpr(new NameExpr(paramName), fieldName))
                )));
        return new BlockStmt(nodeList(new ReturnStmt(new MethodCallExpr(
                new NameExpr("dbBackend"),
                "tx",
                nodeList(lambda)
        ))));
    }

    private BlockStmt createBlock(String backendFunc, String methodName, List<Parameter> params){
        Expression backendCallExpr = new MethodCallExpr(new NameExpr("backend"), methodName,
                nodeList(params.stream().map(para -> new NameExpr(para.getNameAsString())).collect(toList())));
        LambdaExpr lambda = new LambdaExpr(helper.createSingleLambdaParameter("backend"),
                backendCallExpr);
        return new BlockStmt(nodeList(new ReturnStmt(new MethodCallExpr(
                new NameExpr("dbBackend"),
                backendFunc,
                nodeList(lambda)
        ))));
    }

    private BlockStmt createVoidBlock(String backendFunc, String methodName, List<Parameter> params){
        Expression backendCallExpr = new MethodCallExpr(new NameExpr("backend"), methodName,
                nodeList(params.stream().map(para -> new NameExpr(para.getNameAsString())).collect(toList())));
        LambdaExpr lambda = new LambdaExpr(helper.createSingleLambdaParameter("backend"),
                backendCallExpr);
        return new BlockStmt(nodeList(new ExpressionStmt(new MethodCallExpr(
                new NameExpr("dbBackend"),
                backendFunc,
                nodeList(lambda)
        ))));
    }

}
