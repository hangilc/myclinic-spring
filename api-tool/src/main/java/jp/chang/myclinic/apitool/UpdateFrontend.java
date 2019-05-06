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

    private ClassOrInterfaceDeclaration getSupportDeclaration() throws Exception {
        Path path = Paths.get(backendDir, "SupportService.java");
        String className = helper.getClassNameFromSourcePath(path);
        CompilationUnit unit = StaticJavaParser.parse(path);
        return getClassOrInterfaceDeclaration(unit, className);
    }

    private ClassOrInterfaceDeclaration getClassOrInterfaceDeclaration(CompilationUnit unit, String name) {
        return helper.getClassOrInterfaceDeclaration(unit, name);
    }

    private void updateFrontend() throws Exception {
        update("Frontend", FrontendMethod::createFrontendMethod,
                (f, b) -> f.removeBody(),
                (f, b)-> f.removeBody());
    }

    private void updateFrontendBackend() throws Exception {
        update("FrontendBackend", FrontendMethod::createFrontendBackendMethod,
                (f, b) -> {
                    f.setPublic(true);
                    f.addAnnotation(new MarkerAnnotationExpr("Override"));
                    f.setBody(createFrontendBackendBodyFromServiceOrSupport("dbBackendService", b));
                },
                (f, b) -> {
                    f.setPublic(true);
                    f.addAnnotation(new MarkerAnnotationExpr("Override"));
                    f.setBody(createFrontendBackendBodyFromServiceOrSupport("supportService", b));
                });
    }

    private BlockStmt createFrontendBackendBodyFromServiceOrSupport(String serviceOrSuppot,
                                                                    MethodDeclaration serviceMethod) {
        Expression methodCall = new MethodCallExpr(
                new NameExpr(serviceOrSuppot),
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
        BiConsumer<MethodDeclaration, MethodDeclaration> creator = (f, b) -> {
            f.setPublic(true);
            f.addAnnotation(new MarkerAnnotationExpr("Override"));
            f.setBody(FrontendMethodHelper.createNotImplementedBlock());
        };
        update("FrontendAdapter", FrontendMethod::createFrontendAdapterMethod, creator, creator);
    }

    private void updateFrontendProxy() throws Exception {
        BiConsumer<MethodDeclaration, MethodDeclaration> creator = (f, b) -> {
            f.setPublic(true);
            f.addAnnotation(new MarkerAnnotationExpr("Override"));
            f.setBody(new BlockStmt(nodeList(
                    new ReturnStmt(FrontendMethodHelper.createDelegateCall("delegate", b))
            )));
        };
        update("FrontendProxy", FrontendMethod::createFrontendProxyMethod, creator, creator);
    }

    private void update(String frontendClass,
                        Function<FrontendMethod, MethodDeclaration> backendMethodConverter,
                        BiConsumer<MethodDeclaration, MethodDeclaration> serviceMethodCreater,
                        BiConsumer<MethodDeclaration, MethodDeclaration> supportMethodCreater) throws Exception {
        ClassOrInterfaceDeclaration backendDecl = getBackendDeclaration();
        ClassOrInterfaceDeclaration serviceDecl = getServiceDeclaration();
        ClassOrInterfaceDeclaration supportDecl = getSupportDeclaration();
        Path frontendPath = frontendPath(frontendClass);
        CompilationUnit frontendUnit = StaticJavaParser.parse(frontendPath);
        ClassOrInterfaceDeclaration frontendDecl = getClassOrInterfaceDeclaration(frontendUnit,
                helper.getClassNameFromSourcePath(frontendPath));
        List<MethodDeclaration> unimplementedBackendMethods = listUnimplementedMethods(backendDecl,
                frontendDecl);
        List<MethodDeclaration> unimplementedServiceMethods = listUnimplementedMethods(serviceDecl,
                frontendDecl);
        List<MethodDeclaration> unimplementedSupportMethods = listUnimplementedMethods(supportDecl,
                frontendDecl);
        int cUnimplemented = unimplementedBackendMethods.size() + unimplementedBackendMethods.size() +
                unimplementedSupportMethods.size();
        if ( cUnimplemented > 0) {
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
            for (MethodDeclaration supportMethod : unimplementedSupportMethods) {
                MethodDeclaration frontendMethod = createServiceMethodHead(supportMethod);
                supportMethodCreater.accept(frontendMethod, supportMethod);
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
