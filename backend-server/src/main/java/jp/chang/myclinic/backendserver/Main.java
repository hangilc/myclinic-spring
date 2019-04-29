package jp.chang.myclinic.backendserver;

import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.SupportSet;
import jp.chang.myclinic.backendmysql.MysqlDataSourceProvider;
import jp.chang.myclinic.backendmysql.MysqlTableSet;
import jp.chang.myclinic.backendsqlite.SqliteDataSource;
import jp.chang.myclinic.backendsqlite.SqliteTableSet;
import jp.chang.myclinic.support.clinicinfo.ClinicInfoFileProvider;
import jp.chang.myclinic.support.diseaseexample.DiseaseExampleFileProvider;
import jp.chang.myclinic.support.houkatsukensa.HoukatsuKensaFile;
import jp.chang.myclinic.support.kizaicodes.KizaicodeFileResolver;
import jp.chang.myclinic.support.meisai.MeisaiServiceImpl;
import jp.chang.myclinic.support.shinryoucodes.ShinryoucodeFileResolver;
import jp.chang.myclinic.support.stockdrug.StockDrugFile;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;
import picocli.CommandLine;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main( String[] args ) throws Exception {
        CmdOpts cmdOpts = new CmdOpts();
        CommandLine commandLine = new CommandLine(cmdOpts);
        commandLine.parse(args);
        SupportSet ss = new SupportSet();
        ss.stockDrugService = new StockDrugFile(Paths.get("config/stock-drug.txt"));
        ss.houkatsuKensaService = new HoukatsuKensaFile(Paths.get("config/houkatsu-kensa.xml"));
        ss.meisaiService = new MeisaiServiceImpl();
        ss.diseaseExampleProvider = new DiseaseExampleFileProvider(Paths.get("config/disease-example.yml"));
        ss.shinryoucodeResolver = new ShinryoucodeFileResolver(new File("config/shinryoucodes.yml"));
        ss.kizaicodeResolver = new KizaicodeFileResolver(new File("config/kizaicodes.yml"));
        ss.clinicInfoProvider = new ClinicInfoFileProvider(Paths.get("config/clinic-info.yml"));
        String dbFile = Paths.get(System.getProperty("user.home"),
                    "sqlite-data", "myclinic-test-sqlite.db").toString();
        DataSource ds = SqliteDataSource.createTemporaryFromDbFile(dbFile);
        DbBackend dbBackend = new DbBackend(ds, SqliteTableSet::create, ss);
//        DataSource ds = MysqlDataSourceProvider.create();
//        DbBackend dbBackend = new DbBackend(ds, MysqlTableSet::create, ss);
        Server server = new Server(cmdOpts.port);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        ServletContainer container = new ServletContainer(new JerseyConfig(dbBackend));
        ServletHolder jersey = new ServletHolder(container);
        jersey.setInitOrder(0);
        context.addServlet(jersey, "/*");
        server.start();
        System.out.printf("Server listening to port %d\n", cmdOpts.port);
        server.join();
    }

}

