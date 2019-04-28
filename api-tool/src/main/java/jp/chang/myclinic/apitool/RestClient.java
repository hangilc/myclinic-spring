package jp.chang.myclinic.apitool;

import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.DataKey;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.NodeList;

import static com.github.javaparser.ast.NodeList.*;
import static java.util.stream.Collectors.toList;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import jp.chang.myclinic.apitool.lib.Helper;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.lang.annotation.Annotation;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Command(name = "rest-client", description = "Updates FrontendRest according to Backend")
public class RestClient implements Runnable {

    @Option(names = "--save")
    private boolean save;

    private Helper helper = Helper.getInstance();

    @Override
    public void run() {
        try {
            Path serverSource = helper.getBackendServerSourcePath();
            CompilationUnit backendUnit = StaticJavaParser.parse(serverSource);
            ClassOrInterfaceDeclaration serverDecl = backendUnit.getClassByName("RestServer")
                    .orElseThrow(() -> new RuntimeException("Cannot find class RestServer."));
            Path clientSource = helper.getFrontendRestSourcePath();
            CompilationUnit clientUnit = StaticJavaParser.parse(clientSource);
            ClassOrInterfaceDeclaration clientDecl = clientUnit.getClassByName("FrontendRest")
                    .orElseThrow(() -> new RuntimeException("Cannot find class FrontendRest."));
            List<MethodDeclaration> unimplementedMethods = helper.listUnimplementedMethods(serverDecl, clientDecl);
            for (MethodDeclaration backendMethod : unimplementedMethods) {
                MethodDeclaration clientMethod = createClientMethod(backendMethod);
                clientDecl.addMember(clientMethod);
                if( !save ) {
                    System.out.println(clientMethod);
                }
            }
            if( save ){
                helper.saveToFile(clientSource, helper.formatSource(clientUnit.toString()), true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private MethodDeclaration createClientMethod(MethodDeclaration backendMethod) {
        MethodDeclaration method = new MethodDeclaration();
        method.setName(backendMethod.getNameAsString());
        method.setModifiers(Keyword.PUBLIC);
        method.addAnnotation(new MarkerAnnotationExpr("Override"));
        method.setType(helper.createGenericType("CompletableFuture", helper.getBoxedType(backendMethod.getType())));
        for (Parameter param : backendMethod.getParameters()) {
            Parameter p = new Parameter(param.getType(), param.getName());
            method.addParameter(p);
        }
        if (backendMethod.isAnnotationPresent("GET")) {
            method.setBody(createGetBody(backendMethod));
        } else if (backendMethod.isAnnotationPresent("POST")) {
            method.setBody(createPostBody(backendMethod));
        } else {
            System.err.println("Cannot find HTTP method annotation: " + backendMethod);
            System.exit(1);
        }
        return method;
    }

    private String extractAnnotationValue(AnnotationExpr annotExpr) {
        if (annotExpr == null) {
            return null;
        }
        if (annotExpr instanceof SingleMemberAnnotationExpr) {
            SingleMemberAnnotationExpr annot = (SingleMemberAnnotationExpr) annotExpr;
            Expression value = annot.getMemberValue();
            if (value instanceof StringLiteralExpr) {
                return ((StringLiteralExpr) value).getValue();
            }
        }
        return null;
    }

    private String extractAnnotationValue(MethodDeclaration method, String name) {
        return extractAnnotationValue(method.getAnnotationByName(name).orElse(null));
    }

    private String extractAnnotationValue(Parameter parameter, String name) {
        return extractAnnotationValue(parameter.getAnnotationByName(name).orElse(null));
    }

    private static class ParamsData {
        List<Parameter> queryParams = new ArrayList<>();
        Parameter bodyParam;
    }

    private ParamsData createParamsData(Collection<Parameter> parameters) {
        ParamsData data = new ParamsData();
        List<String> paramNames = new ArrayList<>();
        for (Parameter p : parameters) {
            if (p.isAnnotationPresent("QueryParam")) {
                data.queryParams.add(p);
            } else {
                if (data.bodyParam != null) {
                    System.err.println("Multiple body params: " + parameters);
                    System.exit(1);
                }
                data.bodyParam = p;
            }
        }
        return data;
    }

    private Expression createSetterLambda(Collection<Parameter> parameters) {
        List<Expression> assigns = new ArrayList<>();
        for (Parameter param : parameters) {
            String query = extractAnnotationValue(param, "QueryParam");
            if (query == null) {
                System.err.println("Cannot find @QueryParam: " + parameters);
                System.exit(1);
                return null;
            }
            assigns.add(new MethodCallExpr(
                    new NameExpr("setter"),
                    "set",
                    nodeList(new StringLiteralExpr(query), new NameExpr(param.getNameAsString()))
            ));
        }
        if (assigns.size() == 1) {
            return new LambdaExpr(helper.createSingleLambdaParameter("setter"), assigns.get(0));
        } else {
            BlockStmt block = new BlockStmt(nodeList(
                    assigns.stream().map(ExpressionStmt::new).collect(toList())
            ));
            return new LambdaExpr(helper.createSingleLambdaParameter("setter"), block);
        }
    }

    private Expression createGenericType(Type retType) {

        return new ObjectCreationExpr(
                null,
                new ClassOrInterfaceType(null, new SimpleName("GenericType"), nodeList(retType)),
                nodeList(),
                nodeList(),
                nodeList());
    }

    private BlockStmt createGetBody(MethodDeclaration backendMethod) {
        String path = extractAnnotationValue(backendMethod, "Path");
        if (path == null) {
            System.err.println("Cannot find @Path annotation: " + backendMethod);
            System.exit(1);
            return null;
        }
        ParamsData paramsData = createParamsData(backendMethod.getParameters());
        if (paramsData.bodyParam != null) {
            System.err.println("Body param encountered in GET method: " + backendMethod);
            System.exit(1);
        }
        Expression setterLambda = createSetterLambda(paramsData.queryParams);
        Type retType = helper.getBoxedType(backendMethod.getType());
        Expression call = new MethodCallExpr(null, "get", nodeList(
                new StringLiteralExpr(path),
                setterLambda,
                createGenericType(retType)));
        return new BlockStmt(nodeList(new ReturnStmt(call)));
    }

    private BlockStmt createPostBody(MethodDeclaration backendMethod) {
        String path = extractAnnotationValue(backendMethod, "Path");
        if (path == null) {
            System.err.println("Cannot find @Path annotation: " + backendMethod);
            System.exit(1);
            return null;
        }
        ParamsData paramsData = createParamsData(backendMethod.getParameters());
        Expression setterLambda = createSetterLambda(paramsData.queryParams);
        Expression entity = paramsData.bodyParam == null ? new NullLiteralExpr() :
                new NameExpr(paramsData.bodyParam.getNameAsString());
        Type retType = helper.getBoxedType(backendMethod.getType());
        Expression call = new MethodCallExpr(null, "post", nodeList(
                new StringLiteralExpr(path),
                setterLambda,
                entity,
                createGenericType(retType)));
        return new BlockStmt(nodeList(new ReturnStmt(call)));
    }
}
