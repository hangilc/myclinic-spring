package jp.chang.myclinic.backendtester;

import jp.chang.myclinic.backenddb.DB;
import jp.chang.myclinic.backenddb.DBImpl;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.SerialDB;
import jp.chang.myclinic.backenddb.test.Tester;
import jp.chang.myclinic.backendsqlite.SqliteDataSource;
import jp.chang.myclinic.backendsqlite.SqliteTableSet;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import javax.sql.DataSource;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Command(name = "sqlite", mixinStandardHelpOptions = true)
public class TestSqlite implements Runnable {

    @Option(names = "--thread")
    private int threadSize = 1;

    @Parameters(paramLabel = "SQLite db file", arity = "0..1", description = "SQLite db file to use for testing")
    private String dbFile = Paths.get(System.getProperty("user.home"), "sqlite-data",
            "myclinic-test-sqlite.db").toString();

    @Override
    public void run() {
        DataSource ds = SqliteDataSource.createTemporaryFromDbFile(dbFile);
        DB db = new DBImpl(ds);
        if( threadSize == 1 ) {
            DbBackend dbBackend = new DbBackend(db, SqliteTableSet::create);
            new Tester().test(dbBackend);
        } else {
            db = new SerialDB(db);
            DbBackend dbBackend = new DbBackend(db, SqliteTableSet::create);
            List<Future<Void>> futures = new ArrayList<>();
            for(int i=0;i<threadSize;i++){
                ExecutorService service = Executors.newSingleThreadExecutor();
                Future<Void> future = service.submit(() -> {
                    new Tester().test(dbBackend);
                    return null;
                });
                futures.add(future);
            }
            try {
                for (Future future : futures) {
                    future.get();
                }
            } catch(Exception ex){
                ex.printStackTrace();
                System.exit(1);
            }
        }
    }
}

