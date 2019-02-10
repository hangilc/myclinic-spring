package jp.chang.myclinic.practice.grpc;

import io.grpc.stub.StreamObserver;
import static jp.chang.myclinic.practice.grpc.generated.PracticeMgmtOuterClass.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jp.chang.myclinic.practice.grpc.generated.PracticeMgmtGrpc;

public class PracticeMgmtImpl extends PracticeMgmtGrpc.PracticeMgmtImplBase{

    //private static Logger logger = LoggerFactory.getLogger(PracticeMtmtImpl.class);

    PracticeMgmtImpl() {

    }

    @Override
    public void isRunning(VoidType request, StreamObserver<BooleanType> responseObserver) {
        responseObserver.onNext(BooleanType.newBuilder().setValue(true).build());
        responseObserver.onCompleted();
    }
}
