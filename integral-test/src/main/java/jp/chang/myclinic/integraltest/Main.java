package jp.chang.myclinic.integraltest;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtGrpc;
import jp.chang.myclinic.util.kanjidate.Gengou;
import jp.chang.myclinic.util.kanjidate.KanjiDate;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtGrpc.ReceptionMgmtBlockingStub;
import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtOuterClass.*;

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

    private void run(String[] args){
        if( args.length != 1 ){
            usage();
            System.exit(1);
        }
        try {
            {
                ch.qos.logback.classic.Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("io.grpc");
                log.setLevel(ch.qos.logback.classic.Level.ERROR);
            }
            String serverUrl = args[0];
            Service.setServerUrl(serverUrl);
            confirmMockPatient();
            this.receptionStub = newReceptionStub("localhost", 9000);
            testNewPatient();
        } finally {
            Service.stop();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void testNewPatient(){
        int prevLastPatientId = Service.api.listRecentlyRegisteredPatients(1).join().get(0).patientId;
        receptionStub.clickMainPaneNewPatientButton(null);
        WindowType win = findCreatedWindow(receptionStub::findCreatedNewPatientWindow);
        PatientInputs patientInputs = sampleData.pickPatientInputs();
        SetNewPatientWindowInputsRequest req = SetNewPatientWindowInputsRequest.newBuilder()
                .setWindow(win)
                .setInputs(patientInputs)
                .build();
        if(!receptionStub.setNewPatientWindowInputs(req).getValue()){
            throw new RuntimeException("setNewPatientWindowInputs failed.");
        }
        receptionStub.clickNewPatientWindowEnterButton(win);
        PatientDTO lastPatient = enteredPatient(prevLastPatientId);
        if (!isEqualPatient(lastPatient, patientInputs)) {
            System.out.println(patientInputs);
            System.out.println(lastPatient);
            throw new RuntimeException("Enter patient failed.");
        }
        WindowType patientWithHokenWindow = findCreatedWindow(receptionStub::findCreatedPatientWithHokenWindow);
        testNewPatientNewShahokokuho(patientWithHokenWindow);
        //receptionStub.clickEditPatientNewKoukikoureiButton(patientWithHokenWindow);
        //receptionStub.clickEditPatientNewKouhiButton(patientWithHokenWindow);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void testNewPatientNewShahokokuho(WindowType patientWithHokenWindow){
        receptionStub.clickEditPatientNewShahokokuhoButton(patientWithHokenWindow);
        WindowType enterWindow = findCreatedWindow(receptionStub::findCreatedNewShahokokuhoWindow);
        ShahokokuhoInputs inputs = sampleData.pickShahokokuhoInputs();
        SetNewShahokokuhoWindowInputsRequest req = SetNewShahokokuhoWindowInputsRequest.newBuilder()
                .setWindow(enterWindow)
                .setInputs(inputs)
                .build();
        boolean ok = receptionStub.setNewShahokokuhoWindowInputs(req).getValue();
        if( !ok ){
            throw new RuntimeException("set shahokokuho inputs failed");
        }
    }

    private WindowType findCreatedWindow(Function<VoidType, WindowType> f){
        return rpc(5, () -> {
            WindowType w = f.apply(null);
            return (w != null && w.getWindowId() > 0) ? Optional.of(w) : Optional.empty();
        });
    }

    private PatientDTO enteredPatient(int prevLastPatientId){
        return rpc(10, () -> {
            PatientDTO p = Service.api.listRecentlyRegisteredPatients(1).join().get(0);
            if( p.patientId > prevLastPatientId ){
                return Optional.of(p);
            } else {
                return Optional.empty();
            }
        });
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

    private boolean isEqualPatient(PatientDTO dto, PatientInputs inputs){
        LocalDate inputBirthday = KanjiDate.toLocalDate(
                Gengou.fromKanjiRep(inputs.getBirthdayGengou()),
                Integer.parseInt(inputs.getBirthdayNen()),
                Integer.parseInt(inputs.getBirthdayMonth()),
                Integer.parseInt(inputs.getBirthdayDay())
        );
        return dto.lastName.equals(inputs.getLastName())
                && dto.firstName.equals(inputs.getFirstName())
                && dto.lastNameYomi.equals(inputs.getLastNameYomi())
                && dto.firstNameYomi.equals(inputs.getFirstNameYomi())
                && dto.sex.equals(inputs.getSex())
                && dto.birthday.equals(inputBirthday.toString())
                && dto.address.equals(inputs.getAddress())
                && dto.phone.equals(inputs.getPhone());
    }

}
