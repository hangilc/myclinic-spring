package jp.chang.myclinic.practice.guitest.records;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.mockdata.MockData;
import jp.chang.myclinic.mockdata.SamplePrescExample;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.CurrentPatientService;
import jp.chang.myclinic.practice.guitest.GuiTest;
import jp.chang.myclinic.practice.guitest.GuiTestBase;
import jp.chang.myclinic.practice.javafx.RecordsPane;
import jp.chang.myclinic.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
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
            pane.setPrefWidth(680);
            pane.setPrefHeight(Region.USE_COMPUTED_SIZE);
            pane.setMaxHeight(Region.USE_PREF_SIZE);
            ScrollPane scroll = new ScrollPane(pane);
            scroll.setPrefWidth(700);
            scroll.setPrefHeight(600);
            main.getChildren().setAll(scroll);
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

    @GuiTest
    public void disp3(){
        Frontend frontend = Context.frontend;
        MockData mock = new MockData();
        PatientDTO patient = mock.pickPatient();
        patient.patientId = frontend.enterPatient(patient).join();
        VisitDTO visit2 = frontend.startVisit(patient.patientId, prevDays(0)).join();
        VisitDTO visit1 = frontend.startVisit(patient.patientId, prevDays(7)).join();
        Context.currentPatientService = new CurrentPatientService();
        Context.currentPatientService.setCurrentPatient(patient, visit2.visitId);
        enterByPrescExample(visit1, SamplePrescExample.カロナール内服, 1);
        enterByPrescExample(visit1, SamplePrescExample.デキストロメトルファン, 1);
        enterByPrescExample(visit1, SamplePrescExample.アンブロキソール, 1);
        RecordsPane pane = createPane();
        gui(() -> {
            pane.addRecord(frontend.getVisitFull2(visit2.visitId).join(),
                    Collections.emptyMap(), Collections.emptyMap(), Collections.emptyMap());
            pane.addRecord(frontend.getVisitFull2(visit1.visitId).join(),
                    Collections.emptyMap(), Collections.emptyMap(), Collections.emptyMap());
        });
    }

    private DrugDTO enterByPrescExample(VisitDTO visit, PrescExampleDTO example, int prescribed){
        Frontend frontend = Context.frontend;
        LocalDate at = DateTimeUtil.parseSqlDateTime(visit.visitedAt).toLocalDate();
        IyakuhinMasterDTO master = frontend.resolveStockDrug(example.iyakuhincode, at).join();
        DrugDTO drug = new DrugDTO();
        drug.iyakuhincode = master.iyakuhincode;
        drug.amount = Double.parseDouble(example.amount);
        drug.category = example.category;
        drug.days = example.days;
        drug.prescribed = prescribed;
        drug.visitId = visit.visitId;
        drug.usage = example.usage;
        drug.drugId = frontend.enterDrug(drug).join();
        return drug;
    }


}
