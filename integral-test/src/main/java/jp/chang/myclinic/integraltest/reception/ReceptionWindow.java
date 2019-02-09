package jp.chang.myclinic.integraltest.reception;

import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtGrpc.*;
import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtOuterClass.*;

import jp.chang.myclinic.integraltest.GrpcBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReceptionWindow extends GrpcBase {

    //private static Logger logger = LoggerFactory.getLogger(ReceptionWindow.class);
    ReceptionMgmtBlockingStub receptionStub;
    WindowType windowType;

    ReceptionWindow(ReceptionMgmtBlockingStub receptionStub, WindowType windowType) {
        this.receptionStub = receptionStub;
        this.windowType = windowType;
    }

}
