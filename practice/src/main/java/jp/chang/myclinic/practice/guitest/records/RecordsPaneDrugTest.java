package jp.chang.myclinic.practice.guitest.records;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.mockdata.MockData;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.CurrentPatientService;
import jp.chang.myclinic.practice.guitest.GuiTest;
import jp.chang.myclinic.practice.guitest.GuiTestBase;
import jp.chang.myclinic.practice.javafx.RecordsPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

public class RecordsPaneDrugTest extends GuiTestBase {

    public RecordsPaneDrugTest(Stage stage, StackPane main) {
        super(stage, main);
    }

    private RecordsPane createPane(){
        RecordsPane pane = new RecordsPane();
        gui(() -> {
            pane.setPrefHeight(Region.USE_COMPUTED_SIZE);
            pane.setMaxHeight(Region.USE_PREF_SIZE);
            main.setPrefWidth(680);
            main.setPrefHeight(600);
            main.getChildren().setAll(pane);
            stage.sizeToScene();
        });
        return pane;
    }

    private LocalDateTime prevDays(int days){
        return LocalDateTime.now().minus(days, ChronoUnit.DAYS);
    }

    @GuiTest
    public void disp(){
        Frontend frontend = Context.frontend;
        MockData mock = new MockData();
        PatientDTO patient = mock.pickPatient();
        patient.patientId = frontend.enterPatient(patient).join();
        VisitDTO visit1 = frontend.startVisit(patient.patientId, prevDays(7)).join();
        VisitDTO visit2 = frontend.startVisit(patient.patientId, prevDays(0)).join();
        Context.currentPatientService = new CurrentPatientService();
        Context.currentPatientService.setCurrentPatient(patient, visit2.visitId);
        RecordsPane pane = createPane();
        gui(() -> {
            pane.addRecord(frontend.getVisitFull2(visit2.visitId).join(),
                    Collections.emptyMap(), Collections.emptyMap(), Collections.emptyMap());
            pane.addRecord(frontend.getVisitFull2(visit1.visitId).join(),
                    Collections.emptyMap(), Collections.emptyMap(), Collections.emptyMap());
        });

    }


}
