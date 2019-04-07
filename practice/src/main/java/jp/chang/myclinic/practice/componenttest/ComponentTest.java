package jp.chang.myclinic.practice.componenttest;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.practice.componenttest.hoken.HokenTest;
import jp.chang.myclinic.practice.componenttest.mainpane.MainPaneTest;
import jp.chang.myclinic.practice.componenttest.text.TextEditFormTest;
import jp.chang.myclinic.practice.componenttest.text.TextEnterFormTest;
import jp.chang.myclinic.practice.componenttest.text.TextTest;

import java.util.ArrayList;
import java.util.List;

public class ComponentTest extends GroupTestBase {

    @Override
    protected List<TestInterface> getTests() {
        return List.of(
                new TextTest(stage, main),
                new MainPaneTest(stage, main),
                new HokenTest(stage, main)
        );
    }

    public ComponentTest(Stage stage, StackPane main) {
        super(stage, main);
    }

}
