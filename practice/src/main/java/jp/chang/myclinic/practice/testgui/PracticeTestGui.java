package jp.chang.myclinic.practice.testgui;

import javafx.application.Platform;
import javafx.stage.Window;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.mockdata.MockData;
import jp.chang.myclinic.practice.Globals;
import jp.chang.myclinic.practice.javafx.*;
import jp.chang.myclinic.practice.javafx.disease.Select;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class PracticeTestGui implements Runnable {

    //private static Logger logger = LoggerFactory.getLogger(PracticeSelfTest.class);
    private MockData mocker = new MockData();

    @Override
    public void run(){
        System.out.println("Self-test started");
        confirmMockPatient();
        System.out.println("Confirmed that mock database is connected.");
        testExam();
        System.out.println("Self-test completed without error.");
    }

    private void testExam(){
        PatientDTO patient = apiCreatePatient();
        apiCreateShahokokuho(patient.patientId);
        int visitId = apiStartVisit(patient.patientId);
        guiOpenSelectVisitWindow();
        SelectFromWqueueDialog selectVisitDialog = waitForCreatedWindow(SelectFromWqueueDialog.class);
        gui(() -> {
            boolean ok = selectVisitDialog.simulateSelectVisit(visitId);
            if( !ok ){
                throw new RuntimeException("Selecting from wqueue failed.");
            }
            selectVisitDialog.simulateSelectButtonClick();
        });
        Record record = waitForRecord(visitId);
        gui(record::simulateNewTextButtonClick);
        TextEnterForm textEnterForm = waitFor(record::findTextEnterForm);
        int lastTextId = record.getLastTextId();
        String text = "昨日から、のどの痛みと鼻水がある。";
        gui(() -> {
            textEnterForm.setContent(text);
            textEnterForm.simulateClickEnterButton();
        });
        int newTextId = waitFor(10, () -> {
            List<Integer> textIds = record.listTextId();
            for(Integer id: textIds){
                if( id > lastTextId ){
                    return Optional.of(id);
                }
            }
            return Optional.empty();
        });
        RecordText newRecordText = record.findRecordText(newTextId).orElseThrow(() ->
                new RuntimeException("find record text failed."));
    }

    private void gui(Runnable runnable){
        Platform.runLater(runnable);
    }

    Record waitForRecord(int visitId){
        MainPane mainPane = getMainPane();
        return waitFor(10, () -> {
            Record r = mainPane.findRecord(visitId);
            return Optional.ofNullable(r);
        });
    }

    <T extends Window> T waitForCreatedWindow(Class<T> windowClass){
        return waitFor(10, () -> {
            T t = Globals.getInstance().findNewWindow(windowClass);
            return Optional.ofNullable(t);
        });
    }

    <T> T waitFor(Supplier<Optional<T>> f){
        return waitFor(5, f);
    }

    <T> T waitFor(int n, Supplier<Optional<T>> f){
        for(int i=0;i<n;i++){
            Optional<T> t = f.get();
            if( t.isPresent() ){
                return t.get();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException("Thread.sleep failed.");
            }
        }
        throw new RuntimeException("waitFor failed");
    }

    private MainPane getMainPane(){
        return Globals.getInstance().getMainPane();
    }

    private void guiOpenSelectVisitWindow(){
        getMainPane().simulateSelectVisitMenuChoice();
    }

    private PatientDTO apiCreatePatient(){
        PatientDTO patient = mocker.pickPatient();
        patient.patientId = Service.api.enterPatient(patient).join();
        return patient;
    }

    private ShahokokuhoDTO apiCreateShahokokuho(int patientId){
        ShahokokuhoDTO hoken = mocker.pickShahokokuho(patientId);
        hoken.shahokokuhoId = Service.api.enterShahokokuho(hoken).join();
        return hoken;
    }

    private int apiStartVisit(int patientId){
        return Service.api.startVisit(patientId).join();
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


}
