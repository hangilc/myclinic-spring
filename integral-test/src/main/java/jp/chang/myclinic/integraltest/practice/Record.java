package jp.chang.myclinic.integraltest.practice;

import com.google.protobuf.Int32Value;
import jp.chang.myclinic.integraltest.GrpcHelper;
import jp.chang.myclinic.practice.grpc.generated.PracticeMgmtGrpc;
import jp.chang.myclinic.practice.grpc.generated.PracticeMgmtGrpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Record {

    private int visitId;
    private PracticeMgmtBlockingStub practiceStub;
    private GrpcHelper helper = new GrpcHelper();

    Record(int visitId, PracticeMgmtBlockingStub practiceStub) {
        this.visitId = visitId;
        this.practiceStub = practiceStub;
    }

    public void clickNewTextButton(){
        boolean ok = practiceStub.clickNewTextButton(Int32Value.of(visitId)).getValue();
        if( !ok ){
            throw new RuntimeException("Clicking new text button failed.");
        }
    }

}
