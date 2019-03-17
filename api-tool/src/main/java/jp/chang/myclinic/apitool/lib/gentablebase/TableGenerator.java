package jp.chang.myclinic.apitool.lib.gentablebase;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import jp.chang.myclinic.apitool.databasespecifics.DatabaseSpecifics;
import jp.chang.myclinic.apitool.lib.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TableGenerator {

    private Table table;
    private DatabaseSpecifics dbSpecs;
    private String basePackage;
    private Helper helper = Helper.getInstance();

    public TableGenerator(Table table, DatabaseSpecifics dbSpecs) {
        this.table = table;
        this.dbSpecs = dbSpecs;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public CompilationUnit generate(){
        CompilationUnit unit = new CompilationUnit();
        unit.setPackageDeclaration(basePackage + ".table");
        String tableClassName = helper.snakeToCapital(table.getName()) + "Table";
        unit.addImport(basePackage + ".tablebase." + tableClassName + "Base");
        ClassOrInterfaceDeclaration classDecl = unit.addClass(tableClassName);
        classDecl.addExtendedType(tableClassName + "Base");
        return unit;
    }
}
