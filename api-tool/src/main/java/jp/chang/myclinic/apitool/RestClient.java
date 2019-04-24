package jp.chang.myclinic.apitool;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.*;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import jp.chang.myclinic.apitool.lib.Helper;
import picocli.CommandLine.*;

import java.nio.file.Path;
import java.util.List;

@Command(name = "rest-client", description = "Updates FrontendRest according to Backend")
public class RestClient implements Runnable {

    @Option(names = "--save")
    private boolean save;

    private Helper helper = Helper.getInstance();

    @Override
    public void run() {
        try {
            Path serverSource = helper.getBackendServerSourcePath();
            CompilationUnit backendUnit = StaticJavaParser.parse(serverSource);
            ClassOrInterfaceDeclaration serverDecl = backendUnit.getClassByName("RestServer")
                    .orElseThrow(() -> new RuntimeException("Cannot find class RestServer."));
            Path clientSource = helper.getFrontendRestSourcePath();
            CompilationUnit clientUnit = StaticJavaParser.parse(clientSource);
            ClassOrInterfaceDeclaration clientDecl = clientUnit.getClassByName("FrontendRest")
                    .orElseThrow(() -> new RuntimeException("Cannot find class FrontendRest."));
            List<MethodDeclaration> unimplementedMethods = helper.listUnimplementedMethods(serverDecl, clientDecl);
            for(MethodDeclaration backendMethod: unimplementedMethods){
                MethodDeclaration clientMethod = createClientMethod(backendMethod);
                System.out.println(clientMethod);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private MethodDeclaration createClientMethod(MethodDeclaration backendMethod){
        MethodDeclaration method = new MethodDeclaration();
        method.setName(backendMethod.getNameAsString());
        method.setModifiers(Keyword.PUBLIC);
        method.setType(helper.createGenericType("CompletableFuture", helper.getBoxedType(backendMethod.getType())));
        for(Parameter param: backendMethod.getParameters()){
            Parameter p = new Parameter(param.getType(), param.getName());
            method.addParameter(p);
        }
        return method;
    }
}
