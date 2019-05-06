package jp.chang.myclinic.apitool.lib.frontend;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.github.javaparser.ast.NodeList.nodeList;
import static java.util.stream.Collectors.toList;

public class FrontendMethodHelper {

    private FrontendMethodHelper() {

    }

    public static List<Expression> getParameterValues(MethodDeclaration method){
        return method.getParameters().stream()
                .map(Parameter::getNameAsExpression)
                .collect(toList());
    }

    public static Expression createDelegateCall(String delegate, MethodDeclaration method){
        return new MethodCallExpr(new NameExpr(delegate),
                method.getNameAsString(),
                nodeList(getParameterValues(method)));

    }

}
