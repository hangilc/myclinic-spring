package jp.chang.myclinic.practice.guitest.drug;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.frontend.FrontendProxy;
import jp.chang.myclinic.mockdata.MockData;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.guitest.GuiTest;
import jp.chang.myclinic.practice.guitest.GuiTestBase;
import jp.chang.myclinic.practice.javafx.drug.DrugEnterForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

public class DrugEnterFormTest extends GuiTestBase {

    public DrugEnterFormTest(Stage stage, StackPane main) {
        super(stage, main);
    }

    private DrugEnterForm createForm(VisitDTO visit) {
        DrugEnterForm form = new DrugEnterForm(visit);
        gui(() -> {
            form.setPrefHeight(Region.USE_COMPUTED_SIZE);
            form.setMaxHeight(Region.USE_PREF_SIZE);
            main.setPrefWidth(329);
            main.setPrefHeight(460);
            main.getChildren().setAll(form);
            stage.sizeToScene();
        });
        return form;
    }

    @GuiTest
    public void disp() {
        MockData mock = new MockData();
        Frontend frontend = Context.frontend;
        PatientDTO patient = mock.pickPatient();
        patient.patientId = frontend.enterPatient(patient).join();
        VisitDTO visit = frontend.startVisit(patient.patientId, LocalDateTime.now()).join();
        DrugEnterForm form = createForm(visit);
        form.setOnDrugEnteredHandler((drug, attr) -> {
            System.out.println("entered drug: " + drug);
            System.out.println("entered drug attr: " + attr);
        });
    }

}
