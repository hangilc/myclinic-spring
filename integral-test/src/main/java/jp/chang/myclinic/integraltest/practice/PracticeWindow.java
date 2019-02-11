package jp.chang.myclinic.integraltest.practice;

import jp.chang.myclinic.Common.*;
import jp.chang.myclinic.integraltest.GrpcBase;
import jp.chang.myclinic.integraltest.GrpcHelper;
import jp.chang.myclinic.practice.grpc.generated.PracticeMgmtGrpc.*;

public class PracticeWindow {

    PracticeMgmtBlockingStub practiceStub;
    WindowType windowType;
    GrpcHelper helper = new GrpcHelper();

    PracticeWindow(PracticeMgmtBlockingStub practiceStube, WindowType windowType) {
        this.practiceStub = practiceStube;
        this.windowType = windowType;
    }

}
