package jp.chang.myclinic.backendmysql;

import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.dto.PatientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    public static void main(String[] args) {
        DbBackend ctx = new DbBackend(MysqlDataSource.create(), MysqlTableSet::create);
        ctx.proc(backend -> {
            PatientDTO patient = backend.getPatient(198);
            System.out.println(patient);
        });
    }

}
