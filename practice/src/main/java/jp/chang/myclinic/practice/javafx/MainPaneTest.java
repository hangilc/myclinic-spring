package jp.chang.myclinic.practice.javafx;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jp.chang.myclinic.mockdata.MockData;
import jp.chang.myclinic.practice.testgui.TestEnv;
import jp.chang.myclinic.practice.testgui.TestGroup;
import jp.chang.myclinic.practice.testgui.TestHelper;

public class MainPaneTest extends TestGroup implements TestHelper {

    {
//        addTestProc("disp", this::testDisp);
//        addTestProc("broadcast-new-text", this::testBroadcastNewText);
    }

    private Stage stage;
    private Pane main;
    private MockData mock = new MockData();

    public MainPaneTest(TestEnv testEnv){
        this.stage = testEnv.stage;
        this.main = testEnv.main;
    }

//    private void testDisp(){
//        PracticeRestServiceMock restService = new PracticeRestServiceMock();
//        PracticeConfigServiceMock configService = new PracticeConfigServiceMock();
//        MainStageServiceMock stageService = new MainStageServiceMock();
//        MainPaneEnv req = new MainPaneEnv(restService, configService, stageService);
//        MainPane mainPane = new MainPane(req);
//        gui(() -> {
//            mainPane.getStylesheets().addAll("css/Practice.css");
//            main.getChildren().setAll(mainPane);
//            mainPane.setCurrent(null, 0);
//            stage.sizeToScene();
//        });
//        confirm(stageService.getTitle().equals("診察"));
//    }
//
//    private void testBroadcastNewText(){
//        PracticeRestServiceMock restService = new PracticeRestServiceMock();
//        PracticeConfigServiceMock configService = new PracticeConfigServiceMock();
//        MainStageServiceMock stageService = new MainStageServiceMock();
//        MainPaneEnv req = new MainPaneEnv(restService, configService, stageService);
//        MainPane mainPane = new MainPane(req);
//        LocalDateTime at1 = LocalDateTime.of(2019, 3, 4, 9, 0);
//        TestFramework tf = new TestFramework(restService);
//        VisitFull2DTO visit1 = tf.startVisit(1, at1);
//        VisitFull2DTO visit2 = tf.startVisit(1, at1.minus(7, ChronoUnit.DAYS));
//        TextDTO text2 = tf.addText(visit2, "頭痛がする。");
//        gui(() -> {
//            mainPane.getStylesheets().addAll("css/Practice.css");
//            main.getChildren().setAll(mainPane);
//            mainPane.setCurrent(null, 0);
//            stage.sizeToScene();
//            mainPane.setVisits(List.of(visit1, visit2));
//        });
//    }

}
