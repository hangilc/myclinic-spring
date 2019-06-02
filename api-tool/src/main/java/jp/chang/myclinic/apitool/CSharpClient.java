package jp.chang.myclinic.apitool;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private String extractAnnotationValue(AnnotationExpr annotExpr) {
        if (annotExpr == null) {
            return null;
        }
        if (annotExpr instanceof SingleMemberAnnotationExpr) {
            SingleMemberAnnotationExpr annot = (SingleMemberAnnotationExpr) annotExpr;
            Expression value = annot.getMemberValue();
            if (value instanceof StringLiteralExpr) {
                return ((StringLiteralExpr) value).getValue();
            }
        }
        return null;
    }

    private String extractAnnotationValue(MethodDeclaration method, String name) {
        return extractAnnotationValue(method.getAnnotationByName(name).orElse(null));
    }

    public static class QueryParam {
        private String key;
        private String value;

        public QueryParam(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

    private List<QueryParam> createQueryParams(NodeList<Parameter> params){
        List<QueryParam> queryParams = new ArrayList<>();
        for(Parameter param: params){
            String name = param.getNameAsString();
            String key = helper.toHyphenChain(name);
            queryParams.add(new QueryParam(key, name));
        }
        return queryParams;
    }

    private void handleServerMethod(MethodDeclaration serverMethod) {
        String clientMethodName = helper.capitalize(serverMethod.getNameAsString());
        VelocityContext context = new VelocityContext();
        context.put("retType", getRetType(serverMethod.getType()));
        context.put("methodName", clientMethodName);
        context.put("params", getParams(serverMethod.getParameters()));
        String path = extractAnnotationValue(serverMethod, "Path");
        if (path == null) {
            System.err.println("Cannot find @Path annotation: " + serverMethod);
            System.exit(1);
            return;
        }
        context.put("path", path);
        context.put("queryParams", createQueryParams(serverMethod.getParameters()));
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

    private static Pattern genericTypePattern = Pattern.compile("(\\w+)<(.+)>");

    private String toCSharpType(String javaType){
        Matcher matcher = genericTypePattern.matcher(javaType);
        if( matcher.matches() ){
            String outer = matcher.group(1);
            return String.format("%s<%s>", outer, toCSharpType(matcher.group(2)));
        } else {
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
