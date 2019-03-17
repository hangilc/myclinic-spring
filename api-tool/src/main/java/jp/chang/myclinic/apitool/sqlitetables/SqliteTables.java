package jp.chang.myclinic.apitool.sqlitetables;

import jp.chang.myclinic.apitool.SqliteConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.sql.Connection;

@CommandLine.Command(name = "sqlite-tables")
public class SqliteTables implements Runnable {

    @Override
    public void run() {
        try(Connection conn = SqliteConnectionProvider.get()){
            System.out.println(conn);
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
