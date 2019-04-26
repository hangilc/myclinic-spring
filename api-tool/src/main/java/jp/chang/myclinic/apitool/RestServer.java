package jp.chang.myclinic.apitool;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.Type;
import jp.chang.myclinic.apitool.lib.Helper;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import static com.github.javaparser.ast.NodeList.nodeList;
import static java.util.stream.Collectors.toList;

@Command(name = "rest-server", description = "updates RestServer according to Backend")
public class RestServer implements Runnable {

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
                if (!save) {
                    System.out.println(serverMethod);
                }
            }
            if (save) {
                helper.saveToFile(serverSource, serverUnit.toString(), true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private MethodDeclaration createServerMethod(MethodDeclaration backendMethod) {
        MethodDeclaration method = new MethodDeclaration();
        method.addModifier(Keyword.PUBLIC);
        method.setType(backendMethod.getType());
        method.setName(backendMethod.getName());
        ParamsInfo paramsInfo = addParamsToMethod(method, backendMethod.getParameters());
        String name = backendMethod.getNameAsString();
        method.addAnnotation(createPathAnnotation(name));
        method.addAnnotation(createProducesAnnotation());
        if (paramsInfo.bodyParam != null) {
            method.addAnnotation(createConsumesAnnotation());
        }
        if (name.startsWith("enter")) {
            method.addAnnotation(new MarkerAnnotationExpr("POST"));
            AutoIncInfo autoIncInfo = new AutoIncInfo();
            if (paramsInfo.bodyParam != null && isAutoIncMethod(method, paramsInfo.bodyParam, autoIncInfo)) {
                method.setType(autoIncInfo.autoIncField.getType().getSimpleName());
                method.setBody(createAutoIncBlock(name, paramsInfo.bodyParam.getNameAsString(),
                        autoIncInfo.autoIncField.getName()));
            } else {
                if (method.getType().isVoidType()) {
                    method.setBody(createVoidBlock(name, method.getParameters()));
                } else {
                    method.setBody(createBlock("tx", name, method.getParameters()));
                }
            }
        } else if (name.startsWith("get") || name.startsWith("list") ||
                name.startsWith("search") || name.startsWith("find") ||
                name.startsWith("count") || name.startsWith("resolve") ||
                name.startsWith("batchResolve")) {
            if (paramsInfo.bodyParam != null) {
                method.addAnnotation(new MarkerAnnotationExpr("POST"));
            } else {
                method.addAnnotation(new MarkerAnnotationExpr("GET"));
            }
            method.setBody(createBlock("query", name, method.getParameters()));
        } else {
            if (name.startsWith("delete") || name.startsWith("update") ||
                    method.getType().isVoidType() ||
                    backendMethod.isAnnotationPresent("ServerMethodPost")) {
                method.addAnnotation(new MarkerAnnotationExpr("POST"));
            } else {
                if (paramsInfo.bodyParam != null) {
                    method.addAnnotation(new MarkerAnnotationExpr("POST"));
                } else {
                    method.addAnnotation(new MarkerAnnotationExpr("GET"));
                }
            }
            if (method.getType().isVoidType()) {
                method.setBody(createVoidBlock(name, method.getParameters()));
            } else {
                method.setBody(createBlock("tx", name, method.getParameters()));
            }
        }
        return method;
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

    private static class ParamsInfo {
        Parameter bodyParam = null;
    }

    private ParamsInfo addParamsToMethod(MethodDeclaration method, Collection<Parameter> parameters) {
        ParamsInfo info = new ParamsInfo();
        for (Parameter param : parameters) {
            Parameter p = new Parameter(param.getType(), param.getName());
            if (isComplexType(param.getType())) {
                if (info.bodyParam != null) {
                    System.err.println("Cannot add parameters: " + method.toString() + " ; " + parameters);
                    System.exit(1);
                }
                info.bodyParam = p;
            } else {
                AnnotationExpr paramAnnot = new SingleMemberAnnotationExpr(new Name("QueryParam"),
                        new StringLiteralExpr(helper.toHyphenChain(param.getNameAsString())));
                p.addAnnotation(paramAnnot);
            }
            method.addParameter(p);
        }
        return info;
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
        //private Class<?> dtoClass;
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
                    //info.dtoClass = dtoClass;
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

    private BlockStmt createBlock(String backendFunc, String methodName, List<Parameter> params) {
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

    private BlockStmt createVoidBlock(String methodName, List<Parameter> params) {
        Expression backendCallExpr = new MethodCallExpr(new NameExpr("backend"), methodName,
                nodeList(params.stream().map(para -> new NameExpr(para.getNameAsString())).collect(toList())));
        LambdaExpr lambda = new LambdaExpr(helper.createSingleLambdaParameter("backend"),
                backendCallExpr);
        return new BlockStmt(nodeList(new ExpressionStmt(new MethodCallExpr(
                new NameExpr("dbBackend"),
                "txProc",
                nodeList(lambda)
        ))));
    }

}