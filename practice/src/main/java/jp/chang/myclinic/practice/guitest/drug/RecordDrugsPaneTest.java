package jp.chang.myclinic.practice.guitest.drug;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.DrugAttrDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.mockdata.MockData;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.CurrentPatientService;
import jp.chang.myclinic.practice.guitest.GuiTest;
import jp.chang.myclinic.practice.guitest.GuiTestBase;
import jp.chang.myclinic.practice.javafx.RecordDrugsPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RecordDrugsPaneTest extends GuiTestBase {

    public RecordDrugsPaneTest(Stage stage, StackPane main) {
        super(stage, main);
    }

    private RecordDrugsPane createPane(List<DrugFullDTO> drugs, VisitDTO visit,
                                       Map<Integer, DrugAttrDTO> attrMap){
        RecordDrugsPane pane = new RecordDrugsPane(drugs, visit, attrMap);
        gui(() -> {
            pane.setPrefHeight(Region.USE_COMPUTED_SIZE);
            pane.setMaxHeight(Region.USE_PREF_SIZE);
            main.setPrefWidth(329);
            main.setPrefHeight(460);
            main.getChildren().setAll(pane);
            stage.sizeToScene();
        });
        return pane;
    }

    @GuiTest
    public void disp(){
        Context.currentPatientService = new CurrentPatientService();
        Frontend frontend = Context.frontend;
        MockData mock = new MockData();
        PatientDTO patient = mock.pickPatient();
        patient.patientId = frontend.enterPatient(patient).join();
        VisitDTO visit = frontend.startVisit(patient.patientId, LocalDateTime.now()).join();
        frontend.startExam(visit.visitId).join();
        Context.currentPatientService.setCurrentPatient(patient, visit.visitId);
        RecordDrugsPane pane = createPane(Collections.emptyList(), visit, Collections.emptyMap());
    }

}
