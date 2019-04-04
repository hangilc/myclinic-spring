package jp.chang.myclinic.practice.componenttest;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.practice.componenttest.text.TextFormTest;

import java.util.ArrayList;
import java.util.List;

public class ComponentTest {

    private List<ComponentTestBase> tests;

    private List<ComponentTestBase> createTests(Stage stage, Pane main) {
        List<ComponentTestBase> tests = new ArrayList<>();
        tests.add(new TextFormTest(stage, main));
        return tests;
    }

    public ComponentTest(Stage stage) {
        Pane main  = new StackPane();
        this.tests = createTests(stage, main);
        main.setStyle("-fx-padding: 10");
        main.getStylesheets().add("css/Practice.css");
        stage.setScene(new Scene(main));
        stage.show();
    }

    public void runAll() {
        tests.forEach(ComponentTestBase::testAll);
    }

    public void runOne(String testName){
        boolean match = tests.stream().anyMatch(t -> t.testOne(testName));
        if( !match ){
            System.err.println("Cannot find test: " + testName);
        }
    }
}
