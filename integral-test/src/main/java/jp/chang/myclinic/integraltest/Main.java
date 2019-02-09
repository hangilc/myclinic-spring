package jp.chang.myclinic.integraltest;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.KouhiDTO;
import jp.chang.myclinic.dto.KoukikoureiDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.integraltest.reception.ReceptionMainWindow;
import jp.chang.myclinic.integraltest.reception.ReceptionNewPatientWindow;
import jp.chang.myclinic.integraltest.reception.ReceptionNewShahokokuhoWindow;
import jp.chang.myclinic.integraltest.reception.ReceptionPatientWithHokenWindow;
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
    private ReceptionMgmtBlockingStub receptionStub;

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
            testNewPatientWithShahokokuhoAndKouhi(receptionStub);
        } finally {
            Service.stop();
        }
    }

    private void testNewPatientWithShahokokuhoAndKouhi(ReceptionMgmtBlockingStub receptionStub){
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
                ReceptionPatientWithHokenWindow.findCreated(receptionStub);
        ReceptionNewShahokokuhoWindow newShahokokuhoWindow =
                patientWithHokenWindow.clickNewShahokokuhoButton();
        ShahokokuhoInputs shahokokuhoInputs = sampleData.pickShahokokuhoInputs();
        newShahokokuhoWindow.setInputs(shahokokuhoInputs);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void testNewPatient() {
        int prevLastPatientId = Service.api.listRecentlyRegisteredPatients(1).join().get(0).patientId;
        receptionStub.clickMainPaneNewPatientButton(null);
        WindowType win = findCreatedWindow(receptionStub::findCreatedNewPatientWindow);
        PatientInputs patientInputs = sampleData.pickPatientInputs();
        SetNewPatientWindowInputsRequest req = SetNewPatientWindowInputsRequest.newBuilder()
                .setWindow(win)
                .setInputs(patientInputs)
                .build();
        if (!receptionStub.setNewPatientWindowInputs(req).getValue()) {
            throw new RuntimeException("setNewPatientWindowInputs failed.");
        }
        receptionStub.clickNewPatientWindowEnterButton(win);
        PatientDTO enteredPatient = enteredPatient(prevLastPatientId);
        if (!isEqualPatient(enteredPatient, patientInputs)) {
            System.out.println(patientInputs);
            System.out.println(enteredPatient);
            throw new RuntimeException("Enter patient failed.");
        }
        WindowType patientWithHokenWindow = findCreatedWindow(receptionStub::findCreatedPatientWithHokenWindow);
        testNewPatientNewShahokokuhoAndKouhi(patientWithHokenWindow, enteredPatient.patientId);
        //receptionStub.clickEditPatientNewKoukikoureiButton(patientWithHokenWindow);
        //receptionStub.clickEditPatientNewKouhiButton(patientWithHokenWindow);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void testNewPatientNewShahokokuhoAndKouhi(WindowType patientWithHokenWindow, int patientId) {
        int lastShahokokuhoId = getLastShahokokuhoId(patientId);
        receptionStub.clickEditPatientNewShahokokuhoButton(patientWithHokenWindow);
        WindowType enterWindow = findCreatedWindow(receptionStub::findCreatedNewShahokokuhoWindow);
        ShahokokuhoInputs inputs = sampleData.pickShahokokuhoInputs();
        SetNewShahokokuhoWindowInputsRequest req = SetNewShahokokuhoWindowInputsRequest.newBuilder()
                .setWindow(enterWindow)
                .setInputs(inputs)
                .build();
        boolean ok = receptionStub.setNewShahokokuhoWindowInputs(req).getValue();
        if (!ok) {
            throw new RuntimeException("set shahokokuho inputs failed");
        }
        ok = receptionStub.clickNewShahokokuhoWindowEnterButton(enterWindow).getValue();
        if (!ok) {
            throw new RuntimeException("clickNewShahokokuhoWindowEnterButton failed.");
        }
        ShahokokuhoDTO createdShahokokuho = getCreatedShahokokuho(patientId, lastShahokokuhoId);
        if( !(createdShahokokuho.patientId == patientId &&
                isEqualShahokokuho(createdShahokokuho, inputs)) ){
            throw new RuntimeException("Created shahokokuho does not match inputs.");
        }
        int lastKouhiId = getLastKouhiId(patientId);
        receptionStub.clickEditPatientNewKouhiButton(patientWithHokenWindow);
        WindowType enterKouhiWin = findCreatedWindow(receptionStub::findCreatedNewKouhiWindow);
        KouhiInputs kouhiInputs = sampleData.pickKouhiInputs();
        SetNewKouhiWindowInputsRequest kouhiReq = SetNewKouhiWindowInputsRequest.newBuilder()
                .setWindow(enterKouhiWin)
                .setInputs(kouhiInputs)
                .build();
        ok = receptionStub.setNewKouhiWindowInputs(kouhiReq).getValue();
        if( !ok ){
            throw new RuntimeException("set kouhi inputs failed");
        }
        ok = receptionStub.clickNewKouhiWindowEnterButton(enterKouhiWin).getValue();
        if( !ok ){
            throw new RuntimeException("Click enter failed in new kouhi window.");
        }
        KouhiDTO createdKouhi = getCreatedKouhi(patientId, lastKouhiId);
        if( !(createdKouhi.patientId == patientId &&
                isEqualKouhi(createdKouhi, kouhiInputs)) ){
            throw new RuntimeException("Created kouhi does not match inputs.");
        }
        ok = receptionStub.clickEditPatientCloseButton(patientWithHokenWindow).getValue();
        if( !ok ){
            throw new RuntimeException("Clicking close button in edit patient window failed.");
        }
    }

    private int getLastShahokokuhoId(int patientId){
        List<ShahokokuhoDTO> hokenList = Service.api.listHoken(patientId).join().shahokokuhoListDTO;
        return hokenList.stream().map(h -> h.shahokokuhoId).max(Comparator.naturalOrder())
                .orElse(0);
    }

    private int getLastKoukikoureiId(int patientId){
        List<KoukikoureiDTO> hokenList = Service.api.listHoken(patientId).join().koukikoureiListDTO;
        return hokenList.stream().map(h -> h.koukikoureiId).max(Comparator.naturalOrder())
                .orElse(0);
    }

    private int getLastKouhiId(int patientId){
        List<KouhiDTO> hokenList = Service.api.listHoken(patientId).join().kouhiListDTO;
        return hokenList.stream().map(h -> h.kouhiId).max(Comparator.naturalOrder())
                .orElse(0);
    }


    private ShahokokuhoDTO getCreatedShahokokuho(int patientId, int lastShahokokuhoId){
        return rpc(5, () -> {
            List<ShahokokuhoDTO> list = Service.api.listHoken(patientId).join().shahokokuhoListDTO
                    .stream().filter(dto -> dto.shahokokuhoId > lastShahokokuhoId)
                    .collect(toList());
            if( list.size() > 1 ){
                throw new RuntimeException("Too many shahokokuho created.");
            } else if( list.size() == 1 ){
                return Optional.of(list.get(0));
            } else {
                return Optional.empty();
            }
        });
    }

    private KouhiDTO getCreatedKouhi(int patientId, int lastKouhiId){
        return rpc(5, () -> {
            List<KouhiDTO> list = Service.api.listHoken(patientId).join().kouhiListDTO
                    .stream().filter(dto -> dto.kouhiId > lastKouhiId)
                    .collect(toList());
            if( list.size() > 1 ){
                throw new RuntimeException("Too many kouhi created.");
            } else if( list.size() == 1 ){
                return Optional.of(list.get(0));
            } else {
                return Optional.empty();
            }
        });
    }

    private WindowType findCreatedWindow(Function<VoidType, WindowType> f) {
        return rpc(5, () -> {
            WindowType w = f.apply(null);
            return (w != null && w.getWindowId() > 0) ? Optional.of(w) : Optional.empty();
        });
    }

    private PatientDTO enteredPatient(int prevLastPatientId) {
        return rpc(10, () -> {
            PatientDTO p = Service.api.listRecentlyRegisteredPatients(1).join().get(0);
            if (p.patientId > prevLastPatientId) {
                return Optional.of(p);
            } else {
                return Optional.empty();
            }
        });
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

    private <T> T rpc(int maxTry, Supplier<Optional<T>> fun) {
        try {
            while (maxTry-- > 0) {
                Optional<T> opt = fun.get();
                if (opt.isPresent()) {
                    return opt.get();
                }
                Thread.sleep(500);
            }
            throw new RuntimeException("rpc failed");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("rpc filed");
        }
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

    private boolean isEqualKouhi(KouhiDTO dto, KouhiInputs inputs){
        return new CompositeEquals()
                .and(dto.futansha, Integer.parseInt(inputs.getFutanshaBangou()))
                .and(dto.jukyuusha, Integer.parseInt(inputs.getJukyuushaBangou()))
                .and(dto.validFrom, toSqldate(inputs.getValidFromInputs()))
                .and(dto.validUpto, toSqldate(inputs.getValidUptoInputs()))
                .isEqual();
    }

}
