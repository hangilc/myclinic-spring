package jp.chang.myclinic.practice.guitest.shinryou;

import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.practice.guitest.GroupTestBase;
import jp.chang.myclinic.practice.guitest.TestInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ShinryouTest extends GroupTestBase {

    public ShinryouTest(Stage stage, StackPane main) {
        super(stage, main);
    }

    @Override
    protected List<TestInterface> getTests() {
        return List.of();
    }

}
