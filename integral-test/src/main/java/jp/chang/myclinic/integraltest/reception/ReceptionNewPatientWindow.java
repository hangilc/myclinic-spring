package jp.chang.myclinic.integraltest.reception;

import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtGrpc.*;
import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtOuterClass.*;

public class ReceptionNewPatientWindow {

    //private static Logger logger = LoggerFactory.getLogger(ReceptionNewPatientWindow.class);
    private ReceptionMgmtBlockingStub receptionStub;
    private WindowType windowType;

    public ReceptionNewPatientWindow(ReceptionMgmtBlockingStub receptionStub, WindowType windowType) {
        this.receptionStub = receptionStub;
        this.windowType = windowType;
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

}
