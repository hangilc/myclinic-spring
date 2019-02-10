package jp.chang.myclinic.integraltest.reception;

import jp.chang.myclinic.integraltest.GrpcBase;

import java.util.List;

import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtGrpc.*;

import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtOuterClass.*;

public class ReceptionMainWindow extends GrpcBase {

    //private static Logger logger = LoggerFactory.getLogger(ReceptionMainWindow.class);
    private ReceptionMgmtBlockingStub receptionStub;

    public ReceptionMainWindow(ReceptionMgmtBlockingStub receptionStub) {
        this.receptionStub = receptionStub;
    }

    public ReceptionNewPatientWindow clickNewPatientButton() {
        boolean ok = receptionStub.clickMainPaneNewPatientButton(null).getValue();
        if (!ok) {
            throw new RuntimeException("Clicking new patient button failed (reception main window).");
        }
        WindowType windowType = findCreatedWindow(receptionStub::findCreatedNewPatientWindow);
        return new ReceptionNewPatientWindow(receptionStub, windowType);
    }

    public ReceptionMgmtBlockingStub getReceptionStub() {
        return receptionStub;
    }

    public WqueueModel findInWqueue(int visitId) {
        return rpc(5, () ->
                receptionStub.listWqueueModel(null).getListList().stream()
                        .filter(m -> m.getVisitId() == visitId)
                        .findFirst()
        );
    }
}
