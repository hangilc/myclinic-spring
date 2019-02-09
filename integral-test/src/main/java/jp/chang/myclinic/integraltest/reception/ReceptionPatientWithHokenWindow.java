package jp.chang.myclinic.integraltest.reception;

import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtGrpc.*;
import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtOuterClass.*;

import jp.chang.myclinic.integraltest.GrpcBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReceptionPatientWithHokenWindow extends ReceptionWindow {

    //private static Logger logger = LoggerFactory.getLogger(ReceptionPatientWithHokenWindow.class);

    public static ReceptionPatientWithHokenWindow findCreated(ReceptionMgmtBlockingStub receptionStub){
        WindowType windowType = findCreatedWindow(receptionStub::findCreatedPatientWithHokenWindow);
        return new ReceptionPatientWithHokenWindow(receptionStub, windowType);
    }

    private ReceptionPatientWithHokenWindow(ReceptionMgmtBlockingStub receptionStub, WindowType windowType) {
        super(receptionStub, windowType);
    }

    public ReceptionNewShahokokuhoWindow clickNewShahokokuhoButton(){
        boolean ok = receptionStub.clickEditPatientNewShahokokuhoButton(windowType).getValue();
        if( !ok ){
            throw new RuntimeException("Clicking new shahokokuho buton failed.");
        }
        WindowType newWindowType = findCreatedWindow(receptionStub::findCreatedNewShahokokuhoWindow);
        return new ReceptionNewShahokokuhoWindow(receptionStub, newWindowType);
    }

}
