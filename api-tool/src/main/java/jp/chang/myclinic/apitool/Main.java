package jp.chang.myclinic.apitool;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.name.Names;
import jp.chang.myclinic.apitool.pgsqltables.PgsqlTables;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@Command(name="api-tool")
public class Main {

    public static void main(String[] args){
        Injector injector = Guice.createInjector(new AbstractModule(){
            @Override
            protected void configure() {
                bind(Connection.class).annotatedWith(Names.named("pgsql"))
                        .toProvider(PgsqlConnectionProvider.class);
            }
        });
        CommandLine commandLine = new CommandLine(new Main());
        List<CommandLine> parsedResults = commandLine
                .addSubcommand("pgsql-tables", new PgsqlTables())
                .parse(args);
        if( parsedResults.size() != 2 ){
            commandLine.usage(System.out);
            System.exit(1);
        }
        Runnable sub = parsedResults.get(1).getCommand();
        injector.injectMembers(sub);
        sub.run();
    }

    public static class PgsqlConnectionProvider implements Provider<Connection> {

        @Override
        public Connection get() {
            try {
                return DriverManager.getConnection("jdbc:postgresql://localhost/myclinic",
                        System.getenv("MYCLINIC_POSTGRES_USER"), System.getenv("MYCLINIC_POSTGRES_PASS"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
