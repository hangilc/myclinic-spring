package jp.chang.myclinic.apitool;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.CallableDeclaration.Signature;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.VoidType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.github.javaparser.ast.NodeList.nodeList;

@CommandLine.Command(name = "populate-frontend", description = "Adds to Frontend.java according to Backend")
class PopulateFrontend implements Runnable {

    @CommandLine.Option(names = {"--save"}, description = "Saves result to Frontend.java")
    private boolean save;

    private String frontendSourceFile = "frontend/src/main/java/jp/chang/myclinic/frontend/Frontend.java";
    private String backendSourceFile = "backend-db/src/main/java/jp/chang/myclinic/backenddb/Backend.java";

    @Override
    public void run() {
        try {
            CompilationUnit backendUnit = StaticJavaParser.parse(Paths.get(backendSourceFile));
            CompilationUnit frontendUnit = StaticJavaParser.parse(Paths.get(frontendSourceFile));
            ClassOrInterfaceDeclaration backendDecl = backendUnit.getClassByName("Backend")
                    .orElseThrow(() -> new RuntimeException("Cannot find class: Backend"));
            ClassOrInterfaceDeclaration frontendDecl = frontendUnit.getInterfaceByName("Frontend")
                    .orElseThrow(() -> new RuntimeException("Cannot find class: Frontend"));
            List<String> excludes = List.of("setPracticeLogPublisher",
                    "setHotlineLogPublisher", "getQuery", "xlate");
            for(MethodDeclaration backendMethod: backendDecl.getMethods()){
                if( !backendMethod.isPublic() ){
                    continue;
                }
                Signature sig = backendMethod.getSignature();
                List<CallableDeclaration<?>> callables = frontendDecl.getCallablesWithSignature(sig);
                if( callables.size() > 0 ){
                    continue;
                }
                if( excludes.contains(backendMethod.getNameAsString()) ){
                    continue;
                }
                backendMethod.removeBody();
                Type type = backendMethod.getType();
                if (type.isVoidType()) {
                    type = new ClassOrInterfaceType(null, "Void");
                } else if (type.isPrimitiveType()) {
                    PrimitiveType primType = type.asPrimitiveType();
                    type = primType.toBoxedType();
                }
                type = new ClassOrInterfaceType(null, new SimpleName("CompletableFuture"), nodeList(type));
                backendMethod.setType(type);
                backendMethod.removeModifier(Keyword.PUBLIC);
                frontendDecl.addMember(backendMethod);
            }
            if( save ){
                saveToFile(frontendSourceFile, frontendUnit.toString());
            } else {
                System.out.println(frontendUnit);
            }
        } catch(Exception e){
            throw new RuntimeException(e);
        }
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
