package jp.chang.myclinic.apitool.lib.frontend;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.UnknownType;
import jp.chang.myclinic.apitool.lib.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.github.javaparser.ast.NodeList.nodeList;
import static java.util.stream.Collectors.toList;

abstract class FrontendMethodBase implements FrontendMethod {

    private MethodDeclaration backendMethod;
    Helper helper = Helper.getInstance();

    FrontendMethodBase(MethodDeclaration backendMethod) {
        this.backendMethod = backendMethod;
    }

    Type getReturnType(){
        return helper.createGenericType("CompletableFuture",
                helper.getBoxedType(backendMethod.getType()));
    }

    Parameter convertParameter(Parameter backendMethodParameter){
        return backendMethodParameter;
    }

    BlockStmt createFrontendNaclemdMethodBody(){
        return new BlockStmt();
    }

    String getMethodName(){
        return backendMethod.getNameAsString();
    }

    List<Expression> getParameterValues(){
        return backendMethod.getParameters().stream()
                .map(Parameter::getNameAsExpression)
                .collect(toList());
    }

    MethodDeclaration createMethodHead(){
        MethodDeclaration method = new MethodDeclaration();
        method.setName(backendMethod.getNameAsString());
        method.setType(getReturnType());
        for(Parameter param: backendMethod.getParameters()){
            method.addParameter(convertParameter(param));
        }
        method.removeBody();
        return method;
    }

    MethodDeclaration createMethodHeadForFrontendBackend(){
        MethodDeclaration method = createMethodHead();
        method.setPublic(true);
        method.addAnnotation(new MarkerAnnotationExpr("Override"));
        return method;
    }

    @Override
    public MethodDeclaration createFrontendMethod() {
        return createMethodHead();
    }

    MethodDeclaration createFrontendBackendMethod(String dbMethod){
        BlockStmt blockStmt = new BlockStmt();
        LambdaExpr lambdaExpr = new LambdaExpr(
                new Parameter(new UnknownType(), "backend"),
                new MethodCallExpr(new NameExpr("backend"), getMethodName(),
                        nodeList(getParameterValues()))
        );
        blockStmt.addStatement(
                new ReturnStmt(new MethodCallExpr(null, dbMethod, nodeList(lambdaExpr)))
        );
        MethodDeclaration method = createMethodHeadForFrontendBackend();
        method.setBody(blockStmt);
        return method;
    }

    @Override
    public MethodDeclaration createFrontendAdapterMethod(){
        MethodDeclaration method = createMethodHead();
        method.setPublic(true);
        method.addAnnotation(new MarkerAnnotationExpr("Override"));
        Statement throwStmt = new ThrowStmt(new ObjectCreationExpr(
                null,
                new ClassOrInterfaceType(null, "RuntimeException"),
                nodeList(new StringLiteralExpr("not implemented"))
        ));
        method.setBody(new BlockStmt(nodeList(throwStmt)));
        return method;
    }

    @Override
    public MethodDeclaration createFrontendProxyMethod(){
        MethodDeclaration method = createMethodHead();
        method.setPublic(true);
        method.addAnnotation(new MarkerAnnotationExpr("Override"));
        Statement returnStmt = new ReturnStmt(new MethodCallExpr(new NameExpr("delegate"),
                getMethodName(),
                nodeList(getParameterValues())));
        method.setBody(new BlockStmt(nodeList(returnStmt)));
        return method;
    }

}
