package jp.chang.myclinic.integraltest;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.KouhiDTO;
import jp.chang.myclinic.dto.KoukikoureiDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.integraltest.reception.*;
import jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtGrpc;
import jp.chang.myclinic.util.kanjidate.Gengou;
import jp.chang.myclinic.util.kanjidate.KanjiDate;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;
import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtGrpc.ReceptionMgmtBlockingStub;
import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtOuterClass.*;

public class Main {

    //private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        new Main().run(args);
    }

    private void usage() {
        System.out.println("Usage: integral-test SERVER-URL");
    }

    private SampleData sampleData = new SampleData();
    private ReceptionMainWindow receptionMainWindow;

    private void run(String[] args) {
        if (args.length != 1) {
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
            ReceptionMgmtBlockingStub receptionStub = newReceptionStub("localhost", 9000);
            receptionMainWindow = new ReceptionMainWindow(receptionStub);
            //testNewPatientWithShahokokuhoAndKouhi();
            testNewPatientWithKoukikourei();
        } finally {
            Service.stop();
        }
    }

    private void testNewPatientWithShahokokuhoAndKouhi(){
        ReceptionNewPatientWindow newPatientWindow = receptionMainWindow.clickNewPatientButton();
        PatientInputs patientInputs = sampleData.pickPatientInputs();
        newPatientWindow.setInputs(patientInputs);
        PatientDTO enteredPatient = newPatientWindow.clickEnterButton();
        if( !isEqualPatient(enteredPatient, patientInputs) ){
            System.out.println(patientInputs);
            System.out.println(enteredPatient);
            throw new RuntimeException("Enter patient failed.");
        }
        ReceptionPatientWithHokenWindow patientWithHokenWindow =
                ReceptionPatientWithHokenWindow.findCreated(receptionMainWindow.getReceptionStub());
        ReceptionNewShahokokuhoWindow newShahokokuhoWindow =
                patientWithHokenWindow.clickNewShahokokuhoButton();
        ShahokokuhoInputs shahokokuhoInputs = sampleData.pickShahokokuhoInputs();
        newShahokokuhoWindow.setInputs(shahokokuhoInputs);
        ShahokokuhoDTO enteredShahokokuho = newShahokokuhoWindow.clickEnterButton();
        if( !(enteredShahokokuho.patientId == enteredPatient.patientId &&
                isEqualShahokokuho(enteredShahokokuho, shahokokuhoInputs)) ){
            throw new RuntimeException("Created shahokokuho does not match inputs.");
        }
        ReceptionNewKouhiWindow newKouhiWindow = patientWithHokenWindow.clickNewKouhiButton();
        KouhiInputs kouhiInputs = sampleData.pickKouhiInputs();
        newKouhiWindow.setInputs(kouhiInputs);
        KouhiDTO enteredKouhi = newKouhiWindow.clickEnterButton();
        if( !(enteredKouhi.patientId == enteredPatient.patientId &&
                isEqualKouhi(enteredKouhi, kouhiInputs)) ){
            throw new RuntimeException("Created kouhi does not match inputs.");
        }
        patientWithHokenWindow.clickCloseButton();
    }

    private void testNewPatientWithKoukikourei(){
        ReceptionNewPatientWindow newPatientWindow = receptionMainWindow.clickNewPatientButton();
        PatientInputs patientInputs = sampleData.pickPatientInputs();
        newPatientWindow.setInputs(patientInputs);
        PatientDTO enteredPatient = newPatientWindow.clickEnterButton();
        if( !isEqualPatient(enteredPatient, patientInputs) ){
            System.out.println(patientInputs);
            System.out.println(enteredPatient);
            throw new RuntimeException("Enter patient failed.");
        }
        ReceptionPatientWithHokenWindow patientWithHokenWindow =
                ReceptionPatientWithHokenWindow.findCreated(receptionMainWindow.getReceptionStub());
        ReceptionNewKoukikoureiWindow newKoukikoureiWindow =
                patientWithHokenWindow.clickNewKoukikoureiButton();
        KoukikoureiInputs koukikoureiInputs = sampleData.pickKoukikoureiInputs();
        newKoukikoureiWindow.setInputs(koukikoureiInputs);
        KoukikoureiDTO enteredKoukikourei = newKoukikoureiWindow.clickEnterButton();
        if( !(enteredKoukikourei.patientId == enteredPatient.patientId &&
                isEqualKoukikourei(enteredKoukikourei, koukikoureiInputs)) ){
            throw new RuntimeException("Created koukikourei does not match inputs.");
        }
        patientWithHokenWindow.clickCloseButton();
    }


    private int getLastKoukikoureiId(int patientId){
        List<KoukikoureiDTO> hokenList = Service.api.listHoken(patientId).join().koukikoureiListDTO;
        return hokenList.stream().map(h -> h.koukikoureiId).max(Comparator.naturalOrder())
                .orElse(0);
    }

    private void confirmMockPatient() {
        Service.api.getPatient(1)
                .thenAccept(patient -> {
                    if (!("試験".equals(patient.lastName) && "データ".equals(patient.firstName))) {
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



    private ReceptionMgmtBlockingStub newReceptionStub(String host, int port) {
        Channel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        return ReceptionMgmtGrpc.newBlockingStub(channel);
    }

    private String toSqldate(DateInputs inputs) {
        if (inputs.getNen().isEmpty() && inputs.getMonth().isEmpty() && inputs.getDay().isEmpty()) {
            return "0000-00-00";
        }
        return KanjiDate.toLocalDate(
                Gengou.fromKanjiRep(inputs.getGengou()),
                Integer.parseInt(inputs.getNen()),
                Integer.parseInt(inputs.getMonth()),
                Integer.parseInt(inputs.getDay())
        ).toString();
    }

    private boolean isEqualPatient(PatientDTO dto, PatientInputs inputs) {
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

    private boolean isEqualShahokokuho(ShahokokuhoDTO dto, ShahokokuhoInputs inputs) {
        return new CompositeEquals()
                .and(dto.hokenshaBangou, Integer.parseInt(inputs.getHokenshaBangou()))
                .and(dto.hihokenshaKigou, inputs.getHihokenshaKigou())
                .and(dto.hihokenshaBangou, inputs.getHihokenshaBangou())
                .and(dto.honnin, inputs.getHonnin())
                .and(dto.kourei, inputs.getKourei())
                .and(dto.validFrom, toSqldate(inputs.getValidFromInputs()))
                .and(dto.validUpto, toSqldate(inputs.getValidUptoInputs()))
                .isEqual();
    }

    private boolean isEqualKoukikourei(KoukikoureiDTO dto, KoukikoureiInputs inputs){
        return new CompositeEquals()
                .and(dto.hokenshaBangou, inputs.getHokenshaBangou())
                .and(dto.hihokenshaBangou, inputs.getHihokenshaBangou())
                .and(dto.validFrom, toSqldate(inputs.getValidFromInputs()))
                .and(dto.validUpto, toSqldate(inputs.getValidUptoInputs()))
                .and(dto.futanWari, inputs.getFutanwari())
                .isEqual();
    }

    private boolean isEqualKouhi(KouhiDTO dto, KouhiInputs inputs){
        return new CompositeEquals()
                .and(dto.futansha, Integer.parseInt(inputs.getFutanshaBangou()))
                .and(dto.jukyuusha, Integer.parseInt(inputs.getJukyuushaBangou()))
                .and(dto.validFrom, toSqldate(inputs.getValidFromInputs()))
                .and(dto.validUpto, toSqldate(inputs.getValidUptoInputs()))
                .isEqual();
    }

}
