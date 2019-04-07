package jp.chang.myclinic.practice.componenttest.hoken;

import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.practice.componenttest.GroupTestBase;
import jp.chang.myclinic.practice.componenttest.TestInterface;

import java.util.List;

public class HokenTest extends GroupTestBase {

    public HokenTest(Stage stage, StackPane main) {
        super(stage, main);
    }

    @Override
    protected List<TestInterface> getTests() {
        return List.of(
                new HokenDispTest(stage, main),
                new HokenSelectFormTest(stage, main),
                new RecordHokenTest(stage, main)
        );
    }
}
