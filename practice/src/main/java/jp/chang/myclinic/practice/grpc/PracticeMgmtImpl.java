package jp.chang.myclinic.practice.grpc;

import com.google.protobuf.BoolValue;
import com.google.protobuf.Empty;
import com.google.protobuf.Int32Value;
import io.grpc.stub.StreamObserver;

import static java.util.stream.Collectors.toList;
import static jp.chang.myclinic.practice.grpc.generated.PracticeMgmtOuterClass.*;

import javafx.application.Platform;
import javafx.stage.Window;
import jp.chang.myclinic.Common;
import jp.chang.myclinic.Common.*;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.WqueueFullDTO;
import jp.chang.myclinic.practice.Globals;
import jp.chang.myclinic.practice.PracticeEnv;
import jp.chang.myclinic.practice.javafx.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jp.chang.myclinic.practice.grpc.generated.PracticeMgmtGrpc;

import java.util.List;

public class PracticeMgmtImpl extends PracticeMgmtGrpc.PracticeMgmtImplBase {

    //private static Logger logger = LoggerFactory.getLogger(PracticeMtmtImpl.class);

    PracticeMgmtImpl() {

    }

    @Override
    public void isRunning(Empty request, StreamObserver<BoolValue> responseObserver) {
        responseObserver.onNext(BoolValue.of(true));
        responseObserver.onCompleted();
    }

    @Override
    public void chooseSelectVisitMenuInMainPane(Empty request, StreamObserver<BoolValue> responseObserver) {
        MainPane mainPane = Globals.getInstance().getMainPane();
        Platform.runLater(mainPane::simulateSelectVisitMenuChoice);
        responseObserver.onNext(BoolValue.of(true));
        responseObserver.onCompleted();
    }

    @Override
    public void findSelectVisitWindow(Empty request, StreamObserver<WindowType> responseObserver) {
        findCreatedWindow(SelectFromWqueueDialog.class, responseObserver);
    }

    private <T extends Window> void findCreatedWindow(Class<T> windowClass,
                                                      StreamObserver<WindowType> responseObserver) {
        T w = Globals.getInstance().findNewWindow(windowClass);
        if (w != null) {
            Common.WindowType reply = Common.WindowType.newBuilder()
                    .setWindowId((Integer) w.getUserData())
                    .build();
            responseObserver.onNext(reply);
        } else {
            responseObserver.onNext(null);
        }
        responseObserver.onCompleted();
    }

    private Common.WqueueType toWqueueType(WqueueFullDTO dto) {
        return WqueueType.newBuilder()
                .setVisitId(dto.visit.visitId)
                .setWaitState(dto.wqueue.waitState)
                .build();
    }

    @Override
    public void listWqueue(WindowType request, StreamObserver<Common.WqueueListType> responseObserver) {
        SelectFromWqueueDialog dialog = Globals.getInstance().findWindowById(request.getWindowId(),
                SelectFromWqueueDialog.class);
        List<WqueueType> wqueueList = dialog.getList().stream()
                .map(this::toWqueueType)
                .collect(toList());
        WqueueListType reply = WqueueListType.newBuilder()
                .addAllWqueue(wqueueList)
                .build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void chooseVisitInSelectVisitWindow(ChooseVisitInSelectWindowMessage request, StreamObserver<BoolValue> responseObserver) {
        WindowType windowType = request.getWindow();
        SelectFromWqueueDialog dialog = Globals.getInstance().findWindowById(windowType.getWindowId(),
                SelectFromWqueueDialog.class);
        int visitId = request.getVisitId();
        Platform.runLater(() -> {
            boolean ok = dialog.simulateSelectVisit(visitId);
            responseObserver.onNext(BoolValue.of(ok));
            responseObserver.onCompleted();
        });
    }

    @Override
    public void getCurrentPatientId(Empty request, StreamObserver<Int32Value> responseObserver) {
        PatientDTO patient = PracticeEnv.INSTANCE.getCurrentPatient();
        int patientId = patient == null ? 0 : patient.patientId;
        responseObserver.onNext(Int32Value.of(patientId));
        responseObserver.onCompleted();
    }

    @Override
    public void getCurrentVisitId(Empty request, StreamObserver<Int32Value> responseObserver) {
        int currentVisitId = PracticeEnv.INSTANCE.getCurrentVisitId();
        responseObserver.onNext(Int32Value.of(currentVisitId));
        responseObserver.onCompleted();
    }

    @Override
    public void isRecordVisible(VisitType request, StreamObserver<BoolValue> responseObserver) {
        MainPane mainPane = Globals.getInstance().getMainPane();
        Record record = mainPane.findRecord(request.getVisitId());
        responseObserver.onNext(BoolValue.of(record != null));
        responseObserver.onCompleted();
    }

    @Override
    public void clickNewTextButton(VisitType request, StreamObserver<BoolValue> responseObserver) {
        MainPane mainPane = Globals.getInstance().getMainPane();
        Platform.runLater(() -> {
            boolean ok = mainPane.simulateNewTextButtonClick(request.getVisitId());
            responseObserver.onNext(BoolValue.of(ok));
            responseObserver.onCompleted();
        });
    }

    private void completeWithBool(boolean value, StreamObserver<BoolValue> responseObserver){
        responseObserver.onNext(BoolValue.of(value));
        responseObserver.onCompleted();
    }

    private Record findRecord(int visitId){
        MainPane mainPane = Globals.getInstance().getMainPane();
        return mainPane.findRecord(visitId);
    }

    private TextEnterForm findTextEnterForm(int visitId){
        Record record = findRecord(visitId);
        if (record == null) {
            return null;
        } else {
            return record.findTextEnterForm();
        }
    }

    @Override
    public void isNewTextFormVisible(VisitType request, StreamObserver<BoolValue> responseObserver) {
        completeWithBool(findTextEnterForm(request.getVisitId())!= null, responseObserver);
    }

    @Override
    public void setNewTextInputs(NewTextInputs request, StreamObserver<BoolValue> responseObserver) {
        TextEnterForm form = findTextEnterForm(request.getVisitId());
        if( form == null ) {
            completeWithBool(false, responseObserver);
            return;
        }
        form.setContent(request.getContent());
        completeWithBool(true, responseObserver);
    }

    @Override
    public void clickEnterButtonInNextTextForm(VisitType request, StreamObserver<BoolValue> responseObserver) {
        TextEnterForm form = findTextEnterForm(request.getVisitId());
        if( form == null ) {
            completeWithBool(false, responseObserver);
            return;
        }
        form.simulateClickEnterButton();
        completeWithBool(true, responseObserver);
    }

    @Override
    public void isTextVisible(TextType request, StreamObserver<BoolValue> responseObserver) {
        Record record = findRecord(request.getVisitId());
        RecordText recordText = record.findRecordText(request.getTextId());
        completeWithBool(recordText != null, responseObserver);
    }
}
