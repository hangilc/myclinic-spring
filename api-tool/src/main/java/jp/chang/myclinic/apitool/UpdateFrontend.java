package jp.chang.myclinic.apitool;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.CallableDeclaration.Signature;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import jp.chang.myclinic.apitool.lib.Helper;
import jp.chang.myclinic.apitool.lib.frontend.*;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.github.javaparser.ast.NodeList.nodeList;
import static java.util.stream.Collectors.toList;

@CommandLine.Command(name = "update-frontend", description = "Adds to Frontend.java according to Backend")
class UpdateFrontend implements Runnable {

    @CommandLine.Option(names = {"--save"}, description = "Saves result to Frontend.java")
    private boolean save;

    private String backendDir = "backend-db/src/main/java/jp/chang/myclinic/backenddb";
    private String frontendDir = "frontend/src/main/java/jp/chang/myclinic/frontend";
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

    private Path frontendPath(String className) {
        return Paths.get(frontendDir, className + ".java");
    }

    private MethodDeclaration createServiceMethodHead(MethodDeclaration backendMethod) {
        MethodDeclaration method = new MethodDeclaration();
        Type returnType = helper.createGenericType("CompletableFuture",
                helper.getBoxedType(backendMethod.getType()));
        method.setName(backendMethod.getNameAsString());
        method.setType(returnType);
        for (Parameter param : backendMethod.getParameters()) {
            method.addParameter(param);
        }
        return method;
    }

    private ClassOrInterfaceDeclaration getBackendDeclaration() throws Exception {
        Path path = Paths.get(backendDir, "Backend.java");
        String className = helper.getClassNameFromSourcePath(path);
        CompilationUnit unit = StaticJavaParser.parse(path);
        return getClassOrInterfaceDeclaration(unit, className);
    }

    private ClassOrInterfaceDeclaration getServiceDeclaration() throws Exception {
        Path path = Paths.get(backendDir, "DbBackendService.java");
        String className = helper.getClassNameFromSourcePath(path);
        CompilationUnit unit = StaticJavaParser.parse(path);
        return getClassOrInterfaceDeclaration(unit, className);
    }

    private ClassOrInterfaceDeclaration getClassOrInterfaceDeclaration(CompilationUnit unit, String name) {
        return unit.getClassByName(name)
                .or(() -> unit.getInterfaceByName(name))
                .orElseThrow(() -> new RuntimeException("Cannot find class/interface: " + name));
    }

    private void updateFrontend() throws Exception {
        ClassOrInterfaceDeclaration backendDecl = getBackendDeclaration();
        ClassOrInterfaceDeclaration serviceDecl = getServiceDeclaration();
        Path frontendSourceFile = frontendPath("Frontend");
        CompilationUnit frontendUnit = StaticJavaParser.parse(frontendSourceFile);
        ClassOrInterfaceDeclaration frontendDecl = getClassOrInterfaceDeclaration(frontendUnit,
                helper.getClassNameFromSourcePath(frontendSourceFile));
        List<MethodDeclaration> unimplementedBackendMethods = listUnimplementedMethods(backendDecl, frontendDecl);
        List<MethodDeclaration> unimplementedServiceMethods = listUnimplementedMethods(serviceDecl, frontendDecl);
        if (unimplementedBackendMethods.size() + unimplementedServiceMethods.size() > 0) {
            if (!save) {
                System.out.println("*** Frontend");
            }
            for (MethodDeclaration backendMethod : unimplementedBackendMethods) {
                FrontendMethod fm = FrontendMethods.createFrontendMethod(backendMethod);
                MethodDeclaration frontendMethod = fm.createFrontendMethod();
                frontendDecl.addMember(frontendMethod);
                if (!save) {
                    System.out.println(frontendMethod);
                }
            }
            for (MethodDeclaration backendMethod : unimplementedServiceMethods) {
                MethodDeclaration frontendMethod = createServiceMethodHead(backendMethod);
                frontendMethod.removeBody();
                frontendDecl.addMember(frontendMethod);
                if (!save) {
                    System.out.println(frontendMethod);
                }
            }
            if (save) {
                saveToFile(frontendSourceFile.toString(), frontendUnit.toString());
                System.out.printf("saved to %s\n", frontendSourceFile);
            }
        }
    }

