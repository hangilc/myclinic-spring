package jp.chang.myclinic.apitool.sqlitetables;

import com.github.javaparser.ast.CompilationUnit;
import com.google.googlejavaformat.java.Formatter;
import jp.chang.myclinic.apitool.SqliteConnectionProvider;
import jp.chang.myclinic.apitool.databasespecifics.SqliteSpecifics;
import jp.chang.myclinic.apitool.lib.Helper;
import jp.chang.myclinic.apitool.lib.gentablebase.Table;
import jp.chang.myclinic.apitool.lib.gentablebase.TableBaseGenerator;
import jp.chang.myclinic.apitool.lib.gentablebase.TableLister;
import jp.chang.myclinic.apitool.lib.gentablebase.TableTypesLister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@CommandLine.Command(name = "sqlite-tables")
public class SqliteTables implements Runnable {
    @CommandLine.Option(names = {"--show-types"})
    private boolean showTypes;
    @CommandLine.Option(names = {"--dry-run"})
    private boolean dryRun;

    private String basePackage = "jp.chang.myclinic.backendsqlite";
    private Path baseDir = Paths.get("backend-sqlite/src/main/java/jp/chang/myclinic/backendsqlite");
    private Helper helper = Helper.getInstance();

    @Override
    public void run() {
        if( showTypes ){
            runShowTypes();
            return;
        }
        runGenerateTableBases();
    }

    private void runGenerateTableBases(){
//        Formatter formatter = new Formatter();
//        try(Connection conn = new SqliteConnectionProvider().get()){
//            SqliteSpecifics dbSpecs = new SqliteSpecifics();
//            List<Table> tables = new TableLister(dbSpecs).listTables(conn);
//            for(Table table: tables){
//                TableBaseGenerator gen = new TableBaseGenerator(table, dbSpecs);
//                gen.setBasePackage(basePackage);
//                CompilationUnit unit = gen.generate();
//                String src = formatter.formatSource(unit.toString());
//                save(table, src);
//            }
//        } catch(Exception e){
//            throw new RuntimeException(e);
//        }
    }

    private void runShowTypes(){
        try(Connection conn = new SqliteConnectionProvider().get()){
            Map<String, List<TableTypesLister.ColumnType>> map = new TableTypesLister().listTypes(conn);
            Set<String> sqlTypeNames = new LinkedHashSet<>();
            Set<String> dbTypeNames = new LinkedHashSet<>();
            for(String table: map.keySet()){
                System.out.println(table);
                for(TableTypesLister.ColumnType ct: map.get(table)){
                    System.out.printf("  %s: %s(%d) %s\n", ct.columnName, ct.sqlTypeName, ct.sqlType, ct.dbTypeName);
                    sqlTypeNames.add(ct.sqlTypeName);
                    dbTypeNames.add(ct.dbTypeName);
                }
            }
            System.out.println("SUMMARY:");
            System.out.printf("  sqlTypeNames: %s\n", sqlTypeNames.toString());
            System.out.printf("  dbTypeNames: %s\n", dbTypeNames.toString());
        } catch(Exception e){
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
