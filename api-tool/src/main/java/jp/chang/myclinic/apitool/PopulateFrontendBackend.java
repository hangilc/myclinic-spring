package jp.chang.myclinic.apitool;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.CallableDeclaration.Signature;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.UnknownType;
import jp.chang.myclinic.apitool.lib.DtoClassList;
import jp.chang.myclinic.apitool.lib.Helper;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.javaparser.ast.NodeList.nodeList;
import static java.util.stream.Collectors.toList;

@CommandLine.Command(name = "populate-frontendbackend", description = "Extends FrontendBackend.java")
class PopulateFrontendBackend implements Runnable {

    private String frontendSourceFile = "frontend/src/main/java/jp/chang/myclinic/frontend/Frontend.java";
    private String targetSourceFile = "frontend/src/main/java/jp/chang/myclinic/frontend/FrontendBackend.java";
    private Helper helper = Helper.getInstance();

    private Map<String, Class<?>> nameDtoClassMap = new HashMap<>();

    {
        for(Class<?> cls: DtoClassList.getList()){
            nameDtoClassMap.put(cls.getSimpleName(), cls);
        }
    }

    @Override
    public void run() {
        try {
            CompilationUnit frontendUnit = StaticJavaParser.parse(Paths.get(frontendSourceFile));
            CompilationUnit targetUnit = StaticJavaParser.parse(Paths.get(targetSourceFile));
            ClassOrInterfaceDeclaration frontendDecl = frontendUnit.getInterfaceByName("Frontend")
                    .orElseThrow(() -> new RuntimeException("Cannot find interface: Frontend."));
            ClassOrInterfaceDeclaration targetDecl = targetUnit.getClassByName("FrontendBackend")
                    .orElseThrow(() -> new RuntimeException("Cannot find class: FrontendBackend."));
            for (MethodDeclaration frontendMethod : frontendDecl.getMethods()) {
                Signature sig = frontendMethod.getSignature();
                if (targetDecl.getCallablesWithSignature(sig).size() > 0) {
                    continue;
                }
                String name = frontendMethod.getNameAsString();
                frontendMethod.addMarkerAnnotation("Override");
                if (name.startsWith("enter")) {
                    ClassOrInterfaceType methodType = (ClassOrInterfaceType)frontendMethod.getType();
                    System.out.println(methodType);
//                    if( frontendMethod.getType().isVoidType() ){
//                        System.out.println(frontendMethod);
//                    } else {
//                        Parameter param = frontendMethod.getParameter(0);
//                        String dtoName = param.getTypeAsString();
//                        Class<?> dtoClass = nameDtoClassMap.get(dtoName);
//                        System.out.println(frontendMethod);
//                        System.out.println(dtoClass);
//                    }
                } else if (name.startsWith("get") || name.startsWith("list") ||
                        name.startsWith("search") || name.startsWith("find") ||
                        name.startsWith("count") || name.startsWith("resolve")){
                    frontendMethod.setBody(composeGetMethodBody(frontendMethod));
                    targetDecl.addMember(frontendMethod);
                } else {

                }
            }
            //System.out.println(targetUnit);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BlockStmt composeGetMethodBody(MethodDeclaration methodDecl) {
        BlockStmt blockStmt = new BlockStmt();
        MethodCallExpr lambdaBody = new MethodCallExpr(new NameExpr("backend"),
                methodDecl.getName(),
                nodeList(
                        getMethodParameterNames(methodDecl).stream().map(NameExpr::new).collect(toList())
                ));
        LambdaExpr lambdaExpr = new LambdaExpr(
                new Parameter(new UnknownType(), "backend"), lambdaBody);
        MethodCallExpr queryCall = new MethodCallExpr(null, "query", nodeList(lambdaExpr));
        blockStmt.addStatement(new ReturnStmt(queryCall));
        return blockStmt;
    }

    private List<String> getMethodParameterNames(MethodDeclaration method) {
        return method.getParameters().stream().map(NodeWithSimpleName::getNameAsString).collect(toList());
    }
}
