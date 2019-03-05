package jp.chang.myclinic.apitool;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.Type;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class Main {

    private Path backendDir = Paths.get("backend/src/main/java/jp/chang/myclinic/backend");
    private Path backendPersistDir = backendDir.resolve("persistence");
    private Path backendMockDir = Paths.get("backend/src/main/java/jp/chang/myclinic/backendmock");
    private Path backendMockPersistDir = backendMockDir.resolve("persistence");
    private Set<String> persists;

    public static void main( String[] args ) throws Exception
    {
        new Main().run(args);
    }

    private void run(String[] args) throws Exception {
        this.persists = listPersists(backendPersistDir);
        Set<String> missingMockPersists = listMissingMockPersists();
        if( missingMockPersists.size() > 0 ){
            System.err.println("Missing mock persists: " + missingMockPersists);
            System.exit(1);
        }
        for(String persist: persists){
            CompilationUnit unit = StaticJavaParser.parse(getPersistPath(persist));
            Optional<ClassOrInterfaceDeclaration> optInterface = unit.getInterfaceByName(persist);
            if( optInterface.isPresent() ){
                ClassOrInterfaceDeclaration persistInterface = optInterface.get();
                List<MethodDeclaration> methods = persistInterface.getMethods();
                System.out.println(persist);
                for(MethodDeclaration method: methods){
                    SimpleName name = method.getName();
                    Type retType = method.getType();
                }
            } else {
                System.err.println("Cannot find class: " + persist);
                System.exit(1);
            }
        }
    }

    private Path getPersistPath(String name){
        return backendPersistDir.resolve(name + ".java");
    }

    private Set<String> listMissingMockPersists(){
        Set<String> mockPersists = listPersists(backendMockPersistDir);
        mockPersists.removeAll(persists);
        return mockPersists;
    }

    private Set<String> listPersists(Path dir){
        File[] persists = backendPersistDir.toFile().listFiles();
        return Arrays.stream(persists).map(f -> f.getName().replaceAll("\\.java$", "")).collect(toSet());
    }
}

