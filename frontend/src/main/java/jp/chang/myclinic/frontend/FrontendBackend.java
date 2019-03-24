package jp.chang.myclinic.frontend;

import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.dto.PatientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class FrontendBackend /* implements Frontend */ {

    private DbBackend dbBackend;

    public FrontendBackend(DbBackend dbBackend){
        this.dbBackend = dbBackend;
    }

    private <T> CompletableFuture<T> query(DbBackend.QueryStatement<T> q){
        return CompletableFuture.completedFuture(dbBackend.query(q));
    }

//    @Override
//    public CompletableFuture<PatientDTO> getPatient(int patientId) {
//        return query(backend -> backend.getPatient(patientId));
//    }

}
