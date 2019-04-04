package jp.chang.myclinic.practice.componenttest;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.practice.componenttest.text.TextEditFormTest;
import jp.chang.myclinic.practice.componenttest.text.TextEnterFormTest;

import java.util.ArrayList;
import java.util.List;

public class ComponentTest {

    private List<ComponentTestBase> tests;

    private List<ComponentTestBase> createTests(Stage stage, Pane main) {
        List<ComponentTestBase> tests = new ArrayList<>();
        tests.add(new TextEnterFormTest(stage, main));
        tests.add(new TextEditFormTest(stage, main));
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
        System.out.println("done");
    }

    public void runOne(String className, String methodName){
        boolean match = false;
        for(ComponentTestBase test: tests){
            if( test.getClass().getSimpleName().equals(className) ){
                test.testOne(methodName);
                match = true;
                break;
            }
        }
        if( !match ){
            System.err.printf("Cannot find test (%s:%s)\n", className, methodName);
        } else {
            System.out.println("done");
        }
    }
}
