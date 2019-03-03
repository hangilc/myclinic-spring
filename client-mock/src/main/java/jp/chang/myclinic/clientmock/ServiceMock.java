package jp.chang.myclinic.clientmock;

import jp.chang.myclinic.clientmock.dbgateway.DbGatewayMock;
import jp.chang.myclinic.clientmock.dbgateway.ServerProc;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

public class ServiceMock extends ServiceAdapter {

    private DbGatewayMock dbGateway = new DbGatewayMock();
    private ServerProc serverProc = new ServerProc(dbGateway);

    private <T> CompletableFuture<T> future(T t){
        return CompletableFuture.completedFuture(t);
    }

    @Override
    public CompletableFuture<Integer> enterPatient(PatientDTO patient){
        return future(dbGateway.enterPatient(patient));
    }

    @Override
    public CompletableFuture<PatientDTO> getPatient(int patientId) {
        return future(dbGateway.getPatient(patientId));
    }

    @Override
    public CompletableFuture<Integer> startVisit(int patientId, String atDateTime) {
        LocalDateTime at = DateTimeUtil.parseSqlDateTime(atDateTime);
        return future(serverProc.startVisit(patientId, at));
    }

}
