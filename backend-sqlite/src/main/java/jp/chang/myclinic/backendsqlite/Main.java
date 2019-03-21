package jp.chang.myclinic.backendsqlite;

import jp.chang.myclinic.backend.Backend;
import jp.chang.myclinic.backenddb.DB;
import jp.chang.myclinic.dto.PatientDTO;

import javax.sql.DataSource;

public class Main {

    public static void main( String[] args )
    {
        DataSource ds = SqliteDatabase.createTemporaryFromDbFile("work/test.db");
        DB db = new DB(ds);
        Backend backend = SqliteBackend.create(db);
        db.proc(() -> {
            PatientDTO patient = backend.getPatient(1);
            System.out.println(patient);
        });

    }

}

