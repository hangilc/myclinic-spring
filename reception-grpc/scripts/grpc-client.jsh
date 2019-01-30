/env --class-path reception-grpc/target/reception-grpc-1.0.0-SNAPSHOT-jar-with-dependencies.jar
import io.grpc.*;
import jp.chang.myclinic.reception.grpc.generated.*;
import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtOuterClass.*;
import jp.chang.myclinic.reception.grpc.SampleData;

var channel = ManagedChannelBuilder.forAddress("localhost", 18084).usePlaintext().build();
var stub = ReceptionMgmtGrpc.newBlockingStub(channel);

stub.clickMainPaneNewPatientButton(null);
var win = stub.findCreatedNewPatientWindow(null);
var req = SetNewPatientWindowInputsRequest.newBuilder().setWindow(win).setInputs(SampleData.patientInputs1).build();
stub.setNewPatientWindowInputs(req);
stub.clickNewPatientWindowEnterButton(win);
