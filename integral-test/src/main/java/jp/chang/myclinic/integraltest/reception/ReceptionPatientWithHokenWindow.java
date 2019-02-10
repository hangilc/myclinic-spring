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
        return new ReceptionNewShahokokuhoWindow(receptionStub, newWindowType, getPatientId());
    }

    public ReceptionNewKoukikoureiWindow clickNewKoukikoureiButton(){
        boolean ok = receptionStub.clickEditPatientNewKoukikoureiButton(windowType).getValue();
        if( !ok ){
            throw new RuntimeException("Clicking new koukikourei buton failed.");
        }
        WindowType newWindowType = findCreatedWindow(receptionStub::findCreatedNewKoukikoureiWindow);
        return new ReceptionNewKoukikoureiWindow(receptionStub, newWindowType, getPatientId());
    }

    public ReceptionNewKouhiWindow clickNewKouhiButton(){
        boolean ok = receptionStub.clickEditPatientNewKouhiButton(windowType).getValue();
        if( !ok ){
            throw new RuntimeException("Clicking new kouhi buton failed.");
        }
        WindowType newWindowType = findCreatedWindow(receptionStub::findCreatedNewKouhiWindow);
        return new ReceptionNewKouhiWindow(receptionStub, newWindowType, getPatientId());
    }

    public ReceptionRegisterForPracticeWindow clickRegisterButton(){
        boolean ok = receptionStub.clickEditPatientRegisterForPracticeButton(windowType).getValue();
        if( !ok ){
            throw new RuntimeException("Clicking register button failed (patient with hoken window).");
        }
        WindowType registerWindow = findCreatedWindow(receptionStub::findCreatedRegisterForPracticeWindow);
        return new ReceptionRegisterForPracticeWindow(receptionStub, registerWindow);
    }

    public void clickCloseButton(){
        boolean ok = receptionStub.clickEditPatientCloseButton(windowType).getValue();
        if( !ok ){
            throw new RuntimeException("Clicking close button failed (patient with hoken).");
        }
    }

    private int getPatientId(){
        return receptionStub.getPatientWithHokenWindowPatientId(windowType).getPatientId();
    }

}
