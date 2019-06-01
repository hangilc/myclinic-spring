package jp.chang.myclinic.apitool;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.Type;
import jp.chang.myclinic.apitool.lib.Helper;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.*;

import java.io.StringWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import static com.github.javaparser.ast.NodeList.*;

@Command(name = "csharp-client")
class CSharpClient implements Runnable {

    @Option(names = "-o")
    private String outputFile;

    private Helper helper = Helper.getInstance();
    private StringBuffer sb = new StringBuffer();
    private Template tmplGet;
    private Template tmplPost;

    CSharpClient() {

    }

    @Override
    public void run() {
        try {
            Velocity.init();
            this.tmplGet = Velocity.getTemplate("api-tool/template/CSharpClientGet.vm");
            this.tmplPost = Velocity.getTemplate("api-tool/template/CSharpClientPost.vm");
            Path serverSource = helper.getBackendServerSourcePath();
            CompilationUnit backendUnit = StaticJavaParser.parse(serverSource);
            ClassOrInterfaceDeclaration serverDecl = backendUnit.getClassByName("RestServer")
                    .orElseThrow(() -> new RuntimeException("Cannot find class RestServer."));
            for (MethodDeclaration backendMethod : serverDecl.getMethods()) {
                handleServerMethod(backendMethod);
            }
            System.out.println(sb.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private void handleServerMethod(MethodDeclaration serverMethod) {
        String clientMethodName = helper.capitalize(serverMethod.getNameAsString());
        VelocityContext context = new VelocityContext();
        context.put("retType", getRetType(serverMethod.getType()));
        context.put("methodName", clientMethodName);
        context.put("params", getParams(serverMethod.getParameters()));
        StringWriter writer = new StringWriter();
        if (serverMethod.isAnnotationPresent("GET")) {
            tmplGet.merge(context, writer);
        } else if (serverMethod.isAnnotationPresent("POST")) {
            tmplPost.merge(context, writer);
        }
        sb.append(writer.toString());
    }

    public String getParams(NodeList<Parameter> serverParams){
        List<String> parts = new ArrayList<>();
        for(Parameter serverParam: serverParams){
            String cType = toCSharpType(serverParam.getType().toString());
            String name = serverParam.getNameAsString();
            parts.add(cType + " " + name);
        }
        return String.join(", ", parts);
    }

    private String toCSharpType(String javaType){
        switch(javaType){
            case "Integer": return "int";
            case "Double": return "double";
            case "Character": return "char";
            case "String": return "string";
            case "Void": return "void";
            case "LocalDate": return "DateTimeAsDate";
            case "LocalDateTime": return "DateTimeAsDateTime";
            default: return javaType;
        }
    }

    private String getRetType(Type serverRetType){
        String cType = toCSharpType(serverRetType.toString());
        if( cType.equals("void") ){
            return "Task";
        } else {
            return "Task<" + cType + ">";
        }
    }

}
