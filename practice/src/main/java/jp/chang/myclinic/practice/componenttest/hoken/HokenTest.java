package jp.chang.myclinic.practice.componenttest.hoken;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jp.chang.myclinic.practice.componenttest.GroupTestBase;
import jp.chang.myclinic.practice.componenttest.TestInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HokenTest extends GroupTestBase {

    public HokenTest(Stage stage, Pane main) {
        super(stage, main);
    }

    @Override
    protected List<TestInterface> getTests() {
        return List.of(
                new HokenDispTest(stage, main)
        );
    }
}
