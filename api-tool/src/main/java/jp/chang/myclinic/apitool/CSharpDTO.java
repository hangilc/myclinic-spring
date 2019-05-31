package jp.chang.myclinic.apitool;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import jp.chang.myclinic.apitool.lib.DtoClassList;
import jp.chang.myclinic.apitool.lib.Helper;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import picocli.CommandLine.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Command(name = "csharp-dto")
class CSharpDTO implements Runnable {
    @Option(names = "-o", description = "output file")
    private String outputFile;

    @Option(names = "--target", description = "database or composite; database is the default")
    private String target = "database";

    private Helper helper = Helper.getInstance();

    public static class Member {
        private String type;
        private String name;
        private String paramName;

        public Member(String type, String name, String paramName) {
            this.type = type;
            this.name = name;
            this.paramName = paramName;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public String getParamName() {
            return paramName;
        }
    }

    public static class DtoClassInfo {
        private String className;
        private List<Member> members = new ArrayList<>();

        public DtoClassInfo(String className) {
            this.className = className;
        }

        private void addMember(Member member) {
            this.members.add(member);
        }

        public String getClassName() {
            return className;
        }

        public List<Member> getMembers() {
            return members;
        }
    }

    @Override
    public void run() {
        try {
            Velocity.init();
            List<DtoClassInfo> infoList = getClassList();
            StringBuilder sb = new StringBuilder();
            for (DtoClassInfo info: infoList) {
                VelocityContext context = new VelocityContext();
                context.put("data", info);
                Template template = Velocity.getTemplate("api-tool/template/CSharpDTO.vm");
                StringWriter writer = new StringWriter();
                template.merge(context, writer);
                sb.append(writer.toString());
            }
            output(sb.toString());
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private List<DtoClassInfo> getClassList() throws IOException {
        switch (target) {
            case "database":
                return getDatabaseDTO();
            case "composite":
                return getCompositeDTO();
            default: {
                System.err.println("Unknown target: " + target);
                System.err.println("Valid targets are 'default' and 'composite'.");
                System.exit(1);
                return Collections.emptyList();
            }
        }
    }

    private List<Class<?>> listDatabaseDtoClasses() {
        return DtoClassList.getList().stream()
                .filter(cls -> {
                    String name = cls.getSimpleName();
                    if (name.equals("DiseaseNewDTO")) {
                        return false;
                    } else {
                        return true;
                    }
                })
                .collect(toList());
    }

    private List<DtoClassInfo> getDatabaseDTO() {
        List<DtoClassInfo> infoList = new ArrayList<>();
        for (Class<?> dtoClass : listDatabaseDtoClasses()) {
            String name = dtoClass.getSimpleName();
            DtoClassInfo info = new DtoClassInfo(name);
            infoList.add(info);
            for (Field field : dtoClass.getFields()) {
                Class<?> type = field.getType();
                String typeName = type.getName();
                if (typeName.equals("Integer")) {
                    typeName = "int";
                }
                Member member = new Member(
                        typeName,
                        helper.capitalize(field.getName()),
                        field.getName()
                );
                info.addMember(member);
            }
        }
        return infoList;
    }

    private List<Class<?>> listCompositeDtoClasses() {
        List<Class<?>> allList = DtoClassList.getAllList();
        Set<Class<?>> dbList = new HashSet<>(DtoClassList.getList());
        return allList.stream()
                .filter(cls -> {
                    if (cls.getSimpleName().equals("DiseaseNewDTO")) {
                        return true;
                    } else if (dbList.contains(cls)) {
                        return false;
                    } else {
                        return true;
                    }
                })
                .collect(toList());
    }

    private List<DtoClassInfo> getCompositeDTO() throws IOException {
        List<DtoClassInfo> infoList = new ArrayList<>();
        List<Class<?>> dtoClasses = listCompositeDtoClasses();
        for (Class<?> dtoClass : dtoClasses) {
            String name = dtoClass.getSimpleName();
            DtoClassInfo info = new DtoClassInfo(name);
            Path srcPath = Paths.get("./dto/src/main/java/jp/chang/myclinic/dto", name + ".java");
            CompilationUnit unit = StaticJavaParser.parse(srcPath);
            ClassOrInterfaceDeclaration decl = unit.getClassByName(name)
                    .orElseThrow(() -> new RuntimeException("Cannot find class: " + name));
            for (FieldDeclaration field : decl.getFields()) {
                String typeName = field.getElementType().toString();
                if( typeName.equals("Integer") ){
                    typeName = "int";
                } else if( typeName.equals("List<Integer>") ){
                    typeName = "List<int>";
                }
                VariableDeclarator varDecl = field.getVariables().get(0);
                String fieldName = varDecl.getNameAsString();
                Member member = new Member(
                        typeName,
                        helper.capitalize(fieldName),
                        fieldName
                );
                info.addMember(member);
            }
            infoList.add(info);
        }
        return infoList;
    }

    private void output(String result) {
        if (outputFile != null) {
            try {
                Files.write(Path.of(outputFile), result.getBytes());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        } else {
            System.out.println(result);
        }
    }
}
