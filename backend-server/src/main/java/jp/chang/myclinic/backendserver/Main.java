package jp.chang.myclinic.backendserver;

import jp.chang.myclinic.backenddb.DB;
import jp.chang.myclinic.backenddb.DBImpl;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backendsqlite.SqliteDataSource;
import jp.chang.myclinic.backendsqlite.SqliteTableSet;
import jp.chang.myclinic.support.SupportSet;
import jp.chang.myclinic.support.SupportSetProvider;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import picocli.CommandLine;
import picocli.CommandLine.*;

import javax.sql.DataSource;
import java.nio.file.Paths;

@Command(name = "backend-server")
public class Main implements Runnable {

    @Option(names = "--port", description = "TCP/IP port to listen to.")
    int port = 38080;

    public static void main( String[] args ){
        CommandLine.run(new Main(), args);
    }

    @Override
    public void run() {
        try {
            SupportSet ss = SupportSetProvider.createDefault();
            String dbFile = Paths.get(System.getProperty("user.home"),
                    "sqlite-data", "myclinic-test-sqlite.db").toString();
            DataSource ds = SqliteDataSource.createTemporaryFromDbFile(dbFile);
            DB db = new DBImpl(ds);
            DbBackend dbBackend = new DbBackend(db, SqliteTableSet::create);
//        DataSource ds = MysqlDataSourceProvider.create();
//        DB db = new DBImpl(ds);
//        DbBackend dbBackend = new DbBackend(db, MysqlTableSet::create);
            Server server = new Server(port);
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);
            ServletContainer container = new ServletContainer(new JerseyConfig(dbBackend, ss));
            ServletHolder jersey = new ServletHolder(container);
            jersey.setInitOrder(0);
            context.addServlet(jersey, "/*");
            server.start();
            System.out.printf("Server listening to port %d\n", port);
            server.join();
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}

