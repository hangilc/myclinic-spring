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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.javaparser.ast.NodeList.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Command(name = "csharp-client")
class CSharpClient implements Runnable {

    @Option(names = "-o")
    private String outputFile;

    private Helper helper = Helper.getInstance();
    private StringBuffer sb = new StringBuffer();
    private Template tmplGet;
    private Template tmplVoidGet;
    private Template tmplPost;
    private Template tmplVoidPost;

    CSharpClient() {

    }

    @Override
    public void run() {
        try {
            Velocity.init();
            this.tmplGet = Velocity.getTemplate("api-tool/template/CSharpClientGet.vm");
            this.tmplVoidGet = Velocity.getTemplate("api-tool/template/CSharpClientVoidGet.vm");
            this.tmplPost = Velocity.getTemplate("api-tool/template/CSharpClientPost.vm");
            this.tmplVoidPost = Velocity.getTemplate("api-tool/template/CSharpClientVoidPost.vm");
            Path serverSource = helper.getBackendServerSourcePath();
            CompilationUnit backendUnit = StaticJavaParser.parse(serverSource);
            ClassOrInterfaceDeclaration serverDecl = backendUnit.getClassByName("RestServer")
                    .orElseThrow(() -> new RuntimeException("Cannot find class RestServer."));
            for (MethodDeclaration backendMethod : serverDecl.getMethods()) {
                handleServerMethod(backendMethod);
            }
            if( outputFile == null ) {
                System.out.println(sb.toString());
            } else {
                Files.write(Path.of(outputFile), sb.toString().getBytes());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private void handleServerMethod(MethodDeclaration serverMethod) {
        String clientMethodName = helper.capitalize(serverMethod.getNameAsString());
        VelocityContext context = new VelocityContext();
        context.put("methodRetType", calcMethodRetType(serverMethod.getType()));
        context.put("retType", calcRetType(serverMethod.getType()));
        context.put("methodName", clientMethodName);
        context.put("params", getParams(serverMethod.getParameters()));
        String path = extractAnnotationValue(serverMethod, "Path");
        if (path == null) {
            System.err.println("Cannot find @Path annotation: " + serverMethod);
            System.exit(1);
            return;
        }
        context.put("path", path);
        StringWriter writer = new StringWriter();
        if (serverMethod.isAnnotationPresent("GET")) {
            context.put("queryParams", createQueryParams(serverMethod.getParameters()));
            if( Objects.equals(context.get("methodRetType"), "Task") ){
                tmplVoidGet.merge(context, writer);
            } else {
                tmplGet.merge(context, writer);
            }
        } else if (serverMethod.isAnnotationPresent("POST")) {
            PostMethodParams postParams = new PostMethodParams(serverMethod.getParameters());
            context.put("queryParams", createQueryParams(postParams.getRegularParameters()));
            context.put("bodyParam", postParams.getBodyParameter());
            context.put("bodyParamType", postParams.getBodyParameterType());
            if( Objects.equals(context.get("methodRetType"), "Task") ){
                tmplVoidPost.merge(context, writer);
            } else {
                tmplPost.merge(context, writer);
            }
        }
        sb.append(writer.toString());
    }

    private String calcMethodRetType(Type type){
        String cType = toCSharpType(type.toString());
        if( cType.equals("void") ){
            return "Task";
        } else {
            return String.format("Task<%s>", cType);
        }
    }

    private String calcRetType(Type type){
        String cType = toCSharpType(type.toString());
        if( cType.equals("void") ){
            return "object";
        } else {
            return cType;
        }
    }

    private static Pattern genericTypePattern = Pattern.compile("(\\w+)<(.+)>");

    private static String toCSharpType(String javaType){
        Matcher matcher = genericTypePattern.matcher(javaType);
        if( matcher.matches() ){
            String outer = matcher.group(1);
            String cOuter = toCSharpType(outer);
            String inner = matcher.group(2);
            String[] innerParts = inner.split("\\s*,\\s+");
            String cInner = Arrays.stream(innerParts).map(CSharpClient::toCSharpType).collect(joining(", "));
            return String.format("%s<%s>", cOuter, cInner);
        } else {
            switch(javaType){
                case "Integer": return "int";
                case "Double": return "double";
                case "Character": return "char";
                case "String": return "string";
                case "Void": return "void";
                case "LocalDate": return "DateTimeAsDate";
                case "LocalDateTime": return "DateTimeAsDateTime";
                case "Map": return "Dictionary";
                default: return javaType;
            }
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

    private List<QueryParam> createQueryParams(List<Parameter> params){
        List<QueryParam> queryParams = new ArrayList<>();
        for(Parameter param: params){
            String name = param.getNameAsString();
            String key = helper.toHyphenChain(name);
            queryParams.add(new QueryParam(key, name));
        }
        return queryParams;
    }

    private static boolean isBodyParam(Parameter param){
        Type type = param.getType();
        if( type.isPrimitiveType() ){
            return false;
        } else {
            switch(type.toString()){
                case "String": case "Character": case "Integer": case "Boolean": case "Double":
                case "LocalDate": case "LocalDateTime":
                    return false;
                default:
                    return true;
            }
        }
    }

    private static class PostMethodParams {
        private List<Parameter> regularParameters = new ArrayList<>();
        private String bodyParameter;
        private String bodyParameterType;

        PostMethodParams(List<Parameter> params){
            for(Parameter param: params){
                if( isBodyParam(param) ){
                    if( bodyParameter != null ){
                        throw new RuntimeException("Too many body params: " + param.getNameAsString());
                    }
                    this.bodyParameter = param.getNameAsString();
                    this.bodyParameterType = toCSharpType(param.getType().toString());
                } else {
                    regularParameters.add(param);
                }
            }
            if( bodyParameter == null ){
                this.bodyParameter = "null";
                this.bodyParameterType = "object";
            }
        }

        List<Parameter> getRegularParameters() {
            return regularParameters;
        }

        String getBodyParameter() {
            return bodyParameter;
        }

        String getBodyParameterType(){
            return bodyParameterType;
        }
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

    private String getRetType(Type serverRetType){
        String cType = toCSharpType(serverRetType.toString());
        if( cType.equals("void") ){
            return "Task";
        } else {
            return "Task<" + cType + ">";
        }
    }

}
