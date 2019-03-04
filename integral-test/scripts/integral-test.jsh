/env --class-path reception-grpc/target/reception-grpc-1.0.0-SNAPSHOT-jar-with-dependencies.jar;integral-test\target\integral-test-1.0.0-SNAPSHOT-jar-with-dependencies.jar
import io.grpc.*;
import jp.chang.myclinic.reception.grpc.generated.*;
import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtOuterClass.*;
import jp.chang.myclinic.integraltest.*;

var channel = ManagedChannelBuilder.forAddress("localhost", 18084).usePlaintext().build();
var stub = ReceptionMgmtGrpc.newBlockingStub(channel);
var sample = new SampleData();

void start() throws Exception {
    stub.clickMainPaneNewPatientButton(null);
    Thread.sleep(1);
    var win = stub.findCreatedNewPatientWindow(null);
    var patientInputs = sample.pickPatientInputs();
    var execEnv = SetNewPatientWindowInputsRequest.newBuilder().setWindow(win).setInputs(patientInputs).build();
    stub.setNewPatientWindowInputs(execEnv);
    stub.clickNewPatientWindowEnterButton(win);
}
