package jp.chang.myclinic.reception.grpc;

import io.grpc.stub.StreamObserver;
import javafx.application.Platform;
import javafx.stage.Window;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.reception.Globals;
import jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtGrpc;
import jp.chang.myclinic.reception.javafx.PatientWithHokenStage;
import jp.chang.myclinic.reception.javafx.edit_kouhi.EnterKouhiStage;
import jp.chang.myclinic.reception.javafx.edit_koukikourei.EnterKoukikoureiStage;
import jp.chang.myclinic.reception.javafx.edit_patient.EnterPatientStage;
import jp.chang.myclinic.reception.javafx.edit_patient.PatientFormInputs;
import jp.chang.myclinic.reception.javafx.edit_shahokokuho.EnterShahokokuhoStage;
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

    private void findCreatedWindow(Class<? extends Window> windowClass, StreamObserver<WindowType> responseObserver){
        Window win = Globals.getInstance().findNewWindow(windowClass);
        if( win != null ){
            System.out.println("Foudn created window. " + windowClass.toString());
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
}
