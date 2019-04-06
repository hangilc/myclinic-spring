package jp.chang.myclinic.practice.componenttest.text;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jp.chang.myclinic.practice.componenttest.GroupTestBase;
import jp.chang.myclinic.practice.componenttest.TestInterface;

import java.util.List;

public class TextTest extends GroupTestBase {

    public TextTest(Stage stage, Pane main) {
        super(stage, main);
    }

    @Override
    protected List<TestInterface> getTests(){
        return List.of(
                new TextEnterFormTest(stage, main),
                new TextEditFormTest(stage, main),
                new TextDispTest(stage, main)
        );
    }

}
