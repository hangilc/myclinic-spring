package jp.chang.myclinic.apitool.lib;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.UnknownType;
import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import jp.chang.myclinic.apitool.MysqlConnectionProvider;
import jp.chang.myclinic.apitool.PgsqlConnectionProvider;
import jp.chang.myclinic.apitool.SqliteConnectionProvider;
import jp.chang.myclinic.apitool.lib.tables.Config;
import jp.chang.myclinic.apitool.lib.tables.MysqlConfig;
import jp.chang.myclinic.apitool.lib.tables.PgsqlConfig;
import jp.chang.myclinic.apitool.lib.tables.SqliteConfig;
import jp.chang.myclinic.dto.annotation.AutoInc;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.*;
import java.util.function.Supplier;

import static com.github.javaparser.ast.NodeList.nodeList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class Helper {

    private static Helper helper = new Helper();

    public static Helper getInstance() {
        return helper;
    }

    public String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public String snakeToCapital(String s) {
        return Arrays.stream(s.split("_"))
                .map(this::capitalize)
                .collect(joining(""));
    }

    public String snakeToCamel(String s) {
        String capital = snakeToCapital(s);
        return capital.substring(0, 1).toLowerCase() + capital.substring(1, capital.length());
    }

    public String toSnake(String s) {
        s = s.substring(0, 1).toLowerCase() + s.substring(1);
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (Character.isUpperCase(c)) {
                sb.append('_');
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public String toHyphenChain(String s) {
        return toSnake(s).replaceAll("_", "-");
    }

    private static NumberFormat numberFormat = NumberFormat.getNumberInstance();

    public static String formatNumber(double number) {
        return numberFormat.format(number);
    }

    public void saveToFile(Path path, String src, boolean override) {
        if (Files.exists(path)) {
            if (!override) {
                return;
            }
        }
        try {
            Files.write(path, src.getBytes());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public String formatSource(String source) {
        Formatter formatter = new Formatter();
        try {
            return formatter.formatSource(source);
        } catch (FormatterException e) {
            throw new RuntimeException(e);
        }

    }

    public ClassOrInterfaceType createGenericType(String name, String paramType) {
        return new ClassOrInterfaceType(null, new SimpleName(name),
                NodeList.nodeList(new ClassOrInterfaceType(null, paramType)));
    }

    public ClassOrInterfaceType createGenericType(String name, Type paramType) {
        return new ClassOrInterfaceType(null, new SimpleName(name),
                NodeList.nodeList(paramType));
    }

    public ClassOrInterfaceType createGenericType(String name, String paramType1, String paramType2) {
        return createGenericType(name, createGenericType(paramType1, paramType2));
    }

    public Parameter createSingleLambdaParameter(String name) {
        return new Parameter(new UnknownType(), name);
    }

    public NodeList<Parameter> createParameters(String... paramNames) {
        return NodeList.nodeList(Arrays.stream(paramNames)
                .map(name -> new Parameter(new UnknownType(), name))
                .collect(toList()));
    }

    public Expression methodCall(String scope, String method, Expression arg) {
        return new MethodCallExpr(new NameExpr(scope), new SimpleName(method), nodeList(arg));
    }

    public Expression methodCall(Expression scope, String method, Expression arg) {
        return new MethodCallExpr(scope, new SimpleName(method), nodeList(arg));
    }

    public Expression methodCall(Expression scope, String method) {
        return new MethodCallExpr(scope, new SimpleName(method), nodeList());
    }

    // Integer -> Integer.class
    public Expression classLiteral(String className) {
        return new FieldAccessExpr(new NameExpr(className), "class");
    }

    public Expression classLiteral(Class<?> cls) {
        return classLiteral(cls.getSimpleName());
    }

    public static class ColumnInfo {
        public String name;
        public int sqlType;
        public String dbTypeName;
    }

    public List<ColumnInfo> listColumns(DatabaseMetaData meta, String tableName) throws SQLException {
        List<ColumnInfo> columns = new ArrayList<>();
        ResultSet rs = meta.getColumns(null, "public", tableName, "%");
        while (rs.next()) {
            ColumnInfo ci = new ColumnInfo();
            ci.name = rs.getString("COLUMN_NAME");
            ci.sqlType = rs.getInt("DATA_TYPE");
            ci.dbTypeName = rs.getString("TYPE_NAME");
            columns.add(ci);
        }
        rs.close();
        return columns;
    }

    public Class<?> getDTOFieldClass(Class<?> dtoClass, String fieldName) {
        try {
            Field field = dtoClass.getField(fieldName);
            Class<?> c = field.getType();
            return getBoxedClass(c);
        } catch (NoSuchFieldException e) {
            String msg = String.format("Cannot find %s in %s", fieldName, dtoClass.getSimpleName());
            throw new RuntimeException(msg);
        }
    }

    public Class<?> getBoxedClass(Class<?> c) {
        if (c == int.class) {
            return Integer.class;
        } else if (c == double.class) {
            return Double.class;
        } else if (c == char.class) {
            return Character.class;
        } else if (c == void.class) {
            return Void.class;
        } else {
            return c;
        }
    }

    public Type getBoxedType(Type t) {
        if (t.isPrimitiveType()) {
            switch (t.toString()) {
                case "int":
                    return new ClassOrInterfaceType(null, "Integer");
                case "double":
                    return new ClassOrInterfaceType(null, "Double");
                case "char":
                    return new ClassOrInterfaceType(null, "Character");
                default:
                    throw new RuntimeException("Cannot convert to boxed type: " + t);
            }
        } else if (t.isVoidType()) {
            return new ClassOrInterfaceType(null, "Void");
        } else {
            return t;
        }
    }

    public static class AutoIncInfo {
        public Parameter param;
        public List<String> fieldNames = new ArrayList<>();
        public Class<?> autoIncClass;

        @Override
        public String toString() {
            return "AutoIncInfo{" +
                    "param=" + param +
                    ", fieldNames=" + fieldNames +
                    ", autoIncClass=" + autoIncClass +
                    '}';
        }
    }

    public AutoIncInfo scanAutoInc(Collection<Parameter> parameters) {
        AutoIncInfo info = new AutoIncInfo();
        List<String> fields = Collections.emptyList();
        for (Parameter param : parameters) {
            Class<?> c = DtoClassList.getDtoClassByName(param.getTypeAsString());
            if (c != null) {
                scanAutoIncIter(param, c, fields, info, parameters);
            }
        }
        return info;
    }

    private void scanAutoIncIter(Parameter param, Class<?> dtoClass, List<String> fields, AutoIncInfo info,
                                 Collection<Parameter> parameters) {
        for (Field field : dtoClass.getFields()) {
            fields = new ArrayList<>(fields);
            fields.add(field.getName());
            if (field.isAnnotationPresent(AutoInc.class)) {
                if (info.autoIncClass != null) {
                    System.err.println("Multiple auto incs: " + parameters);
                    System.exit(1);
                }
                info.param = param;
                info.fieldNames = fields;
                info.autoIncClass = field.getType();
            }
            Class<?> cls = DtoClassList.getDtoClassByName(field.getType().getSimpleName());
            if (cls != null) {
                scanAutoIncIter(param, cls, fields, info, parameters);
            }
        }
    }

    public Config getSpecifics(String database) {
        switch (database) {
            case "mysql":
                return new MysqlConfig();
            case "sqlite":
                return new SqliteConfig();
            case "pgsql":
                return new PgsqlConfig();
            default:
                throw new RuntimeException("Cannot find database specifics: " + database);
        }
    }

    public Supplier<Connection> getConnectionProvider(String database) {
        switch (database) {
            case "mysql":
                return new MysqlConnectionProvider();
            case "sqlite":
                return new SqliteConnectionProvider();
            case "pgsql":
                return new PgsqlConnectionProvider();
            default:
                throw new RuntimeException("Cannot find database specifics: " + database);
        }
    }

    public Path getBackendSourcePath() throws IOException {
        return Paths.get("backend-db/src/main/java/jp/chang/myclinic/backenddb", "Backend.java");
    }

    public Path getBackendServerSourcePath() throws IOException {
        return Paths.get("backend-server/src/main/java/jp/chang/myclinic/backendserver", "RestServer.java");
    }

    public Path getFrontendRestSourcePath() throws IOException {
        return Paths.get("frontend/src/main/java/jp/chang/myclinic/frontend", "FrontendRest.java");
    }

    public List<MethodDeclaration> listUnimplementedMethods(ClassOrInterfaceDeclaration backend,
                                                            ClassOrInterfaceDeclaration target) {
        List<MethodDeclaration> result = new ArrayList<>();
        for (MethodDeclaration backendMethod : backend.getMethods()) {
            if (!backendMethod.isPublic() || backendMethod.isAnnotationPresent("ExcludeFromFrontend")) {
                continue;
            }
            CallableDeclaration.Signature backendSig = backendMethod.getSignature();
            if (target.getCallablesWithSignature(backendSig).size() == 0) {
                result.add(backendMethod);
            }
        }
        return result;
    }

    public Class<?> getDtoClassNamed(String name) {
        return DtoClassList.getDtoClassByName(name);
    }

    public boolean isDtoClassName(String name) {
        return getDtoClassNamed(name) != null;
    }

}
