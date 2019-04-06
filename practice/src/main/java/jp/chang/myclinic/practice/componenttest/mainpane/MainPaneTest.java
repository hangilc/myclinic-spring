package jp.chang.myclinic.practice.componenttest.mainpane;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.MainStageService;
import jp.chang.myclinic.practice.MainStageServiceAdapter;
import jp.chang.myclinic.practice.componenttest.CompTest;
import jp.chang.myclinic.practice.componenttest.GroupTestBase;
import jp.chang.myclinic.practice.componenttest.TestInterface;
import jp.chang.myclinic.practice.javafx.MainPane;

import java.util.List;
import java.util.function.Consumer;

public class MainPaneTest extends GroupTestBase {

    public MainPaneTest(Stage stage, Pane main) {
        super(stage, main);
    }

    @Override
    protected List<TestInterface> getTests() {
        return List.of(
                new MainPaneTextTest(stage, main)
        );
    }

    @Override
    public boolean testOne(String className, String methodName) {
        if( getClass().getSimpleName().equals(className) && "testMainPaneDisp".equals(methodName) ){
            testMainPaneDisp();
            return true;
        } else {
            return super.testOne(className, methodName);
        }
    }

    private void testMainPaneDisp(){
        Context.mainStageService = new MainStageServiceAdapter(){
            @Override
            public void setTitle(String title) {
                ;
            }
        };
        MainPane mainPane = new MainPane();
        Platform.runLater(() -> {
            main.getChildren().setAll(mainPane);
            stage.sizeToScene();
        });
    }
}
