package jp.chang.myclinic.practice.guitest.drug;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.mockdata.MockData;
import jp.chang.myclinic.mockdata.SampleData;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.guitest.GuiTest;
import jp.chang.myclinic.practice.guitest.GuiTestBase;
import jp.chang.myclinic.practice.javafx.drug.DrugEditForm;
import jp.chang.myclinic.practice.javafx.drug.DrugEnterForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class DrugEditFormTest extends GuiTestBase {

    public DrugEditFormTest(Stage stage, StackPane main) {
        super(stage, main);
    }

    private DrugEditForm createForm(DrugFullDTO drug, DrugAttrDTO attr, VisitDTO visit){
        DrugEditForm form = new DrugEditForm(drug, attr, visit);
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
    public void disp(){
        MockData mock = new MockData();
        Frontend frontend = Context.frontend;
        PatientDTO patient = mock.pickPatient();
        patient.patientId = frontend.enterPatient(patient).join();
        VisitDTO visit = frontend.startVisit(patient.patientId, LocalDateTime.now()).join();
        DrugFullDTO drug = SampleData.calonalDrugFull;
        drug.drug = DrugDTO.copy(drug.drug);
        drug.drug.visitId = visit.visitId;
        frontend.enterDrug(drug.drug);
        DrugAttrDTO attr = new DrugAttrDTO();
        DrugEditForm form = createForm(drug, attr, visit);
        form.setOnCloseHandler((argDrug, argAttr) -> {
            System.out.print("drug: ");
            printJson(argDrug);
            System.out.print("attr: ");
            printJson(argAttr);
        });
    }
}
