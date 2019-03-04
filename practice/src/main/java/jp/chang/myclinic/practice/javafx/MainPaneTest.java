package jp.chang.myclinic.practice.javafx;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.mockdata.MockData;
import jp.chang.myclinic.practice.MainStageServiceMock;
import jp.chang.myclinic.practice.PracticeConfigServiceMock;
import jp.chang.myclinic.practice.PracticeRestServiceMock;
import jp.chang.myclinic.practice.testgui.TestFramework;
import jp.chang.myclinic.practice.testgui.TestGroup;
import jp.chang.myclinic.practice.testgui.TestHelper;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class MainPaneTest extends TestGroup implements TestHelper {

    {
        addTestProc("disp", this::testDisp);
        addTestProc("broadcast-new-text", this::testBroadcastNewText);
    }

    private Stage stage;
    private Pane main;
    private MockData mock = new MockData();

    public MainPaneTest(Stage stage, Pane main){
        this.stage = stage;
        this.main = main;
    }

    private void testDisp(){
        PracticeRestServiceMock restService = new PracticeRestServiceMock();
        PracticeConfigServiceMock configService = new PracticeConfigServiceMock();
        MainStageServiceMock stageService = new MainStageServiceMock();
        MainPaneRequirement req = new MainPaneRequirement(restService, configService, stageService);
        MainPane mainPane = new MainPane(req);
        gui(() -> {
            mainPane.getStylesheets().addAll("css/Practice.css");
            main.getChildren().setAll(mainPane);
            mainPane.setCurrent(null, 0);
            stage.sizeToScene();
        });
        confirm(stageService.getTitle().equals("診察"));
    }

    private void testBroadcastNewText(){
        PracticeRestServiceMock restService = new PracticeRestServiceMock();
        PracticeConfigServiceMock configService = new PracticeConfigServiceMock();
        MainStageServiceMock stageService = new MainStageServiceMock();
        MainPaneRequirement req = new MainPaneRequirement(restService, configService, stageService);
        MainPane mainPane = new MainPane(req);
        LocalDateTime at1 = LocalDateTime.of(2019, 3, 4, 9, 0);
        TestFramework tf = new TestFramework(restService);
        VisitFull2DTO visit1 = tf.startVisit(1, at1);
        VisitFull2DTO visit2 = tf.startVisit(1, at1.minus(7, ChronoUnit.DAYS));
        TextDTO text2 = tf.addText(visit2, "頭痛がする。");
        gui(() -> {
            mainPane.getStylesheets().addAll("css/Practice.css");
            main.getChildren().setAll(mainPane);
            mainPane.setCurrent(null, 0);
            stage.sizeToScene();
            mainPane.setVisits(List.of(visit1, visit2));
        });
    }

}
