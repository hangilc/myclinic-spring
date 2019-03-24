package jp.chang.myclinic.backendsqlite;

import jp.chang.myclinic.backenddb.Backend;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.test.Tester;
import jp.chang.myclinic.dto.PatientDTO;

import javax.sql.DataSource;

public class Main {

    public static void main(String[] args) {
        DataSource ds = SqliteDataSource.createTemporaryFromDbFile("backend-sqlite/src/main/resources/test.db");
        DbBackend dbBackend = new DbBackend(ds, SqliteTableSet::create);
        dbBackend.proc(backend -> {
            confirmMockData(backend);
            new Tester().test(backend);
        });

    }

    private static void confirmMockData(Backend backend) {
        PatientDTO patient = backend.getPatient(1);
        if (!(patient.lastName.equals("試験") && patient.firstName.equals("データ"))) {
            throw new RuntimeException("Not a test database.");
        }
    }

}

