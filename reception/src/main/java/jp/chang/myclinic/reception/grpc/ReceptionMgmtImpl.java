package jp.chang.myclinic.reception.grpc;

import io.grpc.stub.StreamObserver;
import javafx.application.Platform;
import javafx.stage.Window;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.reception.Globals;
import jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtGrpc;
import jp.chang.myclinic.reception.javafx.PatientWithHokenStage;
import jp.chang.myclinic.reception.javafx.edit_kouhi.EnterKouhiStage;
import jp.chang.myclinic.reception.javafx.edit_kouhi.KouhiFormInputs;
import jp.chang.myclinic.reception.javafx.edit_koukikourei.EnterKoukikoureiStage;
import jp.chang.myclinic.reception.javafx.edit_koukikourei.KoukikoureiFormInputs;
import jp.chang.myclinic.reception.javafx.edit_patient.EnterPatientStage;
import jp.chang.myclinic.reception.javafx.edit_patient.PatientFormInputs;
import jp.chang.myclinic.reception.javafx.edit_shahokokuho.EnterShahokokuhoStage;
import jp.chang.myclinic.reception.javafx.edit_shahokokuho.ShahokokuhoFormInputs;
import jp.chang.myclinic.util.kanjidate.Gengou;
import jp.chang.myclinic.utilfx.dateinput.DateFormInputs;

import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtOuterClass.*;

public class ReceptionMgmtImpl extends ReceptionMgmtGrpc.ReceptionMgmtImplBase {

    //private static Logger logger = LoggerFactory.getLogger(ReceptionMgmtImpl.class);

