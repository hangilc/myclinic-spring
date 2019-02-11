package jp.chang.myclinic.integraltest.practice;

import com.google.protobuf.Empty;
import jp.chang.myclinic.Common.*;
import jp.chang.myclinic.integraltest.GrpcHelper;
import jp.chang.myclinic.practice.grpc.generated.PracticeMgmtGrpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PracticeMainWindow {

    private static Logger logger = LoggerFactory.getLogger(PracticeMainWindow.class);
    private PracticeMgmtBlockingStub practiceStub;
    private GrpcHelper helper = new GrpcHelper();

    public PracticeMainWindow(PracticeMgmtBlockingStub practiceStub) {
        this.practiceStub = practiceStub;
    }

    public SelectVisitWindow chooseSelectVisitMenu(){
        boolean ok = practiceStub.chooseSelectVisitMenuInMainPane(Empty.getDefaultInstance())
                .getValue();
        if( !ok ){
            throw new RuntimeException("Choosing select visit menu failed (practice main).");
        }
        return findSelectVisitWindow();
    }

    public SelectVisitWindow findSelectVisitWindow(){
        WindowType windowType =  helper.findCreatedWindow(practiceStub::findSelectVisitWindow);
        return new SelectVisitWindow(practiceStub, windowType);
    }

}
