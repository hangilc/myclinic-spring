package jp.chang.myclinic.practice.guitest.drug;

import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.practice.guitest.GroupTestBase;
import jp.chang.myclinic.practice.guitest.TestInterface;

import java.util.List;

public class DrugTest extends GroupTestBase {

    public DrugTest(Stage stage, StackPane main) {
        super(stage, main);
    }

    @Override
    protected List<TestInterface> getTests() {
        return List.of(
                new DrugEnterFormTest(stage, main),
                new DrugEditFormTest(stage, main),
                new RecordDrugsPaneTest(stage, main)
        );
    }
}