    private void updateFrontendBackend() throws Exception {
        ClassOrInterfaceDeclaration backendDecl = getBackendDeclaration();
        ClassOrInterfaceDeclaration serviceDecl = getServiceDeclaration();
        Path frontendBackendSourceFile = frontendPath("FrontendBackend");
        CompilationUnit frontendBackendUnit = StaticJavaParser.parse(frontendBackendSourceFile);
        ClassOrInterfaceDeclaration frontendBackendDecl = getClassOrInterfaceDeclaration(frontendBackendUnit,
                helper.getClassNameFromSourcePath(frontendBackendSourceFile));
        List<MethodDeclaration> unimplementedBackendMethods = listUnimplementedMethods(backendDecl,
                frontendBackendDecl);
        List<MethodDeclaration> unimplementedServiceMethods = listUnimplementedMethods(serviceDecl,
                frontendBackendDecl);
        if (unimplementedBackendMethods.size() + unimplementedServiceMethods.size() > 0) {
            if (!save) {
                System.out.println("*** FrontendBackend");
            }
            for (MethodDeclaration backendMethod : unimplementedBackendMethods) {
                FrontendMethod fm = FrontendMethods.createFrontendMethod(backendMethod);
                MethodDeclaration method = fm.createFrontendBackendMethod();
                frontendBackendDecl.addMember(method);
                if (!save) {
                    System.out.println(method);
                }
            }
            for (MethodDeclaration serviceMethod : unimplementedServiceMethods) {
                MethodDeclaration frontendMethod = createServiceMethodHead(serviceMethod);
                frontendMethod.setPublic(true);
                frontendMethod.addAnnotation(new MarkerAnnotationExpr("Override"));
                frontendMethod.setBody(createFrontendBackendBodyFromService(serviceMethod));
                frontendBackendDecl.addMember(frontendMethod);
                if (!save) {
                    System.out.println(frontendMethod);
                }
            }
            if (save) {
                saveToFile(frontendBackendSourceFile.toString(), frontendBackendUnit.toString());
                System.out.printf("saved to %s\n", frontendBackendSourceFile);
            }
        }
    }

    private BlockStmt createFrontendBackendBodyFromService(MethodDeclaration serviceMethod) {
        Expression methodCall = new MethodCallExpr(
                new NameExpr("dbBackendService"),
                serviceMethod.getNameAsString(),
                nodeList(serviceMethod.getParameters().stream()
                        .map(p -> new NameExpr(p.getNameAsString()))
                        .collect(toList()))
        );
        if (serviceMethod.getType().isVoidType()) {
            return new BlockStmt(nodeList(
                    new ExpressionStmt(methodCall),
                    new ReturnStmt(
                            new MethodCallExpr(null, "value", nodeList(new NameExpr("null")))
                    )
            ));
        } else {
            return new BlockStmt(nodeList(
                    new ReturnStmt(new MethodCallExpr(
                            null,
                            "value",
                            nodeList(methodCall)
                    ))
            ));
        }
    }

