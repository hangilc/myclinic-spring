package jp.chang.myclinic.backendsqlite;

import jp.chang.myclinic.backenddb.Backend;
import jp.chang.myclinic.backenddb.test.Tester;
import jp.chang.myclinic.backenddb.DB;
import jp.chang.myclinic.dto.PatientDTO;

import javax.sql.DataSource;

public class Main {

    public static void main(String[] args) {
        DataSource ds = SqliteDatabase.createTemporaryFromDbFile("backend-sqlite/src/main/resources/test.db");
        DB db = new DB(ds);
        Backend backend = SqliteBackend.create(db);
        db.proc(() -> {
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

