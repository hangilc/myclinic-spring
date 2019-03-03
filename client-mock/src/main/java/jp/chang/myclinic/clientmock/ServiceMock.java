package jp.chang.myclinic.clientmock;

import jp.chang.myclinic.clientmock.dbgateway.DbGatewayMock;
import jp.chang.myclinic.clientmock.dbgateway.ServerProc;
import jp.chang.myclinic.dto.DrugAttrDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.ShinryouAttrDTO;
import jp.chang.myclinic.dto.ShoukiDTO;
import jp.chang.myclinic.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.List;
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

    @Override
    public CompletableFuture<List<ShinryouAttrDTO>> batchGetShinryouAttr(List<Integer> shinryouIds) {
        return future(dbGateway.batchGetShinryouAttr(shinryouIds));
    }

    @Override
    public CompletableFuture<List<DrugAttrDTO>> batchGetDrugAttr(List<Integer> drugIds) {
        return future(dbGateway.batchGetDrugAttr(drugIds));
    }

    @Override
    public CompletableFuture<List<ShoukiDTO>> batchGetShouki(List<Integer> visitIds) {
        return future(dbGateway.batchGetShouki(visitIds));
    }
}
