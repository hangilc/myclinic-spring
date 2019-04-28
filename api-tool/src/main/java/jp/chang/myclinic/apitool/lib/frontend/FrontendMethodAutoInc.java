package jp.chang.myclinic.apitool.lib.frontend;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.UnknownType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;

import static com.github.javaparser.ast.NodeList.nodeList;

public class FrontendMethodAutoInc extends FrontendMethodBase {

    private Parameter parameter;
    private Class<?> autoIncClass;
    private List<String> autoIncFieldNames;

    FrontendMethodAutoInc(MethodDeclaration backendMethod,
                          Parameter parameter,
                          Class<?> autoIncClass,
                          List<String> autoIncFieldNames) {
        super(backendMethod);
        this.parameter = parameter;
        this.autoIncClass = autoIncClass;
        this.autoIncFieldNames = autoIncFieldNames;
    }

    @Override
    Type getReturnType(){
        Class<?> boxed = helper.getBoxedClass(autoIncClass);
        return helper.createGenericType("CompletableFuture", boxed.getSimpleName());
    }

    @Override
    public MethodDeclaration createFrontendBackendMethod() {
        // return tx(backend -> { backend.---(---); return ----.---; });
        FieldAccessExpr fieldAccessExpr = new FieldAccessExpr(
                parameter.getNameAsExpression(), autoIncFieldNames.get(0));
        for(String fieldName: autoIncFieldNames.subList(1, autoIncFieldNames.size())){
            fieldAccessExpr = new FieldAccessExpr(fieldAccessExpr, fieldName);
        }
        BlockStmt blockStmt = new BlockStmt();
        BlockStmt lambdaBody = new BlockStmt(nodeList(
                new ExpressionStmt(
                        new MethodCallExpr(new NameExpr("backend"), getMethodName(),
                                nodeList(getParameterValues()))
                ),
                new ReturnStmt(fieldAccessExpr)
        ));
        LambdaExpr lambdaExpr = new LambdaExpr(
                new Parameter(new UnknownType(), "backend"),
                lambdaBody
        );
        MethodCallExpr txCall = new MethodCallExpr(null, "tx", nodeList(lambdaExpr));
        blockStmt.addStatement(new ReturnStmt(txCall));
        MethodDeclaration method = createMethodHeadForFrontendBackend();
        method.setBody(blockStmt);
        return method;
    }

}
