package jp.chang.myclinic.apitool.pgsqltables;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import jp.chang.myclinic.apitool.Main;
import picocli.CommandLine.Command;

import java.sql.Connection;

@Command(name = "pgsql-tables")
public class PgsqlTables implements Runnable{

    @Inject @Named("pgsql")
    private Connection conn;

    @Override
    public void run(){
        System.out.println("pgsql-tables");
        System.out.println(conn);
    }

}
