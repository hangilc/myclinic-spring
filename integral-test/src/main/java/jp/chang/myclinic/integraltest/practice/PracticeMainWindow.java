package jp.chang.myclinic.integraltest.practice;

import com.google.protobuf.Empty;
import com.google.protobuf.Int32Value;
import jp.chang.myclinic.Common.*;
import jp.chang.myclinic.integraltest.GrpcHelper;
import jp.chang.myclinic.practice.grpc.generated.PracticeMgmtGrpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class PracticeMainWindow {

    private static Logger logger = LoggerFactory.getLogger(PracticeMainWindow.class);
    private PracticeMgmtBlockingStub practiceStub;
    private GrpcHelper helper = new GrpcHelper();
    private Empty empty = Empty.getDefaultInstance();

    public PracticeMainWindow(PracticeMgmtBlockingStub practiceStub) {
        this.practiceStub = practiceStub;
    }

    public SelectVisitWindow chooseSelectVisitMenu(){
        boolean ok = practiceStub.chooseSelectVisitMenuInMainPane(empty)
                .getValue();
        if( !ok ){
            throw new RuntimeException("Choosing select visit menu failed (practice main).");
        }
        return findSelectVisitWindow();
    }

    private SelectVisitWindow findSelectVisitWindow(){
        WindowType windowType =  helper.findCreatedWindow(practiceStub::findSelectVisitWindow);
        return new SelectVisitWindow(practiceStub, windowType);
    }

    public void waitForCurrentPatientId(int patientId){
        helper.rpc(10, () -> {
            int currentPatientId = practiceStub.getCurrentPatientId(empty).getValue();
            if( currentPatientId == patientId ){
                return Optional.of(true);
            } else {
                return Optional.empty();
            }
        });
    }

    public void confirmCurrentVisitId(int visitId){
        int currentVisitId = practiceStub.getCurrentVisitId(empty).getValue();
        if( currentVisitId != visitId ){
            throw new RuntimeException("Unexpected current visit id.");
        }
    }

    public void waitForRecordVisible(int visitId){
        helper.rpc(10, () -> {
            boolean ok = practiceStub.isRecordVisible(Int32Value.of(visitId)).getValue();
            return ok ? Optional.of(true) : Optional.empty();
        });
    }

    public Record getRecord(int visitId){
        return new Record(visitId, practiceStub);
    }

}
