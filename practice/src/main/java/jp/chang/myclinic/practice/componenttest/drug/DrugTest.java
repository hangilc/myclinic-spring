package jp.chang.myclinic.practice.componenttest.drug;

import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.practice.componenttest.GroupTestBase;
import jp.chang.myclinic.practice.componenttest.TestInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DrugTest extends GroupTestBase {

    public DrugTest(Stage stage, StackPane main) {
        super(stage, main);
    }

    @Override
    protected List<TestInterface> getTests() {
        return List.of(
                new DrugDispTest(stage, main),
                new DrugInputTest(stage, main)
        );
    }

}
