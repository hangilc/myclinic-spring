package jp.chang.myclinic.reception.grpc;

import io.grpc.stub.StreamObserver;
import javafx.application.Platform;
import javafx.stage.Window;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.reception.Globals;
import jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtGrpc;
import jp.chang.myclinic.reception.javafx.edit_patient.EnterPatientStage;
import jp.chang.myclinic.reception.javafx.edit_patient.PatientFormInputs;
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
    public void findCreatedNewPatientWindow(VoidType request, StreamObserver<WindowType> responseObserver) {
        WindowType reply;
        Window win = Globals.getInstance().findNewWindow(EnterPatientStage.class);
        if( win != null ){
            reply = WindowType.newBuilder().setWindowId((Integer)win.getUserData()).build();
        } else {
            reply = null;
        }
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
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
}
