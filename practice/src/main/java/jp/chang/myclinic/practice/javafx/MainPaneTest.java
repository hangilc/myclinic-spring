package jp.chang.myclinic.practice.javafx;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.mockdata.MockData;
import jp.chang.myclinic.practice.javafx.shohousen.ShohousenService;
import jp.chang.myclinic.practice.javafx.shohousen.ShohousenServiceMock;
import jp.chang.myclinic.practice.javafx.text.TextLib;
import jp.chang.myclinic.practice.javafx.text.TextLibAdapter;
import jp.chang.myclinic.practice.testgui.TestGroup;
import jp.chang.myclinic.practice.testgui.TestHelper;
import jp.chang.myclinic.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class MainPaneTest extends TestGroup implements TestHelper {

    {
        addTestProc("disp", this::testDisp);
        addTestProc("disp-exam", this::testDispExam);
    }

    private Stage stage;
    private Pane main;
    private MockData mock = new MockData();

    public MainPaneTest(Stage stage, Pane main){
        this.stage = stage;
        this.main = main;
    }

    private void setUpdateMainStageTitle(Stage stage, PatientDTO patient){
        if (patient == null) {
            stage.setTitle("診察");
        } else {
            String title = String.format("診察 (%d) %s%s",
                    patient.patientId,
                    patient.lastName,
                    patient.firstName);
            stage.setTitle(title);
        }
    }

    private void testDisp(){
        MainPane mainPane = MainPane.getInstance();
        MainPaneLib lib = new MainPaneLibAdapter(){
            @Override
            public void updateTitle(PatientDTO patient) {
                setUpdateMainStageTitle(stage, patient);
            }
        };
        mainPane.setMainPaneRequirement(lib);
        gui(() -> {
            mainPane.getStylesheets().addAll("css/Practice.css");
            main.getChildren().setAll(mainPane);
            mainPane.setCurrent(null, 0);
            stage.sizeToScene();
        });
    }

    private void testDispExam(){
        PatientDTO patient = mock.pickPatientWithPatientId();
        VisitDTO visit = new VisitDTO();
        visit.patientId = patient.patientId;
        visit.visitedAt = DateTimeUtil.toSqlDateTime(LocalDateTime.now());
        visit.visitId = 2;
        HokenDTO hoken = new HokenDTO();
        VisitFull2DTO visitFull = new VisitFull2DTO();
        visitFull.visit = visit;
        visitFull.texts = List.of();
        visitFull.hoken = hoken;
        visitFull.drugs = List.of();
        visitFull.shinryouList = List.of();
        visitFull.conducts = List.of();
        VisitDTO visitPrev = new VisitDTO();
        visitPrev.patientId = patient.patientId;
        visitPrev.visitedAt = DateTimeUtil.toSqlDateTime(LocalDateTime.now().minus(3, ChronoUnit.DAYS));
        visitPrev.visitId = 1;
        VisitFull2DTO visitPrevFull = new VisitFull2DTO();
        visitPrevFull.visit = visitPrev;
        visitPrevFull.texts = List.of();
        visitPrevFull.hoken = hoken;
        visitPrevFull.drugs = List.of();
        visitPrevFull.shinryouList = List.of();
        visitPrevFull.conducts = List.of();
        MainPane mainPane = MainPane.getInstance();
        class State {
            private int serialTextId = 1;
        }
        State state = new State();
        CurrentExamLib currentExamLib = new CurrentExamLib(){
            @Override
            public PatientDTO getCurrentPatient() {
                return patient;
            }

            @Override
            public int getCurrentVisitId() {
                return visit.visitId;
            }

            @Override
            public int getTempVisitId() {
                return 0;
            }
        };
        ShohousenService shohousenLib = ShohousenServiceMock.create(patient, visit);
        TextLib textLib = new TextLibAdapter(){
            @Override
            public CurrentExamLib getCurrentExamLib() {
                return currentExamLib;
            }

            @Override
            public CompletableFuture<Integer> enterText(TextDTO text) {
                return CompletableFuture.completedFuture(state.serialTextId++);
            }

            @Override
            public CompletableFuture<Boolean> updateText(TextDTO textDTO) {
                return CompletableFuture.completedFuture(true);
            }

            @Override
            public CompletableFuture<Boolean> deleteText(int textId) {
                return CompletableFuture.completedFuture(true);
            }

            @Override
            public ShohousenService getShohousenLib() {
                return shohousenLib;
            }
        };
        MainPaneLib lib = new MainPaneLibAdapter(){
            @Override
            public void updateTitle(PatientDTO patient) {
                setUpdateMainStageTitle(stage, patient);
            }

            @Override
            public CompletableFuture<List<ShinryouAttrDTO>> batchGetShinryouAttr(List<Integer> shinryouIds) {
                return CompletableFuture.completedFuture(Collections.emptyList());
            }

            @Override
            public CompletableFuture<List<DrugAttrDTO>> batchGetDrugAttr(List<Integer> drugIds) {
                return CompletableFuture.completedFuture(Collections.emptyList());
            }

            @Override
            public CompletionStage<List<ShoukiDTO>> batchGetShouki(List<Integer> visitIds) {
                return CompletableFuture.completedFuture(Collections.emptyList());
            }

            @Override
            public TextLib getTextLib() {
                return textLib;
            }
        };
        mainPane.setMainPaneRequirement(lib);
        gui(() -> {
            mainPane.getStylesheets().addAll("css/Practice.css");
            main.getChildren().setAll(mainPane);
            mainPane.setCurrent(patient, visit.visitId);
            mainPane.setVisits(List.of(visitFull, visitPrevFull));
            stage.sizeToScene();
        });
    }

}
