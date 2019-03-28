package jp.chang.myclinic.backendsqlite;

import jp.chang.myclinic.backenddb.Backend;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.test.Tester;
import jp.chang.myclinic.dto.PatientDTO;

import javax.sql.DataSource;

public class Test {

    public static void main(String[] args) {
        if( args.length != 1 ){
            System.err.println("Usage: Test dbFile");
            System.exit(1);
        }
        String dbFile = args[0];
        DataSource ds = SqliteDataSource.createTemporaryFromDbFile(dbFile);
        DbBackend dbBackend = new DbBackend(ds, SqliteTableSet::create);
        dbBackend.proc(backend -> {
            new Tester().test(backend);
        });

    }

}

