package jp.chang.myclinic.practice.guitest.records;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.mockdata.MockData;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.CurrentPatientService;
import jp.chang.myclinic.practice.guitest.GuiTestMarker;
import jp.chang.myclinic.practice.guitest.GuiTestBase;
import jp.chang.myclinic.practice.javafx.Record;
import jp.chang.myclinic.practice.javafx.RecordText;
import jp.chang.myclinic.practice.javafx.RecordsPane;
import jp.chang.myclinic.practice.javafx.text.TextDisp;
import jp.chang.myclinic.practice.javafx.text.TextEditForm;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

public class RecordsPaneTextTest extends GuiTestBase {

    public RecordsPaneTextTest(Stage stage, StackPane main) {
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

    private TextDTO enterText(int visitId, String content){
        TextDTO text = TextDTO.create(visitId, content);
        text.textId = Context.frontend.enterText(text).join();
        return text;
    }

    @GuiTestMarker
    public void copy(){
        Frontend frontend = Context.frontend;
        MockData mock = new MockData();
        PatientDTO patient = mock.pickPatient();
        patient.patientId = frontend.enterPatient(patient).join();
        VisitDTO visit2 = frontend.startVisit(patient.patientId, prevDays(0)).join();
        VisitDTO visit1 = frontend.startVisit(patient.patientId, prevDays(7)).join();
        TextDTO text1 = enterText(visit1.visitId, "テスト");
        Context.currentPatientService = new CurrentPatientService();
        Context.currentPatientService.setCurrentPatient(patient, visit2.visitId);
        RecordsPane pane = createPane();
        gui(() -> {
            pane.addRecord(frontend.getVisitFull2(visit2.visitId).join(),
                    Collections.emptyMap(), Collections.emptyMap(), Collections.emptyMap());
            pane.addRecord(frontend.getVisitFull2(visit1.visitId).join(),
                    Collections.emptyMap(), Collections.emptyMap(), Collections.emptyMap());
        });
        Record record1 = waitFor(() -> pane.findRecord(visit1.visitId));
        RecordText recordText1 = waitFor(() -> record1.findRecordText(text1.textId));
        TextDisp textDisp1 = waitFor(recordText1::findTextDisp);
        gui(() -> textDisp1.fireEvent(createMouseClickedEvent(textDisp1)));
        TextEditForm editForm1 = waitFor(recordText1::findTextEditForm);
        gui(editForm1::simulateClickCopyButton);
        Record record2 = waitFor(() -> pane.findRecord(visit2.visitId));
        waitForTrue(() -> record2.listTextId().size() > 0);
        int textId2 = record2.listTextId().get(0);
        TextDTO text2 = frontend.getText(textId2).join();
        confirm(text1.content.equals(text2.content));
    }

}
