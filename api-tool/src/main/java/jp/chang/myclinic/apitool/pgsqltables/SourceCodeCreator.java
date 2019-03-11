package jp.chang.myclinic.apitool.pgsqltables;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import jp.chang.myclinic.apitool.lib.Helper;

import java.util.List;

import static com.github.javaparser.ast.Modifier.Keyword;

public class SourceCodeCreator {

    private CompilationUnit unit;
    private ClassOrInterfaceDeclaration classDecl;
    private String className;
    private String dtoClassName;
    private Helper helper = new Helper();

    CompilationUnit create(Table table, Class<?> dtoClass) {
        this.unit = new CompilationUnit();
        setPackage("jp.chang.myclinic.backendpgsql.tablebase");
        List.of("jp.chang.myclinic.backendpgsql.Column",
                "jp.chang.myclinic.backendpgsql.Table",
                "jp.chang.myclinic.dto.*",
                "java.sql.Connection",
                "java.time.*",
                "java.util.*"
        ).forEach(this::addImport);
        this.className = helper.snakeToCapital(table.getName()) + "TableBase";
        this.dtoClassName = dtoClass.getSimpleName();
        addClass(className);
        declareColumnField();
        return unit;
    }

    private void setPackage(String packageName) {
        unit.setPackageDeclaration(packageName);
    }

    private void addImport(String importName) {
        unit.addImport(importName);
    }

    private void addClass(String className) {
        this.classDecl = unit.addClass(className, Keyword.PUBLIC);
        classDecl.addExtendedType(createGenericType("Table", dtoClassName));
    }

    private void declareColumnField(){
        Type fieldType = createGenericType("List", createGenericType("Column", dtoClassName));
        classDecl.addField(fieldType, "columns", Keyword.PRIVATE, Keyword.STATIC);
    }

    private ClassOrInterfaceType createGenericType(String name, String paramType) {
        return new ClassOrInterfaceType(null, new SimpleName(name),
                NodeList.nodeList(new ClassOrInterfaceType(null, paramType)));
    }

    private ClassOrInterfaceType createGenericType(String name, Type paramType) {
        return new ClassOrInterfaceType(null, new SimpleName(name),
                NodeList.nodeList(paramType));
    }

}
