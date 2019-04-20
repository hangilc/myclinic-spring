package jp.chang.myclinic.practice.guitest.mainpane;

import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.mockdata.MockData;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.CurrentPatientService;
import jp.chang.myclinic.practice.MainStageServiceAdapter;
import jp.chang.myclinic.practice.guitest.GuiTest;
import jp.chang.myclinic.practice.guitest.GuiTestBase;
import jp.chang.myclinic.practice.javafx.MainPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class MainPaneBlankTest extends GuiTestBase {

    public MainPaneBlankTest(Stage stage, StackPane main) {
        super(stage, main);
    }

    private MainPane createMainPane(){
        Context.mainStageService = new MainStageServiceAdapter(){
            @Override
            public void setTitle(String title) {
                ;
            }
        };
        Context.currentPatientService = new CurrentPatientService();
        MainPane mainPane = new MainPane();
        gui(() -> {
            main.getChildren().setAll(mainPane);
            stage.sizeToScene();
        });
        return mainPane;
    }

    @GuiTest
    public void disp(){
        Frontend frontend = Context.frontend;
        MockData mock = new MockData();
        PatientDTO patient = mock.pickPatient();
        patient.patientId = frontend.enterPatient(patient).join();
        VisitDTO visit = frontend.startVisit(patient.patientId, LocalDateTime.now()).join();
        createMainPane();
    }

    @GuiTest
    public void nav(){
        Frontend frontend = Context.frontend;
        MockData mock = new MockData();
        PatientDTO patient = mock.pickPatient();
        patient.patientId = frontend.enterPatient(patient).join();
        List<LocalDateTime> prevTimes = IntStream.range(1, 16)
                .mapToObj(i -> LocalDateTime.now().minus(i, ChronoUnit.DAYS))
                .collect(toList());
        Collections.reverse(prevTimes);
        prevTimes.forEach(at -> {
            VisitDTO visit = frontend.startVisit(patient.patientId, at).join();
            frontend.endExam(visit.visitId, 0);
            frontend.deleteWqueue(visit.visitId);
        });
        VisitDTO visit = frontend.startVisit(patient.patientId, LocalDateTime.now()).join();
        createMainPane();
    }

}
