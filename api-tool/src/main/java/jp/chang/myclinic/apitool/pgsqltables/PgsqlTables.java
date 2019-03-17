package jp.chang.myclinic.apitool.pgsqltables;

import com.github.javaparser.ast.CompilationUnit;
import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import jp.chang.myclinic.apitool.PgsqlConnectionProvider;
import jp.chang.myclinic.apitool.lib.Helper;
import jp.chang.myclinic.apitool.lib.gentablebase.DatabaseSpecifics;
import jp.chang.myclinic.apitool.lib.gentablebase.Table;
import jp.chang.myclinic.apitool.lib.gentablebase.TableBaseGenerator;
import jp.chang.myclinic.apitool.lib.gentablebase.TableLister;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Command(name = "pgsql-tables", description = "creates TableBase.java files in backend-pgsql")
public class PgsqlTables implements Runnable {

    @CommandLine.Option(names = {"--dry-run"})
    public boolean dryRun;

    private DatabaseSpecifics dbSpecs = new PgsqlSpecifics();
    private String basePackage = "jp.chang.myclinic.backendpgsql";
    private Path baseDir = Paths.get("backend-pgsql/src/main/java/jp/chang/myclinic/backendpgsql");
    private Helper helper = Helper.getInstance();

    @Override
    public void run(){
        Formatter formatter = new Formatter();
        try( Connection conn = PgsqlConnectionProvider.get() ){
            TableLister tableLister = new TableLister(dbSpecs);
            List<Table> tables = tableLister.listTables(conn);
            for(Table table: tables){
//                if( !table.getName().equals("patient") ){
//                    continue;
//                }
                TableBaseGenerator gen = new TableBaseGenerator(table, dbSpecs);
                gen.setBasePackage(basePackage);
                CompilationUnit unit = gen.generate();
                String src = formatter.formatSource(unit.toString());
                save(table, src);
            }
        } catch(SQLException | FormatterException e){
            throw new RuntimeException(e);
        }
    }

    private void save(Table table, String src){
        String file = helper.snakeToCapital(table.getName()) + "TableBase.java";
        Path path = baseDir.resolve("tablebase").resolve(file);
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
