package jp.chang.myclinic.apitool.pgsqltables;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.google.inject.Inject;
import jp.chang.myclinic.apitool.lib.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TableCodeGenerator {

    @Inject
    private Helper helper;

    private String tableCapital;
    private String className;

    CompilationUnit generate(Table table) {
        this.tableCapital = helper.snakeToCapital(table.getName());
        this.className = tableCapital + "Table";
        CompilationUnit unit = new CompilationUnit();
        unit.setPackageDeclaration("jp.chang.myclinic.backendpgsql.table");
        unit.addImport(
                String.format("jp.chang.myclinic.backendpgsql.tablebase.%sBase", className)
        );
        addClass(unit);
        return unit;
    }

    private void addClass(CompilationUnit unit) {
        ClassOrInterfaceDeclaration classDecl = unit.addClass(className, Modifier.Keyword.PUBLIC);
        classDecl.addExtendedType(new ClassOrInterfaceType(null, tableCapital + "Table"));
    }

}
