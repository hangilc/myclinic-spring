package jp.chang.myclinic.practice.guitest.drug;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.frontend.FrontendProxy;
import jp.chang.myclinic.mockdata.MockData;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.guitest.GuiTest;
import jp.chang.myclinic.practice.guitest.GuiTestBase;
import jp.chang.myclinic.practice.javafx.drug.DrugEnterForm;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugSearchResultItem;
import jp.chang.myclinic.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
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

    @GuiTest
    public void resolveInStock(){
        MockData mock = new MockData();
        Frontend frontend = Context.frontend;
        PatientDTO patient = mock.pickPatient();
        patient.patientId = frontend.enterPatient(patient).join();
        VisitDTO visit = frontend.startVisit(patient.patientId, LocalDateTime.now()).join();
        DrugEnterForm form = createForm(visit);
        int searchSerialId = form.getSearchResultSerialId();
        gui(() -> {
            form.simulateSetSearchText("ビオフェルミン");
            form.simulateClickSearchButton();
        });
        waitForTrue(() -> form.getSearchResultSerialId() > searchSerialId);
        DrugSearchResultItem resultItemProbe = null;
        for(DrugSearchResultItem item: form.getSearchResultItems()){
            System.out.println(item.getRep());
            if( item.getRep().startsWith("ビオフェルミン 3ｇ") ){
                resultItemProbe = item;
                break;
            }
        }
        confirm(resultItemProbe != null);
        final DrugSearchResultItem resultItem = resultItemProbe;
        gui(() -> form.simulateSelectSearchResultItem(resultItem));
        waitForTrue(() -> form.getIyakuhincode() != 0);
        LocalDate at = DateTimeUtil.parseSqlDateTime(visit.visitedAt).toLocalDate();
        IyakuhinMasterDTO selectedMaster = frontend.getIyakuhinMaster(form.getIyakuhincode(), at).join();
        confirm(selectedMaster != null);
    }

}
