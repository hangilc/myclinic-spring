package jp.chang.myclinic.apitool;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.CallableDeclaration.Signature;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.UnknownType;
import jp.chang.myclinic.apitool.lib.DtoClassList;
import jp.chang.myclinic.apitool.lib.Helper;
import picocli.CommandLine;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.github.javaparser.ast.NodeList.nodeList;
import static java.util.stream.Collectors.toList;

@CommandLine.Command(name = "populate-frontendbackend", description = "Extends FrontendBackend.java")
class PopulateFrontendBackend implements Runnable {

    @CommandLine.Option(names = {"--save"}, description = "Saves output to FrontendBackend.java")
    private boolean save;

    private String frontendSourceFile = "frontend/src/main/java/jp/chang/myclinic/frontend/Frontend.java";
    private String targetSourceFile = "frontend/src/main/java/jp/chang/myclinic/frontend/FrontendBackend.java";
    private Helper helper = Helper.getInstance();

    private Map<String, Class<?>> nameDtoClassMap = DtoClassList.getNameDtoClassMap();

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
                    String paramType = frontendMethod.getParameter(0).getTypeAsString();
                    Class<?> dtoClass = nameDtoClassMap.get(paramType);
                    List<Field> autoIncs = Collections.emptyList();
                    if (dtoClass != null) {
                        autoIncs = helper.getAutoIncs(dtoClass);
                    }
                    if (autoIncs.size() == 1) {
                        Field autoInc = autoIncs.get(0);
                        Type retTypeArg = unwrapCompletableFuture(frontendMethod.getType());
                        Class<?> autoIncClass = primitiveToBoxedClass(autoInc.getType());
                        if (!autoIncClass.getSimpleName().equals(retTypeArg.asString())) {
                            throw new RuntimeException("Inconsisten types with AutoInc field>");
                        }
                        frontendMethod.setBody(makeEnterWithAutoIncBody(
                                frontendMethod.getNameAsString(),
                                frontendMethod.getParameter(0).getNameAsString(),
                                autoInc.getName()
                        ));
                        targetDecl.addMember(frontendMethod);
                    } else {
                        frontendMethod.setBody(makeTxBody(frontendMethod));
                        targetDecl.addMember(frontendMethod);
                    }
                } else if (name.startsWith("get") || name.startsWith("list") ||
                        name.startsWith("search") || name.startsWith("find") ||
                        name.startsWith("count") || name.startsWith("resolve") ||
                        name.startsWith("batchResolve")) {
                    frontendMethod.setBody(makeBody("query", frontendMethod));
                    targetDecl.addMember(frontendMethod);
                } else {
                    frontendMethod.setBody(makeTxBody(frontendMethod));
                    targetDecl.addMember(frontendMethod);
                }
            }
            if( save ){
                saveToFile(targetSourceFile, targetUnit.toString());
            } else {
                System.out.println(targetUnit);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BlockStmt makeEnterWithAutoIncBody(String methodName, String argName, String autoIncField) {
        BlockStmt blockStmt = new BlockStmt();
        BlockStmt lambdaBody = new BlockStmt(nodeList(
                new ExpressionStmt(
                        new MethodCallExpr(new NameExpr("backend"), methodName, nodeList(new NameExpr(argName)))
                ),
                new ReturnStmt(
                        new FieldAccessExpr(new NameExpr(argName), autoIncField)
                )
        ));
        LambdaExpr lambdaExpr = new LambdaExpr(
                new Parameter(new UnknownType(), "backend"),
                lambdaBody
        );
        MethodCallExpr txCall = new MethodCallExpr(null, "tx", nodeList(lambdaExpr));
        blockStmt.addStatement(new ReturnStmt(txCall));
        return blockStmt;
    }

    private BlockStmt makeTxBody(MethodDeclaration methodDecl) {
        Type retArgType = unwrapCompletableFuture(methodDecl.getType());
        String txName = retArgType.asString().equals("Void") ? "txProc" : "tx";
        return makeBody(txName, methodDecl);
    }

    private BlockStmt makeBody(String dbMethod, MethodDeclaration methodDecl) {
        BlockStmt blockStmt = new BlockStmt();
        LambdaExpr lambdaExpr = new LambdaExpr(
                new Parameter(new UnknownType(), "backend"),
                new MethodCallExpr(new NameExpr("backend"),
                        methodDecl.getNameAsString(),
                        nodeList(
                                getMethodParameterNames(methodDecl).stream().map(NameExpr::new).collect(toList())
                        )
                )
        );
        blockStmt.addStatement(
                new ReturnStmt(new MethodCallExpr(null, dbMethod, nodeList(lambdaExpr)))
        );
        return blockStmt;
    }

    private List<String> getMethodParameterNames(MethodDeclaration method) {
        return method.getParameters().stream().map(NodeWithSimpleName::getNameAsString).collect(toList());
    }

    private Class<?> primitiveToBoxedClass(Class<?> prim) {
        if (prim == int.class) {
            return Integer.class;
        } else if (prim == double.class) {
            return Double.class;
        } else {
            return prim;
        }
    }

    private Type unwrapCompletableFuture(Type type) {
        if (!type.isClassOrInterfaceType()) {
            throw new RuntimeException("Cannot unwrap CompletableFuture");
        }
        ClassOrInterfaceType classType = type.asClassOrInterfaceType();
        if (!classType.getNameAsString().equals("CompletableFuture")) {
            throw new RuntimeException("Cannot unwrap CompletableFuture");
        }
        return classType.getTypeArguments()
                .orElseThrow(() -> new RuntimeException("Cannot get type arguments."))
                .get(0);
    }

    private void saveToFile(String file, String src){
        if( file == null ){
            System.out.println(src);
        } else {
            try {
                Files.write(Paths.get(file), src.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
