package jp.chang.myclinic.apitool.sqlitetables;

import jp.chang.myclinic.apitool.SqliteConnectionProvider;
import jp.chang.myclinic.apitool.lib.gentablebase.TableTypesLister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.sql.Connection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@CommandLine.Command(name = "sqlite-tables")
public class SqliteTables implements Runnable {
    @CommandLine.Option(names = {"--show-types"})
    private boolean showTypes;

    @Override
    public void run() {
        if( showTypes ){
            runShowTypes();
            return;
        }
        try(Connection conn = SqliteConnectionProvider.get()){
            System.out.println(conn);
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private void runShowTypes(){
        try(Connection conn = SqliteConnectionProvider.get()){
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
}
