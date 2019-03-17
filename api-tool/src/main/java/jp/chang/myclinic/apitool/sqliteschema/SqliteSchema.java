package jp.chang.myclinic.apitool.sqliteschema;

import jp.chang.myclinic.apitool.PgsqlConnectionProvider;
import jp.chang.myclinic.apitool.databasespecifics.PgsqlSpecifics;
import jp.chang.myclinic.apitool.lib.Helper;
import jp.chang.myclinic.apitool.lib.gentablebase.Table;
import jp.chang.myclinic.apitool.lib.gentablebase.TableLister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@CommandLine.Command(name = "sqlite-schema")
public class SqliteSchema implements Runnable {

    @CommandLine.Option(names = {"--output"})
    private String outputFile;

    private Helper helper = Helper.getInstance();

    @Override
    public void run() {
        try(Connection conn = PgsqlConnectionProvider.get()){
            List<Table> tables = new TableLister(new PgsqlSpecifics()).listTables(conn);
            StringBuilder sb = new StringBuilder();
            for(Table table: tables){
                Create create = new Create(table);
                sb.append(create.output());
                sb.append("\n");
            }
            String src = sb.toString();
            if( outputFile == null ){
                System.out.println(src);
            } else {
                helper.saveToFile(Paths.get(outputFile), src, true);
            }
        } catch(SQLException ex){
            throw new RuntimeException(ex);
        }
    }
}