    private void updateFrontendAdapter() throws Exception {
        ClassOrInterfaceDeclaration backendDecl = getBackendDeclaration();
        ClassOrInterfaceDeclaration serviceDecl = getServiceDeclaration();
        Path frontendPath = frontendPath("FrontendAdapter");
        CompilationUnit frontendUnit = StaticJavaParser.parse(frontendPath);
        ClassOrInterfaceDeclaration frontendDecl = getClassOrInterfaceDeclaration(frontendUnit,
                helper.getClassNameFromSourcePath(frontendPath));
        List<MethodDeclaration> unimplementedBackendMethods = listUnimplementedMethods(backendDecl,
                frontendDecl);
        List<MethodDeclaration> unimplementedServiceMethods = listUnimplementedMethods(serviceDecl,
                frontendDecl);
        if (unimplementedBackendMethods.size() + unimplementedBackendMethods.size() > 0) {
            if (!save) {
                System.out.println("*** FrontendAdapter");
            }
            for (MethodDeclaration backendMethod : unimplementedBackendMethods) {
                FrontendMethod fm = FrontendMethods.createFrontendMethod(backendMethod);
                MethodDeclaration method = fm.createFrontendAdapterMethod();
                frontendDecl.addMember(method);
                if (!save) {
                    System.out.println(method);
                }
            }
            for (MethodDeclaration serviceMethod : unimplementedServiceMethods) {
                MethodDeclaration frontendMethod = createServiceMethodHead(serviceMethod);
                frontendMethod.setPublic(true);
                frontendMethod.addAnnotation(new MarkerAnnotationExpr("Override"));
                // throw new RuntimeException("not implemented");
                frontendMethod.setBody(new BlockStmt(nodeList(
                        new ThrowStmt(new ObjectCreationExpr(
                                null,
                                new ClassOrInterfaceType(null, "RuntimeException"),
                                nodeList(new StringLiteralExpr("not implemented"))))
                )));
                frontendDecl.addMember(frontendMethod);
                if (!save) {
                    System.out.println(frontendMethod);
                }
            }
            if (save) {
                saveToFile(frontendPath.toString(), frontendUnit.toString());
                System.out.printf("saved to %s\n", frontendPath);
            }
        }
    }

    private void update(String frontendClass,
                        Function<FrontendMethod, MethodDeclaration> backendMethodConverter,
                        BiConsumer<MethodDeclaration, MethodDeclaration> serviceMethodCreater) throws Exception {
        ClassOrInterfaceDeclaration backendDecl = getBackendDeclaration();
        ClassOrInterfaceDeclaration serviceDecl = getServiceDeclaration();
        Path frontendPath = frontendPath(frontendClass);
        CompilationUnit frontendUnit = StaticJavaParser.parse(frontendPath);
        ClassOrInterfaceDeclaration frontendDecl = getClassOrInterfaceDeclaration(frontendUnit,
                helper.getClassNameFromSourcePath(frontendPath));
        List<MethodDeclaration> unimplementedBackendMethods = listUnimplementedMethods(backendDecl,
                frontendDecl);
        List<MethodDeclaration> unimplementedServiceMethods = listUnimplementedMethods(serviceDecl,
                frontendDecl);
        if (unimplementedBackendMethods.size() + unimplementedBackendMethods.size() > 0) {
            if (!save) {
                System.out.println("*** " + frontendClass);
            }
            for (MethodDeclaration backendMethod : unimplementedBackendMethods) {
                FrontendMethod fm = FrontendMethods.createFrontendMethod(backendMethod);
                MethodDeclaration method = backendMethodConverter.apply(fm);
                frontendDecl.addMember(method);
                if (!save) {
                    System.out.println(method);
                }
            }
            for (MethodDeclaration serviceMethod : unimplementedServiceMethods) {
                MethodDeclaration frontendMethod = createServiceMethodHead(serviceMethod);
                serviceMethodCreater.accept(frontendMethod, serviceMethod);
                frontendDecl.addMember(frontendMethod);
                if (!save) {
                    System.out.println(frontendMethod);
                }
            }
            if (save) {
                saveToFile(frontendPath.toString(), frontendUnit.toString());
                System.out.printf("saved to %s\n", frontendPath);
            }
        }
    }

    private void updateFrontendProxy() throws Exception {
        update("FrontendProxy", FrontendMethod::createFrontendProxyMethod,
                (f, b) -> {
                    f.setPublic(true);
                    f.addAnnotation(new MarkerAnnotationExpr("Override"));
                    f.setBody(new BlockStmt(nodeList(
                            new ReturnStmt(FrontendMethodHelper.createDelegateCall("delegate", b))
                    )));
                });
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
