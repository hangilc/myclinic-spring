package jp.chang.myclinic.apitool;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.CallableDeclaration.Signature;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.UnknownType;
import jp.chang.myclinic.apitool.lib.DtoClassList;
import jp.chang.myclinic.apitool.lib.Helper;
import picocli.CommandLine;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.github.javaparser.ast.NodeList.nodeList;
import static java.util.stream.Collectors.toList;

@CommandLine.Command(name = "update-frontend", description = "Adds to Frontend.java according to Backend")
class UpdateFrontend implements Runnable {

    @CommandLine.Option(names = {"--save"}, description = "Saves result to Frontend.java")
    private boolean save;

    private String backendSourceFile = "backend-db/src/main/java/jp/chang/myclinic/backenddb/Backend.java";
    private String frontendDir = "frontend/src/main/java/jp/chang/myclinic/frontend";
    private static Map<String, Class<?>> nameToDtoClassMap = DtoClassList.getNameDtoClassMap();
    private Helper helper = Helper.getInstance();

    @Override
    public void run() {
        try {
            updateFrontend();
            updateFrontendBackend();
            updateFrontendAdapter();
            updateFrontendProxy();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void updateFrontend() throws Exception {
        CompilationUnit backendUnit = StaticJavaParser.parse(Paths.get(backendSourceFile));
        Path frontendSourceFile = Paths.get(frontendDir, "Frontend.java");
        CompilationUnit frontendUnit = StaticJavaParser.parse(frontendSourceFile);
        ClassOrInterfaceDeclaration backendDecl = backendUnit.getClassByName("Backend")
                .orElseThrow(() -> new RuntimeException("Cannot find class: Backend"));
        ClassOrInterfaceDeclaration frontendDecl = frontendUnit.getInterfaceByName("Frontend")
                .orElseThrow(() -> new RuntimeException("Cannot find class: Frontend"));
        backendDecl.getOrphanComments().forEach(backendDecl::removeOrphanComment);
        List<MethodDeclaration> unimplementedBackendMethods = listUnimplementedMethods(backendDecl, frontendDecl);
        if (unimplementedBackendMethods.size() > 0) {
            for (MethodDeclaration backendMethod : unimplementedBackendMethods) {
                backendMethod.removeBody();
                backendMethod.setType(makeFrontendReturnType(backendMethod));
                backendMethod.removeModifier(Keyword.PUBLIC);
                System.out.println("*** Frontend");
                System.out.println(backendMethod);
                frontendDecl.addMember(backendMethod);
            }
            if (save) {
                saveToFile(frontendSourceFile.toString(), frontendUnit.toString());
                System.out.printf("saved to %s\n", frontendSourceFile);
            }
        }
    }


    private void updateFrontendBackend() throws Exception {
        CompilationUnit backendUnit = StaticJavaParser.parse(Paths.get(backendSourceFile));
        Path frontendBackendSourceFile = Paths.get(frontendDir, "FrontendBackend.java");
        CompilationUnit frontendBackendUnit = StaticJavaParser.parse(frontendBackendSourceFile);
        ClassOrInterfaceDeclaration backendDecl = backendUnit.getClassByName("Backend")
                .orElseThrow(() -> new RuntimeException("Cannot find class: Backend"));
        ClassOrInterfaceDeclaration frontendBackendDecl = frontendBackendUnit.getClassByName("FrontendBackend")
                .orElseThrow(() -> new RuntimeException("Cannot find class: FrontendBackend"));
        backendDecl.getOrphanComments().forEach(backendDecl::removeOrphanComment);
        List<MethodDeclaration> unimplementedBackendMethods = listUnimplementedMethods(backendDecl,
                frontendBackendDecl);
        if (unimplementedBackendMethods.size() > 0) {
            for (MethodDeclaration backendMethod : unimplementedBackendMethods) {
                backendMethod.removeBody();
                backendMethod.setType(makeFrontendReturnType(backendMethod));
                backendMethod.removeModifier(Keyword.PUBLIC);
                MethodDeclaration impl = implementFrontendBackendMethod(backendMethod);
                System.out.println("*** FrontendBackend");
                System.out.println(impl);
                frontendBackendDecl.addMember(impl);
            }
            if (save) {
                saveToFile(frontendBackendSourceFile.toString(), frontendBackendUnit.toString());
                System.out.printf("saved to %s\n", frontendBackendSourceFile);
            }
        }
    }

    private MethodDeclaration implementFrontendBackendMethod(MethodDeclaration backendMethod) {
        String name = backendMethod.getNameAsString();
        backendMethod.addMarkerAnnotation("Override");
        backendMethod.setModifiers(Keyword.PUBLIC);
        if (name.startsWith("enter")) {
            String paramType = backendMethod.getParameter(0).getTypeAsString();
            Class<?> dtoClass = nameToDtoClassMap.get(paramType);
            List<Field> autoIncs = Collections.emptyList();
            if (dtoClass != null) {
                autoIncs = helper.getAutoIncs(dtoClass);
            }
            if (autoIncs.size() == 1) {
                Field autoInc = autoIncs.get(0);
                backendMethod.setBody(makeEnterWithAutoIncBody(
                        backendMethod.getNameAsString(),
                        backendMethod.getParameter(0).getNameAsString(),
                        autoInc.getName()
                ));
            } else {
                backendMethod.setBody(makeTxBody(backendMethod));
            }
        } else if (name.startsWith("get") || name.startsWith("list") ||
                name.startsWith("search") || name.startsWith("find") ||
                name.startsWith("count") || name.startsWith("resolve") ||
                name.startsWith("batchResolve")) {
            backendMethod.setBody(makeBody("query", backendMethod));
        } else {
            backendMethod.setBody(makeTxBody(backendMethod));
        }
        return backendMethod;
    }

    private void updateFrontendAdapter() throws Exception {
        CompilationUnit backendUnit = StaticJavaParser.parse(Paths.get(backendSourceFile));
        Path frontendAdapterSrcPath = Paths.get(frontendDir, "FrontendAdapter.java");
        CompilationUnit adapterUnit = StaticJavaParser.parse(frontendAdapterSrcPath);
        ClassOrInterfaceDeclaration adapterClass = adapterUnit.getClassByName("FrontendAdapter")
                .orElseThrow(() -> new RuntimeException("cannot find FrontendAdapter interface."));
        ClassOrInterfaceDeclaration backendInterface = backendUnit.getClassByName("Backend")
                .orElseThrow(() -> new RuntimeException("cannot find Backend interface."));
        List<MethodDeclaration> unimplementedBackendMethods = listUnimplementedMethods(backendInterface,
                adapterClass);
        if (unimplementedBackendMethods.size() > 0) {
            for (MethodDeclaration backendMethod : unimplementedBackendMethods) {
                backendMethod.removeBody();
                backendMethod.setType(wrapWithCompletableFuture(backendMethod.getType()));
                backendMethod.addAnnotation(new MarkerAnnotationExpr("Override"));
                Statement throwStmt = new ThrowStmt(new ObjectCreationExpr(
                        null,
                        new ClassOrInterfaceType(null, "RuntimeException"),
                        nodeList(new StringLiteralExpr("not implemented"))
                ));
                backendMethod.setBody(new BlockStmt(nodeList(
                        throwStmt
                )));
                System.out.println("*** FrontendAdapter");
                System.out.println(backendMethod);
                adapterClass.addMember(backendMethod);
            }
            if (save) {
                saveToFile(frontendAdapterSrcPath.toString(), adapterUnit.toString());
                System.out.printf("saved to %s\n", frontendAdapterSrcPath);
            }
        }
    }

    private void updateFrontendProxy() throws Exception {
        CompilationUnit backendUnit = StaticJavaParser.parse(Paths.get(backendSourceFile));
        Path frontendProxySrcPath = Paths.get(frontendDir, "FrontendProxy.java");
        CompilationUnit proxyUnit = StaticJavaParser.parse(frontendProxySrcPath);
        ClassOrInterfaceDeclaration proxyClass = proxyUnit.getClassByName("FrontendProxy")
                .orElseThrow(() -> new RuntimeException("cannot find FrontendProxy interface."));
        ClassOrInterfaceDeclaration backendInterface = backendUnit.getClassByName("Backend")
                .orElseThrow(() -> new RuntimeException("cannot find Backend interface."));
        List<MethodDeclaration> unimplementedBackendMethods = listUnimplementedMethods(backendInterface,
                proxyClass);
        if (unimplementedBackendMethods.size() > 0) {
            for (MethodDeclaration backendMethod : unimplementedBackendMethods) {
                backendMethod.removeBody();
                backendMethod.setType(wrapWithCompletableFuture(backendMethod.getType()));
                backendMethod.addAnnotation(new MarkerAnnotationExpr("Override"));
                Statement returnStmt = new ReturnStmt(new MethodCallExpr(new NameExpr("delegate"),
                        backendMethod.getNameAsString(),
                        nodeList(backendMethod.getParameters().stream()
                                .map(NodeWithSimpleName::getNameAsExpression)
                                .collect(toList()))));
                backendMethod.setBody(new BlockStmt(nodeList(
                        returnStmt
                )));
                System.out.println("*** FrontendProxy");
                System.out.println(backendMethod);
                proxyClass.addMember(backendMethod);
            }
            if (save) {
                saveToFile(frontendProxySrcPath.toString(), proxyUnit.toString());
                System.out.printf("saved to %s\n", frontendProxySrcPath);
            }
        }
    }

    private List<MethodDeclaration> listUnimplementedMethods(ClassOrInterfaceDeclaration backend,
                                                             ClassOrInterfaceDeclaration target) {
        List<MethodDeclaration> result = new ArrayList<>();
        for (MethodDeclaration backendMethod : backend.getMethods()) {
            if (!backendMethod.isPublic() || backendMethod.isAnnotationPresent("ExcludeFromFrontend")) {
                continue;
            }
            Signature backendSig = backendMethod.getSignature();
            if (target.getCallablesWithSignature(backendSig).size() == 0) {
                result.add(backendMethod);
            }
        }
        return result;
    }

    private Type makeFrontendReturnType(MethodDeclaration backendMethod) {
        if (backendMethod.getNameAsString().startsWith("enter")) {
            if (backendMethod.getType().isVoidType()) {
                Parameter param = backendMethod.getParameter(0);
                Type paramType = param.getType();
                Class<?> dtoClass = nameToDtoClassMap.get(paramType.asString());
                if (dtoClass != null) {
                    List<Field> autoIncs = helper.getAutoIncs(dtoClass);
                    if (autoIncs.size() == 1) {
                        Field autoInc = autoIncs.get(0);
                        Class<?> autoIncClass = primitiveToBoxedClass(autoInc.getType());
                        Type retType = new ClassOrInterfaceType(null, autoIncClass.getSimpleName());
                        return wrapWithCompletableFuture(retType);
                    }
                }
            }
        }
        return wrapWithCompletableFuture(backendMethod.getType());
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

    private Class<?> primitiveToBoxedClass(Class<?> prim) {
        if (prim == int.class) {
            return Integer.class;
        } else if (prim == double.class) {
            return Double.class;
        } else {
            return prim;
        }
    }

    private Type wrapWithCompletableFuture(Type type) {
        if (type.isVoidType()) {
            type = new ClassOrInterfaceType(null, "Void");
        } else if (type.isPrimitiveType()) {
            type = type.asPrimitiveType().toBoxedType();
        }
        return new ClassOrInterfaceType(null, new SimpleName("CompletableFuture"), nodeList(type));
    }

    private void saveToFile(String file, String src) {
        if (file == null) {
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
