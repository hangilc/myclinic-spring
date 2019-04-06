package jp.chang.myclinic.practice.componenttest.mainpane;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.frontend.FrontendAdapter;
import jp.chang.myclinic.mockdata.MockData;
import jp.chang.myclinic.practice.*;
import jp.chang.myclinic.practice.componenttest.CompTest;
import jp.chang.myclinic.practice.componenttest.ComponentTestBase;
import jp.chang.myclinic.practice.javafx.MainPane;
import jp.chang.myclinic.practice.javafx.Record;
import jp.chang.myclinic.practice.javafx.RecordText;
import jp.chang.myclinic.practice.javafx.text.TextDisp;
import jp.chang.myclinic.practice.javafx.text.TextEditForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MainPaneTextTest extends ComponentTestBase {

    public MainPaneTextTest(Stage stage, Pane main) {
        super(stage, main);
    }

    private MainPane createMainPane(Frontend frontend){
        Context.mainStageService = new MainStageServiceAdapter(){
            @Override
            public void setTitle(String title) {
                ;
            }
        };
        Context.frontend = frontend;
        Context.currentPatientService = new CurrentPatientService();
        MainPane mainPane = new MainPane();
        gui(() -> {
            main.getChildren().setAll(mainPane);
            stage.sizeToScene();
        });
        return mainPane;
    }

    @CompTest(excludeFromBatch = true)
    public void testMainPaneTextDisp(){
        MainPane mainPane = createMainPane(new FrontendBase());
        VisitFull2DTO visitFull = createBlankVisitFull2(1, 1, LocalDateTime.now());
        mainPane.setVisits(List.of(visitFull));
    }

    private class FrontendBase extends FrontendAdapter {
        @Override
        public CompletableFuture<List<DrugAttrDTO>> batchGetDrugAttr(List<Integer> drugIds) {
            return value(Collections.emptyList());
        }

        @Override
        public CompletableFuture<List<ShoukiDTO>> batchGetShouki(List<Integer> visitIds) {
            return value(Collections.emptyList());
        }

        @Override
        public CompletableFuture<List<ShinryouAttrDTO>> batchGetShinryouAttr(List<Integer> shinryouIds) {
            return value(Collections.emptyList());
        }

    }

    @CompTest
    public void testMainPaneTextCopy(){
        TextDTO text = new TextDTO();
        text.visitId = 1;
        text.textId = 1;
        text.content = "体調いい。";
        Context.integrationService = new IntegrationServiceImpl();
        MainPane mainPane = createMainPane(new FrontendBase(){
            @Override
            public CompletableFuture<Integer> enterText(TextDTO entered) {
                entered.textId = text.textId + 1;
                return value(entered.textId);
            }
        });
        VisitFull2DTO visitFull2 = createBlankVisitFull2(2, 1, LocalDateTime.now());
        VisitFull2DTO visitFull1 = createBlankVisitFull2(1, 1, LocalDateTime.now().minus(7, ChronoUnit.DAYS));
        visitFull1.texts.add(text);
        mainPane.setVisits(List.of(visitFull2, visitFull1));
        Context.currentPatientService = new CurrentPatientService();
        MockData mockData = new MockData();
        PatientDTO patient = mockData.pickPatientWithPatientId();
        Context.currentPatientService.setCurrentPatient(patient, 2);
        Record record1 = waitFor(() -> mainPane.findRecord(1));
        RecordText recordText = waitFor(() -> record1.findRecordText(1));
        TextDisp textDisp = waitFor(recordText::findTextDisp);
        gui(() -> textDisp.simulateMouseEvent(createMouseClickedEvent(textDisp)));
        TextEditForm form = waitFor(recordText::findTextEditForm);
        gui(form::simulateClickCopyButton);
        Record record2 = waitFor(() -> mainPane.findRecord(2));
        RecordText recordText2 = waitFor(() -> record2.findRecordText(text.textId + 1));
        TextDisp textDisp2 = waitFor(recordText2::findTextDisp);
        confirm(textDisp2.getRep().equals(text.content));
    }

}
