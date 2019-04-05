package jp.chang.myclinic.practice.componenttest;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.practice.componenttest.text.TextEditFormTest;
import jp.chang.myclinic.practice.componenttest.text.TextEnterFormTest;

import java.util.ArrayList;
import java.util.List;

public class ComponentTest {

    private Stage stage;
    private Pane main;
    private List<ComponentTestBase> tests;

    private List<ComponentTestBase> createTests(Stage stage, Pane main) {
        List<ComponentTestBase> tests = new ArrayList<>();
        tests.add(new TextEnterFormTest(stage, main));
        tests.add(new TextEditFormTest(stage, main));
        return tests;
    }

    public ComponentTest(Stage stage, Pane main) {
        this.stage = stage;
        this.main = main;
        this.tests = createTests(stage, main);
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
