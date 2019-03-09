package jp.chang.myclinic.apitool;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.CallableDeclaration.Signature;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithPublicModifier;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.util.stream.Collectors.*;

public class Sync {

    private Path backendDir = Paths.get("backend/src/main/java/jp/chang/myclinic/backend");
    private Path backendPersistDir = backendDir.resolve("persistence");
    private Path backendMockDir = Paths.get("backend-mock/src/main/java/jp/chang/myclinic/backendmock");
    private Path backendPersistMockDir = backendMockDir.resolve("persistence");
    private Path asyncDir = Paths.get("backend-async/src/main/java/jp/chang/myclinic/backendasync");
    private Set<String> persists;
    private List<MethodDeclaration> backendMethods;
    private CmdArgs cmdArgs;

    public static void main(String[] args) throws Exception {
        new Sync().run(args);
    }

    private void run(String[] args) throws Exception {
        Properties props = new Properties();
        props.put("resource.loader", "RESOURCE");
        props.put("RESOURCE.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(props);
        this.persists = listPersists(backendPersistDir);
        this.cmdArgs = CmdArgs.parse(args);
        if (cmdArgs.help) {
            cmdArgs.usage(System.out);
            return;
        }
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
        //syncAsync();
        syncMysql();
    }

    private void syncMysql() {
        Path mysqlDir = Paths.get("./backend-mysql/src/main/java/jp/chang/myclinic/backendmysql");
        {
            Path mysqlPersistDir = mysqlDir.resolve("persistence");
            for (String persist : persists) {
                Path backendPersistPath = backendPersistDir.resolve(persist + ".java");
                CompilationUnit backendUnit = parseSource(backendPersistPath);
                List<MethodDeclaration> backendMethods = listInterfaceMethods(backendUnit, persist);
                Map<Signature, MethodDeclaration> backendSigs = methodsToSigMap(backendMethods);
                Path mysqlPersistPath = mysqlPersistDir.resolve(persist + "Mysql.java");
                if (!mysqlPersistPath.toFile().exists()) {
                    Template template = Velocity.getTemplate("PersistenceMysql.vm");
                    StringWriter sw = new StringWriter();
                    VelocityContext context = new VelocityContext();
                    context.put("Name", persist);
                    String entity = persist.replaceAll("Persistence$", "");
                    context.put("Entity", entity);
                    context.put("entity", toLowerFirst(entity));
                    template.merge(context, sw);
                    createFile("mysql", mysqlPersistPath, sw.toString());
                    if (cmdArgs.dryRun) {
                        backendMethods.forEach(m ->
                                System.out.printf("mysql:+: %s: %s\n", persist + "Mysql", m.getNameAsString())
                        );
                        continue;
                    }
                }
                CompilationUnit mysqlUnit = parseSource(mysqlPersistPath);
                List<MethodDeclaration> mysqlMethods = listClassMethods(mysqlUnit, persist + "Mysql");
                Map<Signature, MethodDeclaration> mysqlSig = methodsToSigMap(mysqlMethods);
                ClassOrInterfaceDeclaration mysqlClass = getClass(mysqlUnit, persist + "Mysql");
                Set<Signature> missing = findMissingSigs(mysqlSig.keySet(), backendSigs.keySet());
                if (missing.size() > 0) {
                    for (Signature sig : missing) {
                        MethodDeclaration method = backendSigs.get(sig);
                        MethodDeclaration m = mysqlClass.addMethod(method.getNameAsString(), Keyword.PUBLIC);
                        m.setType(method.getType());
                        for (Parameter param : method.getParameters()) {
                            m.addParameter(param);
                        }
                        m.addAnnotation(new MarkerAnnotationExpr("Override"));
                        BlockStmt stmt = new BlockStmt();
                        stmt.addStatement("throw new RuntimeException(\"not implemented (api-tool)\");");
                        m.setBody(stmt);
                        System.out.printf("mysql:+: %s: %s\n", persist + "Mysql", m.getNameAsString());
                    }
                    saveFile("mock", mysqlPersistPath, mysqlUnit);
                }
            }
        }
        {
            Path persistMysqlPath = mysqlDir.resolve("PersistenceMysql.java");
            CompilationUnit mysqlPersistUnit = parseSource(persistMysqlPath);
            ClassOrInterfaceDeclaration mysqlPersistClass = getClass(mysqlPersistUnit, "PersistenceMysql");
            List<MethodDeclaration> mysqlPersistMethods = mysqlPersistClass.getMethods();
            Map<Signature, MethodDeclaration> mysqlPersistSigs = methodsToSigMap((mysqlPersistMethods));
            CompilationUnit backendPersistUnit = parseSource(backendDir.resolve("Persistence.java"));
            List<MethodDeclaration> backendPersistMethods = listInterfaceMethods(backendPersistUnit, "Persistence");
            Map<Signature, MethodDeclaration> backendPersistSigs = methodsToSigMap(backendPersistMethods);
            Set<Signature> missing = findMissingSigs(mysqlPersistSigs.keySet(), backendPersistSigs.keySet());
            if (missing.size() > 0) {
                for (Signature sig : missing) {
                    MethodDeclaration backendPersistMethod = backendPersistSigs.get(sig);
                    String typeName = backendPersistMethod.getType().toString() + "Mysql";
                    String varName = toLowerFirst(typeName);
                    FieldDeclaration fieldDecl = mysqlPersistClass.addPrivateField(typeName, varName);
                    fieldDecl.addAnnotation(new MarkerAnnotationExpr("Autowired"));
                    MethodDeclaration m = addMethod(mysqlPersistClass, backendPersistMethod);
                    m.addModifier(Keyword.PUBLIC);
                    m.addAnnotation(new MarkerAnnotationExpr("Override"));
                    BlockStmt block = new BlockStmt();
                    ReturnStmt retStmt = new ReturnStmt(new NameExpr(varName));
                    block.addStatement(retStmt);
                    m.setBody(block);
                    System.out.printf("mysql-persist:+:%s\n", m.toString());
                }
                saveFile("mysql-persist", persistMysqlPath, mysqlPersistUnit);
            }
        }
    }

    private String toLowerFirst(String s) {
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }

    private MethodDeclaration addMethod(ClassOrInterfaceDeclaration dstClass, MethodDeclaration src) {
        MethodDeclaration dst = dstClass.addMethod(src.getNameAsString());
        dst.setType(src.getType());
        for (Parameter para : src.getParameters()) {
            dst.addParameter(para);
        }
        return dst;
    }

    private void syncAsync() {
        Map<Signature, MethodDeclaration> backendSigs = methodsToSigMap(backendMethods);
        {
            Path asyncSourcePath = asyncDir.resolve("BackendAsync.java");
            CompilationUnit asyncUnit = parseSource(asyncSourcePath);
            ClassOrInterfaceDeclaration asyncInterface = getInterface(asyncUnit, "BackendAsync");
            Map<Signature, MethodDeclaration> asyncSigs = methodsToSigMap(asyncInterface.getMethods());
            Set<Signature> missing = findMissingSigs(asyncSigs.keySet(), backendSigs.keySet());
            if (missing.size() > 0) {
                for (Signature sig : missing) {
                    MethodDeclaration backendMethod = backendSigs.get(sig);
                    MethodDeclaration asyncMethod = asyncInterface.addMethod(backendMethod.getNameAsString());
                    asyncMethod.setType(makeAsyncReturnType(backendMethod.getType()));
                    asyncMethod.removeBody();
                    for (Parameter param : backendMethod.getParameters()) {
                        asyncMethod.addParameter(param);
                    }
                }
                saveFile("async", asyncSourcePath, asyncUnit);
            }
        }
        {
            Path asyncBackendSourcePath = asyncDir.resolve("BackendAsyncBackend.java");
            CompilationUnit asyncBackendUnit = parseSource(asyncBackendSourcePath);
            ClassOrInterfaceDeclaration asyncBackendClass = getClass(asyncBackendUnit, "BackendAsyncBackend");
            Map<Signature, MethodDeclaration> asyncBackendSigs = methodsToSigMap(
                    asyncBackendClass.getMethods().stream()
                            .filter(NodeWithPublicModifier::isPublic)
                            .collect(toList())
            );
            Set<Signature> missing = findMissingSigs(asyncBackendSigs.keySet(), backendSigs.keySet());
            if (missing.size() > 0) {
                for (Signature sig : missing) {
                    MethodDeclaration backendMethod = backendSigs.get(sig);
                    MethodDeclaration asyncBackendMethod =
                            asyncBackendClass.addMethod(backendMethod.getNameAsString(),
                                    Keyword.PUBLIC);
                    asyncBackendMethod.addAnnotation(new MarkerAnnotationExpr("Override"));
                    asyncBackendMethod.setType(makeAsyncReturnType(backendMethod.getType()));
                    for (Parameter param : backendMethod.getParameters()) {
                        asyncBackendMethod.addParameter(param);
                    }
                    BlockStmt body = new BlockStmt();
                    List<Expression> backendCallArgs = backendMethod.getParameters().stream()
                            .map(NodeWithSimpleName::getNameAsExpression)
                            .collect(toList());
                    MethodCallExpr backendCall = new MethodCallExpr(new NameExpr("backend"),
                            backendMethod.getNameAsString(),
                            NodeList.nodeList(backendCallArgs));
                    if (backendMethod.getType().isVoidType()) {
                        body.addStatement(new ExpressionStmt(backendCall));
                        ReturnStmt retStmt = new ReturnStmt(
                                new MethodCallExpr("future", new BooleanLiteralExpr(true))
                        );
                        body.addStatement(retStmt);
                    } else {
                        ReturnStmt stmt = new ReturnStmt();
                        MethodCallExpr futureCall = new MethodCallExpr(null, "future",
                                NodeList.nodeList(backendCall));
                        stmt.setExpression(futureCall);
                        body.addStatement(stmt);
                    }
                    asyncBackendMethod.setBody(body);
                }
                saveFile("asyncBackend", asyncBackendSourcePath, asyncBackendUnit);
            }
        }
        {
            Path asyncClientSourcePath = asyncDir.resolve("BackendAsyncClient.java");
            CompilationUnit asyncClientUnit = parseSource(asyncClientSourcePath);
            ClassOrInterfaceDeclaration asyncBackendClass = getClass(asyncClientUnit, "BackendAsyncClient");
            Map<Signature, MethodDeclaration> asyncClientSigs = methodsToSigMap(
                    asyncBackendClass.getMethods().stream()
                            .filter(NodeWithPublicModifier::isPublic)
                            .collect(toList())
            );
            Set<Signature> missing = findMissingSigs(asyncClientSigs.keySet(), backendSigs.keySet());
            if (missing.size() > 0) {
                for (Signature sig : missing) {
                    MethodDeclaration backendMethod = backendSigs.get(sig);
                    MethodDeclaration asyncBackendMethod =
                            asyncBackendClass.addMethod(backendMethod.getNameAsString(),
                                    Keyword.PUBLIC);
                    asyncBackendMethod.addAnnotation(new MarkerAnnotationExpr("Override"));
                    asyncBackendMethod.setType(makeAsyncReturnType(backendMethod.getType()));
                    for (Parameter param : backendMethod.getParameters()) {
                        asyncBackendMethod.addParameter(param);
                    }
                    BlockStmt body = new BlockStmt();
                    boolean convertLocalDateTime = getBooleanAnnotationAttribute(backendMethod,
                            "BackendAsyncClientOption", "convertLocalDateTime").orElse(false);
                    List<Expression> backendCallArgs = backendMethod.getParameters().stream()
                            .map(para -> {
                                if (convertLocalDateTime) {
                                    if (para.getType().isClassOrInterfaceType() &&
                                            para.getType().asClassOrInterfaceType().getNameAsString().equals("LocalDateTime")) {
                                        VariableDeclarationExpr declExpr = new VariableDeclarationExpr(
                                                new VariableDeclarator(
                                                        new ClassOrInterfaceType(null, "String"),
                                                        para.getNameAsString() + "_str",
                                                        new MethodCallExpr(
                                                                new NameExpr("DateTimeUtil"),
                                                                "toSqlDateTime",
                                                                NodeList.nodeList(new NameExpr(para.getNameAsString()))
                                                        )
                                                )
                                        );
                                        body.addStatement(new ExpressionStmt(declExpr));
                                        return new NameExpr(para.getNameAsString() + "_str");
                                    } else {
                                        return para.getNameAsExpression();
                                    }
                                } else {
                                    return para.getNameAsExpression();
                                }
                            })
                            .collect(toList());
                    MethodCallExpr apiCall = new MethodCallExpr(new NameExpr("api"),
                            backendMethod.getNameAsString(),
                            NodeList.nodeList(backendCallArgs));
                    String compose = getStringAnnotationAttribute(backendMethod,
                            "BackendAsyncClientOption", "composeResult").orElse("");
                    if (!compose.isEmpty()) {
                        Expression lambda = StaticJavaParser.parseExpression(compose);
                        apiCall = new MethodCallExpr(apiCall, "thenCompose", NodeList.nodeList(lambda));
                    }
                    ReturnStmt retStmt = new ReturnStmt(apiCall);
                    body.addStatement(retStmt);
                    asyncBackendMethod.setBody(body);
                }
                saveFile("asyncClient", asyncClientSourcePath, asyncClientUnit);
            }
        }
        {
            Path asyncDelegateSourcePath = asyncDir.resolve("BackendAsyncDelegate.java");
            CompilationUnit asyncDelegateUnit = parseSource(asyncDelegateSourcePath);
            ClassOrInterfaceDeclaration asyncBackendClass = getClass(asyncDelegateUnit, "BackendAsyncDelegate");
            Map<Signature, MethodDeclaration> asyncDelegateSigs = methodsToSigMap(
                    asyncBackendClass.getMethods().stream()
                            .filter(NodeWithPublicModifier::isPublic)
                            .collect(toList())
            );
            Set<Signature> missing = findMissingSigs(asyncDelegateSigs.keySet(), backendSigs.keySet());
            if (missing.size() > 0) {
                for (Signature sig : missing) {
                    MethodDeclaration backendMethod = backendSigs.get(sig);
                    MethodDeclaration asyncDelegateMethod =
                            asyncBackendClass.addMethod(backendMethod.getNameAsString(),
                                    Keyword.PUBLIC);
                    asyncDelegateMethod.addAnnotation(new MarkerAnnotationExpr("Override"));
                    asyncDelegateMethod.setType(makeAsyncReturnType(backendMethod.getType()));
                    for (Parameter param : backendMethod.getParameters()) {
                        asyncDelegateMethod.addParameter(param);
                    }
                    BlockStmt body = new BlockStmt();
                    List<Expression> backendCallArgs = backendMethod.getParameters().stream()
                            .map(Parameter::getNameAsExpression)
                            .collect(toList());
                    MethodCallExpr delegateCall = new MethodCallExpr(new NameExpr("delegate"),
                            backendMethod.getNameAsString(),
                            NodeList.nodeList(backendCallArgs));
                    ReturnStmt retStmt = new ReturnStmt(delegateCall);
                    body.addStatement(retStmt);
                    asyncDelegateMethod.setBody(body);
                }
                saveFile("asyncDelegate", asyncDelegateSourcePath, asyncDelegateUnit);
            }
        }
    }

    private Optional<Boolean> getBooleanAnnotationAttribute(MethodDeclaration method, String annotationName,
                                                            String attributeName) {
        return method.getAnnotationByName(annotationName)
                .flatMap(AnnotationExpr::toNormalAnnotationExpr)
                .flatMap(annot -> {
                    for (MemberValuePair pair : annot.getPairs()) {
                        String member = pair.getNameAsString();
                        if (member.equals(attributeName)) {
                            return Optional.of(pair.getValue().asBooleanLiteralExpr().getValue());
                        }
                    }
                    return Optional.empty();
                });
    }

    private Optional<String> getStringAnnotationAttribute(MethodDeclaration method, String annotationName,
                                                          String attributeName) {
        return method.getAnnotationByName(annotationName)
                .flatMap(AnnotationExpr::toNormalAnnotationExpr)
                .flatMap(annot -> {
                    for (MemberValuePair pair : annot.getPairs()) {
                        String member = pair.getNameAsString();
                        if (member.equals(attributeName)) {
                            return Optional.of(pair.getValue().asStringLiteralExpr().getValue());
                        }
                    }
                    return Optional.empty();
                });
    }

    private Type makeAsyncReturnType(Type type) {
        if (type.isVoidType()) {
            type = new ClassOrInterfaceType(null, "Boolean");
        }
        if (type instanceof PrimitiveType) {
            PrimitiveType primitiveType = (PrimitiveType) type;
            type = primitiveType.toBoxedType();
        }
        return new ClassOrInterfaceType(null, new SimpleName("CompletableFuture"), new NodeList<>(type));
    }

    private Set<Signature> findMissingSigs(Set<Signature> sigs, Set<Signature> expected) {
        expected = new HashSet<>(expected);
        expected.removeAll(sigs);
        return expected;
    }

    private Map<Signature, MethodDeclaration> methodsToSigMap(List<MethodDeclaration> methods) {
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
                    m.addAnnotation(new MarkerAnnotationExpr("Override"));
                    BlockStmt stmt = new BlockStmt();
                    stmt.addStatement("throw new RuntimeException(\"not implemented (api-tool)\");");
                    m.setBody(stmt);
                    System.out.printf("mock:+: %s: %s\n", mockName, m.getNameAsString());
                }
                saveFile("mock", getPersistMockPath(mockName), mockUnit);
            }
        }
    }

    private void saveFile(String kind, Path path, CompilationUnit unit) {
        System.out.printf("%s:save:%s\n", kind, path.toString());
        if (cmdArgs.dryRun) {
            System.out.println(unit);
        } else {
            try {
                Files.write(path, unit.toString().getBytes());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    private void createFile(String kind, Path path, String content) {
        System.out.printf("%s:create:%s\n", kind, path.toString());
        if (cmdArgs.dryRun) {
            System.out.println(content);
        } else {
            try {
                Files.write(path, content.getBytes());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
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
        if (persists == null) {
            persists = new File[]{};
        }
        return Arrays.stream(persists).map(f -> f.getName().replaceAll("\\.java$", "")).collect(toSet());
    }
}

