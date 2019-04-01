package jp.chang.myclinic.apitool;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.CallableDeclaration.Signature;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import jp.chang.myclinic.apitool.lib.DtoClassList;
import jp.chang.myclinic.dto.annotation.AutoInc;
import picocli.CommandLine;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.github.javaparser.ast.NodeList.nodeList;

@CommandLine.Command(name = "populate-frontend", description = "Adds to Frontend.java according to Backend")
class PopulateFrontend implements Runnable {

    @CommandLine.Option(names = {"--save"}, description = "Saves result to Frontend.java")
    private boolean save;

    private String frontendSourceFile = "frontend/src/main/java/jp/chang/myclinic/frontend/Frontend.java";
    private String backendSourceFile = "backend-db/src/main/java/jp/chang/myclinic/backenddb/Backend.java";
    private static Map<String, Class<?>> nameToDtoClassMap = DtoClassList.getNameDtoClassMap();

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
            backendDecl.getOrphanComments().forEach(backendDecl::removeOrphanComment);
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
                String methodName = backendMethod.getNameAsString();
                Type retType = backendMethod.getType();
                if( methodName.startsWith("enter") ){
                    if( retType.isVoidType() ){
                        Parameter param = backendMethod.getParameter(0);
                        Type paramType = param.getType();
                        Class<?> dtoClass = nameToDtoClassMap.get(paramType.asString());
                        List<Field> autoIncs = getAutoIncs(dtoClass);
                        if( autoIncs.size() == 1 ){
                            Field autoInc = autoIncs.get(0);
                            Class<?> autoIncClass = primitiveToBoxedClass(autoInc.getType());
                            retType = new ClassOrInterfaceType(null, autoIncClass.getSimpleName());
                        }
                    }
                }
                backendMethod.setType(wrapWithCompletableFuture(retType));
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

    private Class<?> primitiveToBoxedClass(Class<?> prim){
        if( prim == int.class ){
            return Integer.class;
        } else if( prim == double.class ){
            return Double.class;
        } else {
            return prim;
        }
    }

    private Type wrapWithCompletableFuture(Type type){
        if( type.isVoidType() ){
            type = new ClassOrInterfaceType(null, "Void");
        } else if( type.isPrimitiveType() ){
            type = type.asPrimitiveType().toBoxedType();
        }
        return new ClassOrInterfaceType(null, new SimpleName("CompletableFuture"), nodeList(type));
    }

    private List<Field> getAutoIncs(Class<?> dtoClass){
        List<Field> autoIncs = new ArrayList<>();
        for(Field field: dtoClass.getFields()){
            if( field.isAnnotationPresent(AutoInc.class)){
                autoIncs.add(field);
            }
        }
        return autoIncs;
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
