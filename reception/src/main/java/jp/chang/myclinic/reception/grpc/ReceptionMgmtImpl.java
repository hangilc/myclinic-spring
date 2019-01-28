package jp.chang.myclinic.reception.grpc;

import io.grpc.stub.StreamObserver;
import jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtGrpc;
import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtOuterClass.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReceptionMgmtImpl extends ReceptionMgmtGrpc.ReceptionMgmtImplBase {

    private static Logger logger = LoggerFactory.getLogger(ReceptionMgmtImpl.class);

    @Override
    public void isRunning(IsRunningRequest request, StreamObserver<IsRunningReply> responseObserver) {
        IsRunningReply reply = IsRunningReply.newBuilder().setOk(true).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
