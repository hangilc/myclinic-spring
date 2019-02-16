package jp.chang.myclinic.practice.testintegration;

import javafx.application.Platform;
import javafx.stage.Window;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.mockdata.MockData;
import jp.chang.myclinic.practice.Globals;
import jp.chang.myclinic.practice.javafx.*;
import jp.chang.myclinic.practice.javafx.drug.DrugEnterForm;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugSearchResultItem;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugSearcher;
import jp.chang.myclinic.util.HokenUtil;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class PracticeTestGui implements Runnable {

    //private static Logger logger = LoggerFactory.getLogger(PracticeSelfTest.class);
    private MockData mocker = new MockData();

    @Override
    public void run() {
        System.out.println("Self-test started");
        confirmMockPatient();
        System.out.println("Confirmed that mock database is connected.");
        testExam();
        System.out.println("Self-test completed successfully.");
    }

    private void testExam() {
        PatientDTO patient = apiCreatePatient();
        apiCreateShahokokuho(patient.patientId);
        int visitId = apiStartVisit(patient.patientId);
        VisitDTO visit = Service.api.getVisit(visitId).join();
        guiOpenSelectVisitWindow();
        SelectFromWqueueDialog selectVisitDialog = waitForCreatedWindow(SelectFromWqueueDialog.class);
        gui(() -> {
            boolean ok = selectVisitDialog.simulateSelectVisit(visitId);
            if (!ok) {
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
            for (Integer id : textIds) {
                if (id > lastTextId) {
                    return Optional.of(id);
                }
            }
            return Optional.empty();
        });
        RecordText newRecordText = record.findRecordText(newTextId).orElseThrow(() ->
                new RuntimeException("find record text failed.")
        );
        if (!newRecordText.getContentRep().equals(text)) {
            throw new RuntimeException("Incorrect text content.");
        }
        HokenDTO hoken = Service.api.getHoken(visitId).join();
        RecordHoken recordHoken = record.findRecordHoken().orElseThrow(
                () -> new RuntimeException("Failed to find record hoken.")
        );
        if (!recordHoken.getDispText().equals(HokenUtil.hokenRep(hoken))) {
            throw new RuntimeException("Hoken disp is not correct.");
        }
        gui(record::simulateNewDrugButtonClick);
        DrugEnterForm drugEnterForm = waitFor(record::findDrugEnterForm);
        int drugSearchResultSerialId = drugEnterForm.getSearchResultSerialId();
        gui(() -> {
            drugEnterForm.simulateSetSearchText("カロナール");
            drugEnterForm.simulateClickSearchButton();
        });
        waitFor(10, () -> {
            int id = drugEnterForm.getSearchResultSerialId();
            if (id > drugSearchResultSerialId) {
                return Optional.of(true);
            } else {
                return Optional.empty();
            }
        });
        List<DrugSearchResultItem> drugSearchResults = drugEnterForm.getSearchResultItems();
        int calonalIyakuhincode = 620000033;
        DrugSearchResultItem drugItem1 = null;
        for (DrugSearchResultItem item : drugSearchResults) {
            if (item instanceof DrugSearcher.ExampleItem) {
                DrugSearcher.ExampleItem exampleItem = (DrugSearcher.ExampleItem) item;
                PrescExampleFullDTO example = exampleItem.getExample();
                if (example.prescExample.prescExampleId == 553) {
                    drugItem1 = item;
                    break;
                }
            }
        }
        if( drugItem1 == null ){
            throw new RuntimeException("Cannot find caronal.");
        }
        final DrugSearchResultItem finalDrugItem1 = drugItem1;
        gui(() -> {
            drugEnterForm.simulateSelectSearchResultItem(finalDrugItem1);
        });
        waitFor(() -> {
            DrugSearchResultItem currentItem = drugEnterForm.getCurrentInputItem();
            if( currentItem == finalDrugItem1 ){
                return Optional.of(true);
            } else {
                return Optional.empty();
            }
        });
        gui(drugEnterForm::simulateClickEnterButton);
    }

    private void gui(Runnable runnable) {
        Platform.runLater(runnable);
    }

    private Record waitForRecord(int visitId) {
        MainPane mainPane = getMainPane();
        return waitFor(10, () -> {
            Record r = mainPane.findRecord(visitId);
            return Optional.ofNullable(r);
        });
    }

    private <T extends Window> T waitForCreatedWindow(Class<T> windowClass) {
        return waitFor(10, () -> {
            T t = Globals.getInstance().findNewWindow(windowClass);
            return Optional.ofNullable(t);
        });
    }

    private <T> T waitFor(Supplier<Optional<T>> f) {
        return waitFor(5, f);
    }

    private <T> T waitFor(int n, Supplier<Optional<T>> f) {
        for (int i = 0; i < n; i++) {
            Optional<T> t = f.get();
            if (t.isPresent()) {
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

    private MainPane getMainPane() {
        return Globals.getInstance().getMainPane();
    }

    private void guiOpenSelectVisitWindow() {
        getMainPane().simulateSelectVisitMenuChoice();
    }

    private PatientDTO apiCreatePatient() {
        PatientDTO patient = mocker.pickPatient();
        patient.patientId = Service.api.enterPatient(patient).join();
        return patient;
    }

    private ShahokokuhoDTO apiCreateShahokokuho(int patientId) {
        ShahokokuhoDTO hoken = mocker.pickShahokokuho(patientId);
        hoken.shahokokuhoId = Service.api.enterShahokokuho(hoken).join();
        return hoken;
    }

    private int apiStartVisit(int patientId) {
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
