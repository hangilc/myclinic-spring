package jp.chang.myclinic.practice.guitest;

import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.backendsqlite.Test;
import jp.chang.myclinic.practice.guitest.drug.DrugTest;
import jp.chang.myclinic.practice.guitest.drug.RecordDrugsPaneTest;
import jp.chang.myclinic.practice.guitest.mainpane.MainPaneTest;
import jp.chang.myclinic.practice.guitest.records.RecordsPaneTest;
import jp.chang.myclinic.practice.guitest.shinryou.ShinryouTest;

import java.util.List;

public class GuiTestRunner extends GroupTestBase {

    public GuiTestRunner(Stage stage, StackPane main) {
        super(stage, main);
    }

    @Override
    protected List<TestInterface> getTests() {
        return List.of(
                new DrugTest(stage, main)
                , new ShinryouTest(stage, main)
                , new RecordDrugsPaneTest(stage, main)
                , new RecordsPaneTest(stage, main)
                , new MainPaneTest(stage, main)
        );
    }

}
