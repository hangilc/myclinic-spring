package jp.chang.myclinic.practice.guitest.records;

import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.practice.guitest.GroupTestBase;
import jp.chang.myclinic.practice.guitest.TestInterface;

import java.util.List;

public class RecordsPaneTest extends GroupTestBase {

    public RecordsPaneTest(Stage stage, StackPane main) {
        super(stage, main);
    }

    @Override
    protected List<TestInterface> getTests() {
        return List.of(
                new RecordsPaneTextTest(stage, main),
                new RecordsPaneDrugTest(stage, main),
                new RecordsPaneShinryouTest(stage, main),
                new RecordsPaneConductTest(stage, main),
                new RecordsPaneChargeTest(stage, main)
        );
    }
}
