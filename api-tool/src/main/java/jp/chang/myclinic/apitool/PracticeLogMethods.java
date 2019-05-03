package jp.chang.myclinic.apitool;

import static com.github.javaparser.ast.NodeList.*;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import jp.chang.myclinic.apitool.lib.Helper;
import jp.chang.myclinic.apitool.lib.PracticeLogKindList;
import picocli.CommandLine.*;

import java.nio.file.Path;
import java.nio.file.Paths;

@Command(name = "practice-log-methods")
class PracticeLogMethods implements Runnable{

    @Option(names = "--save")
    private boolean save = false;

    private Helper helper = Helper.getInstance();

    @Override
    public void run() {
        try {
            Path targetPath = Paths.get("logdto/src/main/java/jp/chang/myclinic/logdto/practicelog",
                    "PracticeLogDTO.java");
            CompilationUnit unit = StaticJavaParser.parse(targetPath);
            ClassOrInterfaceDeclaration decl = unit.getClassByName("PracticeLogDTO")
                    .orElseThrow(() -> new RuntimeException("Cannot find class: PracticeLogDTO"));
            for (Class<?> cls : PracticeLogKindList.kindMap.keySet()) {
                String kind = PracticeLogKindList.kindMap.get(cls);
                MethodDeclaration isMethod = createIsMethod(cls, kind);
                MethodDeclaration getMethod = createGetMethod(cls, kind);
                MethodDeclaration asMethod = createAsMethod(cls, kind);
                decl.addMember(isMethod);
                decl.addMember(getMethod);
                decl.addMember(asMethod);
                if (!save ) {
                    System.out.println(isMethod);
                    System.out.println(getMethod);
                    System.out.println(asMethod);
                }
            }
            if( save ){
                helper.saveToFile(targetPath, unit.toString(), true);
            }
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private MethodDeclaration createIsMethod(Class<?> cls, String kind){
        MethodDeclaration method = new MethodDeclaration();
        method.setPublic(true);
        method.setType("boolean");
        method.setName("is" + cls.getSimpleName());
        method.setBody(new BlockStmt(nodeList(new ReturnStmt(
                new MethodCallExpr(new StringLiteralExpr(kind), "equals", nodeList(new NameExpr("kind"))
        )))));
        return method;
    }

    private MethodDeclaration createGetMethod(Class<?> cls, String kind){
        MethodDeclaration method = new MethodDeclaration();
        method.setPublic(true);
        method.setType(helper.createGenericType("Optional", cls.getSimpleName()));
        method.setName("get" + cls.getSimpleName());
        // template: return Optional.of(mapper.readValue(body, ChargeCreated.class));
        Statement returnResultStmt = new ReturnStmt(
            new MethodCallExpr(new NameExpr("Optional"), "of", nodeList(
                    new MethodCallExpr(new NameExpr("mapper"), "readValue", nodeList(
                            new NameExpr("body"),
                            new FieldAccessExpr(new NameExpr(cls.getSimpleName()), "class")
                    ))
            ))
        );
        CatchClause catchClause = new CatchClause(
                new Parameter(new ClassOrInterfaceType(null, "IOException"), "e"),
                new BlockStmt(nodeList(new ThrowStmt(
                        new ObjectCreationExpr(null, new ClassOrInterfaceType(null, "UncheckedIOException"),
                        nodeList(new NameExpr("e"))))))
        );
        Statement tryStmt = new TryStmt(new BlockStmt(nodeList(returnResultStmt)), nodeList(catchClause), null);
        Statement ifElseStmt = new IfStmt(
                new MethodCallExpr(null, "is" + cls.getSimpleName(), nodeList()),
                tryStmt,
                new ReturnStmt(new MethodCallExpr(new NameExpr("Optional"), "empty", nodeList()))
        );
        method.setBody(new BlockStmt(nodeList(ifElseStmt)));
        return method;
    }

    private MethodDeclaration createAsMethod(Class<?> cls, String kind){
        MethodDeclaration method = new MethodDeclaration();
        method.setPublic(true);
        method.setType(cls.getSimpleName());
        method.setName("as" + cls.getSimpleName());
        // template: return getChargeCreated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
        MethodCallExpr methodCall = new MethodCallExpr(null, "get" + cls.getSimpleName(), nodeList());
        Expression throwLambda = new LambdaExpr(nodeList(),
                new ObjectCreationExpr(null, new ClassOrInterfaceType(null, "RuntimeException"), nodeList(
                        new StringLiteralExpr("Invalid practice-log kind.")
                )));
        methodCall = new MethodCallExpr(methodCall, "orElseThrow", nodeList(throwLambda));
        method.setBody(new BlockStmt(nodeList(new ReturnStmt(methodCall))));
        return method;
    }

}
