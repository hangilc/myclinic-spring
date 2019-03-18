package jp.chang.myclinic.apitool.tablecolumnsinterface;

import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import jp.chang.myclinic.apitool.PgsqlConnectionProvider;
import jp.chang.myclinic.apitool.databasespecifics.DatabaseSpecifics;
import jp.chang.myclinic.apitool.databasespecifics.PgsqlSpecifics;
import jp.chang.myclinic.apitool.lib.Helper;
import jp.chang.myclinic.apitool.lib.gentablebase.Column;
import jp.chang.myclinic.apitool.lib.gentablebase.Table;
import jp.chang.myclinic.apitool.lib.gentablebase.TableBaseGenerator;
import jp.chang.myclinic.apitool.lib.gentablebase.TableLister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@CommandLine.Command(name = "table-columns-interface")
public class TableColumnsInterface implements Runnable {

    @CommandLine.Option(names = {"--dry-run"})
    private boolean dryRun;

    private DatabaseSpecifics dbSpecs = new PgsqlSpecifics();
    private String basePackage = "jp.chang.myclinic.backenddb.tablecolumnsinterface";
    private Path baseDir = Paths.get("backend-db/src/main/java/jp/chang/myclinic/backenddb/tablecolumnsinterface");
    private Helper helper = Helper.getInstance();

    @Override
    public void run() {
        Formatter formatter = new Formatter();
        try( Connection conn = PgsqlConnectionProvider.get() ){
            TableLister tableLister = new TableLister(dbSpecs);
            List<Table> tables = tableLister.listTables(conn);
            for(Table table: tables){
//                if( !table.getName().equals("patient") ){
//                    continue;
//                }
                CompilationUnit unit = generate(table);
                String src = formatter.formatSource(unit.toString());
                save(table, src);
            }
        } catch(SQLException | FormatterException e){
            throw new RuntimeException(e);
        }

    }

    private CompilationUnit generate(Table table){
        CompilationUnit unit = new CompilationUnit();
        unit.setPackageDeclaration(basePackage);
        String interfaceName = helper.snakeToCapital(table.getName()) + "TableColumnsInterface";
        ClassOrInterfaceDeclaration interfaceDecl = unit.addInterface(interfaceName);
        for(Column col: table.getColumns()){
            addMethod(interfaceDecl, col.getDtoField(), col.getName());
        }
        return unit;
    }

    private void addMethod(ClassOrInterfaceDeclaration decl, String fieldName, String dbColumnName){
        MethodDeclaration method = decl.addMethod(fieldName);
        method.removeBody();
        method.setType(new ClassOrInterfaceType(null, "String"));
    }

    private void save(Table table, String src){
        String file = helper.snakeToCapital(table.getName()) + "TableColumnsInterface.java";
        Path path = baseDir.resolve(file);
        System.out.println("saving: " + path.toString());
        if( dryRun ){
            System.out.println(src);
        } else {
            try {
                Files.write(path, src.getBytes());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

}
