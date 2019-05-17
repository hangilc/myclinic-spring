package jp.chang.myclinic.practice.guitest.drug;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.mockdata.MockData;
import jp.chang.myclinic.mockdata.SampleData;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.guitest.GuiTestMarker;
import jp.chang.myclinic.practice.guitest.GuiTestBase;
import jp.chang.myclinic.practice.javafx.drug.DrugEditForm;

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

    @GuiTestMarker
    public void disp(){
        MockData mock = new MockData();
        Frontend frontend = Context.frontend;
        PatientDTO patient = mock.pickPatient();
        patient.patientId = frontend.enterPatient(patient).join();
        VisitDTO visit = frontend.startVisit(patient.patientId, LocalDateTime.now()).join();
        DrugFullDTO drug = SampleData.calonalDrugFull;
        drug.drug = DrugDTO.copy(drug.drug);
        drug.drug.drugId = 0;
        drug.drug.visitId = visit.visitId;
        drug.drug.drugId = frontend.enterDrug(drug.drug).join();
        DrugEditForm form = createForm(drug, null, visit);
        form.setOnEnteredHandler((argDrug, argAttr) -> {
            System.out.print("drug: ");
            printJson(argDrug);
            System.out.print("attr: ");
            printJson(argAttr);
        });
        form.setOnCancelHandler(() -> System.out.println("cancel"));
        form.setOnDeletedHandler(() -> System.out.println("deleted"));
    }
}
