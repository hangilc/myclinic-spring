package jp.chang.myclinic.integraltest.practice;

import jp.chang.myclinic.Common.VisitType;
import jp.chang.myclinic.integraltest.GrpcHelper;
import jp.chang.myclinic.practice.grpc.generated.PracticeMgmtGrpc.PracticeMgmtBlockingStub;

public class Record {

    private int visitId;
    private PracticeMgmtBlockingStub practiceStub;
    private GrpcHelper helper = new GrpcHelper();

    Record(int visitId, PracticeMgmtBlockingStub practiceStub) {
        this.visitId = visitId;
        this.practiceStub = practiceStub;
    }

    public void clickNewTextButton(){
        VisitType visitType = VisitType.newBuilder().setVisitId(visitId).build();
        boolean ok = practiceStub.clickNewTextButton(visitType).getValue();
        if( !ok ){
            throw new RuntimeException("Clicking new text button failed.");
        }
    }

}
