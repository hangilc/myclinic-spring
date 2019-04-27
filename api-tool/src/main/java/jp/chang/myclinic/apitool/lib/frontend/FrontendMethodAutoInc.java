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

import static com.github.javaparser.ast.NodeList.nodeList;

public class FrontendMethodAutoInc extends FrontendMethodBase {

    private Field autoIncField;
    private Parameter dtoParameter;
    private Class<?> dtoClass;

    FrontendMethodAutoInc(MethodDeclaration backendMethod, Field autoIncField,
                          Parameter dtoParameter, Class<?> dtoClass) {
        super(backendMethod);
        this.autoIncField = autoIncField;
        this.dtoParameter = dtoParameter;
        this.dtoClass = dtoClass;
    }

    @Override
    Type getReturnType(){
        Class<?> boxed = helper.getBoxedClass(autoIncField.getType());
        return helper.createGenericType("CompletableFuture", boxed.getSimpleName());
    }

    @Override
    public MethodDeclaration createFrontendBackendMethod() {
        // return tx(backend -> { backend.---(---); return ----.---; });
        BlockStmt blockStmt = new BlockStmt();
        BlockStmt lambdaBody = new BlockStmt(nodeList(
                new ExpressionStmt(
                        new MethodCallExpr(new NameExpr("backend"), getMethodName(),
                                nodeList(getParameterValues()))
                ),
                new ReturnStmt(
                        new FieldAccessExpr(dtoParameter.getNameAsExpression(),
                                autoIncField.getName())
                )
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
