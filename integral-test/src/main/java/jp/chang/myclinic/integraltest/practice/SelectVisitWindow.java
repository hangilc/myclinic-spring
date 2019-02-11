package jp.chang.myclinic.integraltest.practice;

import jp.chang.myclinic.Common.*;
import jp.chang.myclinic.practice.grpc.generated.PracticeMgmtGrpc.*;
import jp.chang.myclinic.practice.grpc.generated.PracticeMgmtOuterClass;

import java.util.List;

public class SelectVisitWindow extends PracticeWindow {

    SelectVisitWindow(PracticeMgmtBlockingStub practiceStube, WindowType windowType) {
        super(practiceStube, windowType);
    }

    public List<WqueueType> getWqueueList(){
        return practiceStub.listWqueue(windowType).getWqueueList();
    }

    public void chooseVisit(int visitId){
        PracticeMgmtOuterClass.ChooseVisitInSelectWindowMessage req =
                PracticeMgmtOuterClass.ChooseVisitInSelectWindowMessage.newBuilder()
                        .setWindow(windowType)
                        .setVisitId(visitId)
                        .build();
        boolean ok = practiceStub.chooseVisitInSelectVisitWindow(req).getValue();
        if( !ok ){
            throw new RuntimeException("chooseVisit failed.");
        }
    }

}
