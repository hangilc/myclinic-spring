package jp.chang.myclinic.apitool.tableinterface;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import jp.chang.myclinic.apitool.PgsqlConnectionProvider;
import jp.chang.myclinic.apitool.databasespecifics.DatabaseSpecifics;
import jp.chang.myclinic.apitool.databasespecifics.PgsqlSpecifics;
import jp.chang.myclinic.apitool.lib.Helper;
import jp.chang.myclinic.apitool.lib.gentablebase.Column;
import jp.chang.myclinic.apitool.lib.gentablebase.Table;
import jp.chang.myclinic.apitool.lib.gentablebase.TableLister;
import picocli.CommandLine;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static com.github.javaparser.ast.NodeList.nodeList;

@CommandLine.Command(name = "table-interface")
public class TableInterface implements Runnable {

    @CommandLine.Option(names = {"--dry-run"})
    private boolean dryRun;

    private DatabaseSpecifics dbSpecs = new PgsqlSpecifics();
    private String basePackage = "jp.chang.myclinic.backenddb.tableinterface";
    private Path baseDir = Paths.get("backend-db/src/main/java/jp/chang/myclinic/backenddb/tableinterface");
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
        Class<?> dtoClass = dbSpecs.mapTableNameToDtoClass(table.getName());
        String dtoClassName = dtoClass.getSimpleName();
        CompilationUnit unit = new CompilationUnit();
        unit.setPackageDeclaration(basePackage);
        unit.addImport("jp.chang.myclinic.backenddb.*");
        switch(dtoClassName){
            case "PracticeLogDTO":
                unit.addImport("jp.chang.myclinic.logdto.practicelog.PracticeLogDTO");
                break;
            case "HotlineLogDTO":
                unit.addImport("jp.chang.myclinic.logdto.hotline.HotlineLogDTO");
                break;
            default:
                unit.addImport("jp.chang.myclinic.dto." + dtoClassName);
                break;
        }
        String interfaceName = helper.snakeToCapital(table.getName()) + "TableInterface";
        ClassOrInterfaceDeclaration interfaceDecl = unit.addInterface(interfaceName);
        interfaceDecl.addExtendedType(helper.createGenericType("TableInterface", dtoClassName));
        interfaceDecl.addExtendedType(new ClassOrInterfaceType(
                new ClassOrInterfaceType(null, "Query"), new SimpleName("Projector"),
                nodeList(new ClassOrInterfaceType(null, dtoClassName))
        ));
        interfaceDecl.addExtendedType(new ClassOrInterfaceType(
                new ClassOrInterfaceType(null, "SqlTranslator"), "TableInfo"
        ));
        return unit;
    }

    private void save(Table table, String src){
        String file = helper.snakeToCapital(table.getName()) + "TableInterface.java";
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
