package jp.chang.myclinic.apitool.lib.gentablebase;

import com.github.javaparser.ast.CompilationUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TableBaseGenerator {

    private Table table;
    private DatabaseSpecifics dbSpecs;

    public TableBaseGenerator(Table table, DatabaseSpecifics dbSpecs){
        this.table = table;
        this.dbSpecs = dbSpecs;
    }

    public CompilationUnit generate(){
        CompilationUnit unit = new CompilationUnit();
        unit.setPackageDeclaration(dbSpecs.tableBasePackage());
        List.of(dbSpecs.projectPackage() + ".Column",
                dbSpecs.projectPackage() + ".Table",
                dbSpecs.projectPackage() + ".tablebasehelper.TableBaseHelper",
                "jp.chang.myclinic.dto.*",
                "jp.chang.myclinic.logdto.practicelog.*",
                "java.sql.Connection",
                "java.time.*",
                "java.util.*",
                "java.math.BigDecimal"
        ).forEach(unit::addImport);
        return unit;
    }
}
