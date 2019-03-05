package jp.chang.myclinic.apitool;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.CallableDeclaration.Signature;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class Main {

    private Path backendDir = Paths.get("backend/src/main/java/jp/chang/myclinic/backend");
    private Path backendPersistDir = backendDir.resolve("persistence");
    private Path backendMockDir = Paths.get("backend-mock/src/main/java/jp/chang/myclinic/backendmock");
    private Path backendPersistMockDir = backendMockDir.resolve("persistence");
    private Path asyncDir = Paths.get("backend-async/src/main/java/jp/chang/myclinic/backendasync");
    private Set<String> persists;
    private List<MethodDeclaration> backendMethods;
    private boolean dryRun = false;

    public static void main(String[] args) throws Exception {
        new Main().run(args);
    }

    private void run(String[] args) throws Exception {
        this.persists = listPersists(backendPersistDir);
        this.dryRun = true;
        CompilationUnit backendUnit = StaticJavaParser.parse(backendDir.resolve("Backend.java"));
        ClassOrInterfaceDeclaration backendClass = backendUnit.getClassByName("Backend").orElseThrow(
                () -> new RuntimeException("Cannot find Backend class")
        );
        backendMethods = backendClass.getMethods().stream()
                .filter(m ->
                    m.isPublic() && !m.isAnnotationPresent("BackendPrivate")
                )
                .collect(toList());
        //syncMock();
        syncAsync();
    }

    private void syncAsync() {
        Map<Signature, MethodDeclaration> backendSigs = methodsToSigMap(backendMethods);
        CompilationUnit asyncUnit = parseSource(asyncDir.resolve("BackendAsync.java"));
        ClassOrInterfaceDeclaration asyncInterface = getInterface(asyncUnit, "BackendAsync");
        Map<Signature, MethodDeclaration> asyncSigs = methodsToSigMap(asyncInterface.getMethods());
        Set<Signature> missing = findMissingSigs(asyncSigs.keySet(), backendSigs.keySet());
        for(Signature sig: missing){
            MethodDeclaration backendMethod = backendSigs.get(sig);
            MethodDeclaration asyncMethod = asyncInterface.addMethod(backendMethod.getNameAsString());
            asyncMethod.setType(makeAsyncReturnType(backendMethod.getType()));
            asyncMethod.removeBody();
            for(Parameter param: backendMethod.getParameters()){
                asyncMethod.addParameter(param);
            }
        }
        System.out.println(asyncInterface);
    }

    private Type makeAsyncReturnType(Type type){
        if( type.isVoidType() ){
            type = PrimitiveType.booleanType();
        }
        return new ClassOrInterfaceType(null, new SimpleName("CompletableFuture"), new NodeList<>(type));
    }

    private Set<Signature> findMissingSigs(Set<Signature> sigs, Set<Signature> expected){
        expected = new HashSet<>(expected);
        expected.removeAll(sigs);
        return expected;
    }

    private Map<Signature, MethodDeclaration> methodsToSigMap(List<MethodDeclaration> methods){
        return methods.stream()
                .collect(toMap(
                        MethodDeclaration::getSignature,
                        m -> m,
                        (e1, e2) -> e2,
                        LinkedHashMap::new));
    }

    private void syncMock() throws Exception {
        Set<String> missingMockPersists = listMissingMockPersists();
        if (missingMockPersists.size() > 0) {
            System.err.println("Missing mock persists: " + missingMockPersists);
            System.exit(1);
        }
        for (String persist : persists) {
            CompilationUnit persistUnit = parseSource(getPersistPath(persist));
            Map<Signature, MethodDeclaration> sigMap =
                    listInterfaceMethods(persistUnit, persist).stream()
                            .collect(toMap(MethodDeclaration::getSignature, m -> m));
            String mockName = getMockName(persist);
            CompilationUnit mockUnit = parseSource(getPersistMockPath(mockName));
            ClassOrInterfaceDeclaration mockClass = getClass(mockUnit, mockName);
            Map<Signature, MethodDeclaration> mockMap =
                    mockClass.getMethods().stream()
                            .collect(toMap(MethodDeclaration::getSignature, m -> m));
            Set<Signature> missingSigs = new HashSet<>(sigMap.keySet());
            missingSigs.removeAll(mockMap.keySet());
            if (missingSigs.size() > 0) {
                for (Signature sig : missingSigs) {
                    MethodDeclaration method = sigMap.get(sig);
                    MethodDeclaration m = mockClass.addMethod(method.getNameAsString(), Keyword.PUBLIC);
                    m.setType(method.getType());
                    for (Parameter param : method.getParameters()) {
                        m.addParameter(param);
                    }
                    m.addAnnotation("Override");
                    BlockStmt stmt = new BlockStmt();
                    stmt.addStatement("throw new RuntimeException(\"not implemented (api-tool)\");");
                    m.setBody(stmt);
                    System.out.printf("mock:+: %s: %s\n", mockName, m.getNameAsString());
                }
                if (dryRun) {
                    System.out.println(mockUnit);
                } else {
                    Files.write(getPersistMockPath(mockName), mockUnit.toString().getBytes());
                    System.out.printf("mock:save:%s\n", getPersistMockPath(mockName).toString());
                }
            }
        }
    }

    private CompilationUnit parseSource(Path path) {
        try {
            return StaticJavaParser.parse(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<MethodDeclaration> listInterfaceMethods(CompilationUnit unit, String interfaceName) {
        Optional<ClassOrInterfaceDeclaration> optInterface = unit.getInterfaceByName(interfaceName);
        if (optInterface.isPresent()) {
            return optInterface.get().getMethods();
        } else {
            throw new RuntimeException("Cannot find interface: " + interfaceName);
        }
    }

    private List<MethodDeclaration> listClassMethods(CompilationUnit unit, String className) {
        Optional<ClassOrInterfaceDeclaration> optInterface = unit.getClassByName(className);
        if (optInterface.isPresent()) {
            return optInterface.get().getMethods();
        } else {
            throw new RuntimeException("Cannot find class: " + className);
        }
    }

    private Path getPersistPath(String interfaceName) {
        return backendPersistDir.resolve(interfaceName + ".java");
    }

    private String getMockName(String name) {
        return name + "Mock";
    }

    private Path getPersistMockPath(String className) {
        return backendPersistMockDir.resolve(className + ".java");
    }

    private ClassOrInterfaceDeclaration getClass(CompilationUnit unit, String className) {
        return unit.getClassByName(className).orElseThrow(() ->
                new RuntimeException("Cannot find class: " + className));
    }

    private ClassOrInterfaceDeclaration getInterface(CompilationUnit unit, String interfaceName) {
        return unit.getInterfaceByName(interfaceName).orElseThrow(() ->
                new RuntimeException("Cannot find interface: " + interfaceName));
    }

    private Set<String> listMissingMockPersists() {
        Set<String> mockPersists = listPersists(backendPersistMockDir);
        mockPersists.removeAll(persists);
        return mockPersists;
    }

    private Set<String> listPersists(Path dir) {
        File[] persists = backendPersistDir.toFile().listFiles();
        return Arrays.stream(persists).map(f -> f.getName().replaceAll("\\.java$", "")).collect(toSet());
    }
}

