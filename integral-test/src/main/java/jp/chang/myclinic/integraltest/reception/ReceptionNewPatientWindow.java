package jp.chang.myclinic.integraltest.reception;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.integraltest.GrpcBase;

import java.util.Optional;

import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtGrpc.*;
import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtOuterClass.*;

public class ReceptionNewPatientWindow extends ReceptionWindow {

    //private static Logger logger = LoggerFactory.getLogger(ReceptionNewPatientWindow.class);

    public ReceptionNewPatientWindow(ReceptionMgmtBlockingStub receptionStub, WindowType windowType) {
        super(receptionStub, windowType);
    }

    public void setInputs(PatientInputs inputs){
        SetNewPatientWindowInputsRequest req = SetNewPatientWindowInputsRequest.newBuilder()
                .setWindow(windowType)
                .setInputs(inputs)
                .build();
        boolean ok = receptionStub.setNewPatientWindowInputs(req).getValue();
        if (!ok) {
            throw new RuntimeException("setNewPatientWindowInputs failed.");
        }
    }

    public PatientDTO clickEnterButton(){
        int lastPatientId = getLastpatientId();
        boolean ok = receptionStub.clickNewPatientWindowEnterButton(windowType).getValue();
        if( !ok ){
            throw new RuntimeException("Clicking enter button failed (new patient window).");
        }
        return rpc(10, () -> {
                PatientDTO p = Service.api.listRecentlyRegisteredPatients(1).join().get(0);
                if (p.patientId > lastPatientId) {
                    return Optional.of(p);
                } else {
                    return Optional.empty();
                }
            });
    }

    private int getLastpatientId(){
        return Service.api.listRecentlyRegisteredPatients(1).join().get(0).patientId;
    }

}
