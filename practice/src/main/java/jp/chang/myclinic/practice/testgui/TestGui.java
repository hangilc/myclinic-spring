package jp.chang.myclinic.practice.testgui;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.backend.Backend;
import jp.chang.myclinic.backendasync.BackendAsyncBackend;
import jp.chang.myclinic.backendmock.BackendMock;
import jp.chang.myclinic.practice.PracticeConfigServiceMock;
import jp.chang.myclinic.practice.javafx.TestText;

public class TestGui extends GuiTestBase {

    private StackPane main = new StackPane();

    private void registerTests(TestEnv testEnv){
        addTestGroup("text", () -> new TestText(testEnv));
//        addTestGroup("shohousen", TestShohousen::new);
//        addTestGroup("main", () -> new MainPaneTest(stage, main));
    }

    public TestGui(Stage stage) {
        StackPane main = new StackPane();
        main.setStyle("-fx-padding: 10");
        main.getStylesheets().add("css/Practice.css");
        stage.setScene(new Scene(main));
        stage.show();
        TestEnv testEnv = new TestEnv();
        Backend backend = new BackendMock();
        testEnv.restService = new BackendAsyncBackend(backend);
        testEnv.configService = new PracticeConfigServiceMock();
        testEnv.stage = stage;
        testEnv.main = main;
        registerTests(testEnv);
    }

    @Override
    public void runTest(String test){
        try {
            new Thread(() -> {
                TestGui.super.runTest(test);
                System.out.println("test done");
            }).start();
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

}
