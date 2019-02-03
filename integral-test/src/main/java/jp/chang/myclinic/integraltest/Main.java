package jp.chang.myclinic.integraltest;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtGrpc;
import org.slf4j.LoggerFactory;

import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtGrpc.*;

import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtOuterClass.*;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Main {

    //private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args){
        new Main().run(args);
    }

    private void usage(){
        System.out.println("Usage: integral-test SERVER-URL");
    }

    private SampleData sampleData = new SampleData();
    private ReceptionMgmtBlockingStub receptionStub;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void run(String[] args){
        if( args.length != 1 ){
            usage();
            System.exit(1);
        }
        String serverUrl = args[0];
        Service.setServerUrl(serverUrl);
        confirmMockPatient();
        this.receptionStub = newReceptionStub("localhost", 9000);
        receptionStub.clickMainPaneNewPatientButton(null);
        WindowType win = receptionStub.findCreatedNewPatientWindow(null);
        PatientInputs patientInputs = sampleData.pickPatientInputs();
        SetNewPatientWindowInputsRequest req = SetNewPatientWindowInputsRequest.newBuilder()
                .setWindow(win)
                .setInputs(patientInputs)
                .build();
        receptionStub.setNewPatientWindowInputs(req);
        Service.stop();
    }

    private void confirmMockPatient(){
        Service.api.getPatient(1)
                .thenAccept(patient -> {
                    if( !("試験".equals(patient.lastName) && "データ".equals(patient.firstName)) ){
                        System.err.println("Invalid mock patient.");
                        System.exit(3);
                    }
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    System.err.println("Cannot find mock patient.");
                    System.exit(2);
                    return null;
                });
    }

    private <T> T rpc(int maxTry, Supplier<Optional<T>> fun){
        try {
            while (maxTry-- > 0) {
                Optional<T> opt = fun.get();
                if (opt.isPresent()) {
                    return opt.get();
                }
                Thread.sleep(500);
            }
            throw new RuntimeException("rpc failed");
        } catch(Exception ex){
            ex.printStackTrace();
            throw new RuntimeException("rpc filed");
        }
    }

    private ReceptionMgmtBlockingStub newReceptionStub(String host, int port){
        Channel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        return ReceptionMgmtGrpc.newBlockingStub(channel);
    }

}
