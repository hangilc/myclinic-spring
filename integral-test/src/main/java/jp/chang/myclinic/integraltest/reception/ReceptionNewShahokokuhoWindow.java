package jp.chang.myclinic.integraltest.reception;

import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtGrpc.*;
import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtOuterClass.*;

import jp.chang.myclinic.integraltest.GrpcBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReceptionNewShahokokuhoWindow extends ReceptionWindow {

    //private static Logger logger = LoggerFactory.getLogger(ReceptionNewShahokokuhoWindow.class);

    ReceptionNewShahokokuhoWindow(ReceptionMgmtBlockingStub receptionStub, WindowType windowType) {
        super(receptionStub, windowType);
    }

    public void setInputs(ShahokokuhoInputs inputs){
        SetNewShahokokuhoWindowInputsRequest req = SetNewShahokokuhoWindowInputsRequest.newBuilder()
                .setWindow(windowType)
                .setInputs(inputs)
                .build();
        boolean ok = receptionStub.setNewShahokokuhoWindowInputs(req).getValue();
        if (!ok) {
            throw new RuntimeException("set shahokokuho inputs failed");
        }
    }
}
