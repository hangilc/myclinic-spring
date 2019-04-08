package jp.chang.myclinic.practice.componenttest.text;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.practice.componenttest.GroupTestBase;
import jp.chang.myclinic.practice.componenttest.TestInterface;

import java.util.List;

public class TextTest extends GroupTestBase {

    public TextTest(Stage stage, StackPane main) {
        super(stage, main);
    }

    @Override
    protected List<TestInterface> getTests(){
        return List.of(
                new TextEnterFormTest(stage, main),
                new TextEditFormTest(stage, main),
                new TextDispTest(stage, main),
                new RecordTextTest(stage, main),
                new TextsPaneTest(stage, main)
        );
    }

}