    @Override
    public void isRunning(VoidType request, StreamObserver<BooleanType> responseObserver) {
        BooleanType reply = BooleanType.newBuilder().setValue(true).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void clickMainPaneNewPatientButton(VoidType request, StreamObserver<BooleanType> responseObserver) {
        Platform.runLater(() -> Globals.getMainPane().simulateNewPatientButtonClick());
        BooleanType reply = BooleanType.newBuilder().setValue(true).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void clickNewPatientWindowEnterButton(WindowType request, StreamObserver<BooleanType> responseObserver) {
        Window win = Globals.getInstance().findWindow(request.getWindowId());
        boolean result = false;
        if( win instanceof EnterPatientStage ){
            EnterPatientStage stage = (EnterPatientStage)win;
            Platform.runLater(stage::simulateEnterButtonClick);
            result = true;
        }
        responseObserver.onNext(BooleanType.newBuilder().setValue(result).build());
        responseObserver.onCompleted();
    }

    @Override
    public void clickEditPatientNewShahokokuhoButton(WindowType request, StreamObserver<BooleanType> responseObserver) {
        Window win = Globals.getInstance().findWindow(request.getWindowId());
        boolean result = false;
        if( win instanceof PatientWithHokenStage){
            PatientWithHokenStage stage = (PatientWithHokenStage)win;
            Platform.runLater(stage::simulateNewShahokokuhoButtonClick);
            result = true;
        }
        responseObserver.onNext(BooleanType.newBuilder().setValue(result).build());
        responseObserver.onCompleted();
    }

    @Override
    public void clickEditPatientNewKoukikoureiButton(WindowType request, StreamObserver<BooleanType> responseObserver) {
        Window win = Globals.getInstance().findWindow(request.getWindowId());
        boolean result = false;
        if( win instanceof PatientWithHokenStage){
            PatientWithHokenStage stage = (PatientWithHokenStage)win;
            Platform.runLater(stage::simulateNewKoukikoureiButtonClick);
            result = true;
        }
        responseObserver.onNext(BooleanType.newBuilder().setValue(result).build());
        responseObserver.onCompleted();
    }

    @Override
    public void clickEditPatientNewKouhiButton(WindowType request, StreamObserver<BooleanType> responseObserver) {
        Window win = Globals.getInstance().findWindow(request.getWindowId());
        boolean result = false;
        if( win instanceof PatientWithHokenStage){
            PatientWithHokenStage stage = (PatientWithHokenStage)win;
            Platform.runLater(stage::simulateNewKouhiButtonClick);
            result = true;
        }
        responseObserver.onNext(BooleanType.newBuilder().setValue(result).build());
        responseObserver.onCompleted();
    }

    @Override
    public void clickEditPatientCloseButton(WindowType request, StreamObserver<BooleanType> responseObserver) {
        Window win = Globals.getInstance().findWindow(request.getWindowId());
        boolean result = false;
        if( win instanceof PatientWithHokenStage){
            PatientWithHokenStage stage = (PatientWithHokenStage)win;
            Platform.runLater(stage::simulateCloseButtonClick);
            result = true;
        }
        responseObserver.onNext(BooleanType.newBuilder().setValue(result).build());
        responseObserver.onCompleted();
    }

    @Override
    public void clickNewShahokokuhoWindowEnterButton(WindowType request, StreamObserver<BooleanType> responseObserver) {
        Window win = Globals.getInstance().findWindow(request.getWindowId());
        boolean ok = false;
        if( win instanceof EnterShahokokuhoStage ){
            EnterShahokokuhoStage stage = (EnterShahokokuhoStage)win;
            Platform.runLater(stage::simulateEnterButtonClick);
            ok = true;
        } else {
            System.err.println("Cannot get EnterShahokokuhoStage.");
        }
        responseObserver.onNext(BooleanType.newBuilder().setValue(ok).build());
        responseObserver.onCompleted();
    }

    @Override
    public void clickNewKoukikoureiWindowEnterButton(WindowType request, StreamObserver<BooleanType> responseObserver) {
        Window win = Globals.getInstance().findWindow(request.getWindowId());
        boolean ok = false;
        if( win instanceof EnterKoukikoureiStage ){
            EnterKoukikoureiStage stage = (EnterKoukikoureiStage)win;
            Platform.runLater(stage::simulateEnterButtonClick);
            ok = true;
        } else {
            System.err.println("Cannot get EnterShahokokuhoStage.");
        }
        responseObserver.onNext(BooleanType.newBuilder().setValue(ok).build());
        responseObserver.onCompleted();
    }

    @Override
    public void clickNewKouhiWindowEnterButton(WindowType request, StreamObserver<BooleanType> responseObserver) {
        Window win = Globals.getInstance().findWindow(request.getWindowId());
        boolean ok = false;
        if( win instanceof EnterKouhiStage ){
            EnterKouhiStage stage = (EnterKouhiStage)win;
            Platform.runLater(stage::simulateEnterButtonClick);
            ok = true;
        } else {
            System.err.println("Cannot get EnterShahokokuhoStage.");
        }
        responseObserver.onNext(BooleanType.newBuilder().setValue(ok).build());
        responseObserver.onCompleted();
    }

    private void findCreatedWindow(Class<? extends Window> windowClass, StreamObserver<WindowType> responseObserver){
        Window win = Globals.getInstance().findNewWindow(windowClass);
        if( win != null ){
            System.out.println("Found created window. " + windowClass.toString());
            WindowType winType = WindowType.newBuilder().setWindowId((Integer)win.getUserData()).build();
            responseObserver.onNext(winType);
        } else {
            System.out.println("Failed to find created window. " + windowClass.toString());
            responseObserver.onNext(null);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void findCreatedNewPatientWindow(VoidType request, StreamObserver<WindowType> responseObserver) {
        findCreatedWindow(EnterPatientStage.class, responseObserver);
    }

    @Override
    public void findCreatedPatientWithHokenWindow(VoidType request, StreamObserver<WindowType> responseObserver) {
        findCreatedWindow(PatientWithHokenStage.class, responseObserver);
    }

    @Override
    public void findCreatedNewShahokokuhoWindow(VoidType request, StreamObserver<WindowType> responseObserver) {
        findCreatedWindow(EnterShahokokuhoStage.class, responseObserver);
    }

    @Override
    public void findCreatedNewKoukikoureiWindow(VoidType request, StreamObserver<WindowType> responseObserver) {
        findCreatedWindow(EnterKoukikoureiStage.class, responseObserver);
    }

    @Override
    public void findCreatedNewKouhiWindow(VoidType request, StreamObserver<WindowType> responseObserver) {
        findCreatedWindow(EnterKouhiStage.class, responseObserver);
    }

    @Override
    public void setNewPatientWindowInputs(SetNewPatientWindowInputsRequest request, StreamObserver<BooleanType> responseObserver) {
        Window win = Globals.getInstance().findWindow(request.getWindow().getWindowId());
        if( win instanceof EnterPatientStage){
            EnterPatientStage stage = (EnterPatientStage)win;
            PatientFormInputs inputs = new PatientFormInputs();
            inputs.lastNameInput = request.getInputs().getLastName();
            inputs.firstNameInput = request.getInputs().getFirstName();
            inputs.lastNameYomiInput = request.getInputs().getLastNameYomi();
            inputs.firstNameYomiInput = request.getInputs().getFirstNameYomi();
            Gengou birthdayGengou = Gengou.fromKanjiRep(request.getInputs().getBirthdayGengou());
            if( birthdayGengou == null ){
                System.err.println("Invalid birthday gengou: " + request.getInputs().getBirthdayGengou());
            }
            inputs.birthdayInputs = new DateFormInputs(
                    birthdayGengou,
                    request.getInputs().getBirthdayNen(),
                    request.getInputs().getBirthdayMonth(),
                    request.getInputs().getBirthdayDay()
            );
            Sex sex = Sex.fromCode(request.getInputs().getSex());
            if( sex == null ){
                System.err.println("Invalid sex: " + request.getInputs().getSex());
            }
            inputs.sexInput = sex;
            inputs.addressInput = request.getInputs().getAddress();
            inputs.phoneInput = request.getInputs().getPhone();
            Platform.runLater(() -> stage.setInputs(inputs));
            responseObserver.onNext(BooleanType.newBuilder().setValue(true).build());
        } else {
            System.err.println("Cannot find window (setNewPatientWindowInputs.)");
            responseObserver.onNext(BooleanType.newBuilder().setValue(false).build());
        }
        responseObserver.onCompleted();
    }

    private DateFormInputs toDateFormInputs(DateInputs src){
        DateFormInputs dst = new DateFormInputs();
        dst.gengou = Gengou.fromKanjiRep(src.getGengou());
        dst.nen = src.getNen();
        dst.month = src.getMonth();
        dst.day = src.getDay();
        return dst;
    }

    private ShahokokuhoFormInputs convertToShahokokuhoFormInputs(ShahokokuhoInputs src){
        ShahokokuhoFormInputs dst = new ShahokokuhoFormInputs();
        dst.hokenshaBangou = src.getHokenshaBangou();
        dst.hihokenshaKigou = src.getHihokenshaKigou();
        dst.hihokenshaBangou = src.getHihokenshaBangou();
        dst.honnin = src.getHonnin();
        dst.validFromInputs = toDateFormInputs(src.getValidFromInputs());
        dst.validUptoInputs = toDateFormInputs(src.getValidUptoInputs());
        dst.kourei = src.getKourei();
        return dst;
    }

    @Override
    public void setNewShahokokuhoWindowInputs(SetNewShahokokuhoWindowInputsRequest request, StreamObserver<BooleanType> responseObserver) {
        Window win = Globals.getInstance().findWindow(request.getWindow().getWindowId());
        boolean ok = false;
        if( win instanceof EnterShahokokuhoStage ){
            EnterShahokokuhoStage stage = (EnterShahokokuhoStage)win;
            ShahokokuhoFormInputs inputs = convertToShahokokuhoFormInputs(request.getInputs());
            Platform.runLater(() -> stage.setInputs(inputs));
            ok = true;
        } else {
            System.err.println("Cannot find window (setNewShahokokuhoWindowInputs.)");
        }
        responseObserver.onNext(BooleanType.newBuilder().setValue(ok).build());
        responseObserver.onCompleted();
    }

    private KoukikoureiFormInputs toKoukikoureiFormInputs(KoukikoureiInputs src){
        KoukikoureiFormInputs inputs = new KoukikoureiFormInputs();
        inputs.hokenshaBangou = src.getHokenshaBangou();
        inputs.hihokenshaBangou = src.getHihokenshaBangou();
        inputs.validFromInputs = toDateFormInputs(src.getValidFromInputs());
        inputs.validUptoInputs = toDateFormInputs(src.getValidUptoInputs());
        return inputs;
    }

    @Override
    public void setNewKoukikoureiWindowInputs(SetNewKoukikoureiWindowInputsRequest request, StreamObserver<BooleanType> responseObserver) {
        Window win = Globals.getInstance().findWindow(request.getWindow().getWindowId());
        boolean ok = false;
        if( win instanceof EnterKoukikoureiStage ){
            EnterKoukikoureiStage stage = (EnterKoukikoureiStage)win;
            KoukikoureiFormInputs inputs = toKoukikoureiFormInputs(request.getInputs());
            Platform.runLater(() -> stage.setInputs(inputs));
            ok = true;
        } else {
            System.err.println("Cannot find window (setNewKoukikoureiWindowInputs.)");
        }
        responseObserver.onNext(BooleanType.newBuilder().setValue(ok).build());
        responseObserver.onCompleted();
    }

    private KouhiFormInputs toKouhiFormInputs(KouhiInputs src){
        KouhiFormInputs inputs = new KouhiFormInputs();
        inputs.futanshaBangou = src.getFutanshaBangou();
        inputs.jukyuushaBangou = src.getJukyuushaBangou();
        inputs.validFromInputs = toDateFormInputs(src.getValidFromInputs());
        inputs.validUptoInputs = toDateFormInputs(src.getValidUptoInputs());
        return inputs;
    }

    @Override
    public void setNewKouhiWindowInputs(SetNewKouhiWindowInputsRequest request, StreamObserver<BooleanType> responseObserver) {
        Window win = Globals.getInstance().findWindow(request.getWindow().getWindowId());
        boolean ok = false;
        if( win instanceof EnterKouhiStage ){
            EnterKouhiStage stage = (EnterKouhiStage)win;
            KouhiFormInputs inputs = toKouhiFormInputs(request.getInputs());
            Platform.runLater(() -> stage.setInputs(inputs));
            ok = true;
        } else {
            System.err.println("Cannot find window (setNewKouhiWindowInputs.)");
        }
        responseObserver.onNext(BooleanType.newBuilder().setValue(ok).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getPatientWithHokenWindowPatientId(WindowType request, StreamObserver<PatientWithHokenWindowInfo> responseObserver) {
        Window win = Globals.getInstance().findWindow(request.getWindowId());
        PatientWithHokenWindowInfo info = null;
        if( win instanceof PatientWithHokenStage ){
            PatientWithHokenStage stage = (PatientWithHokenStage)win;
            info = PatientWithHokenWindowInfo.newBuilder()
                    .setPatientId(stage.getPatientId())
                    .build();
        } else {
            System.err.println("Cannot find window (getPatientWithHokenWindowPatientId.)");
        }
        responseObserver.onNext(info);
        responseObserver.onCompleted();
    }

}
