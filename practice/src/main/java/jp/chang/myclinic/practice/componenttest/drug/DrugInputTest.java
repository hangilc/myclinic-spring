package jp.chang.myclinic.practice.componenttest.drug;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.PrescExampleDTO;
import jp.chang.myclinic.dto.PrescExampleFullDTO;
import jp.chang.myclinic.mockdata.IyakuhinMasterData;
import jp.chang.myclinic.practice.componenttest.CompTest;
import jp.chang.myclinic.practice.componenttest.ComponentTestBase;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugInput;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugInputState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrugInputTest extends ComponentTestBase {

    public DrugInputTest(Stage stage, StackPane main) {
        super(stage, main);
    }

    private DrugInput createInput(DrugInputState state) {
        DrugInput input = new DrugInput();
        input.setStateFrom(state);
        gui(() -> {
            input.setPrefHeight(Region.USE_COMPUTED_SIZE);
            input.setMaxHeight(Region.USE_PREF_SIZE);
            main.setPrefWidth(329);
            main.setPrefHeight(300);
            main.getChildren().setAll(input);
            stage.sizeToScene();
        });
        return input;
    }

    @CompTest
    public void disp() {
        DrugInputState state = new DrugInputState();
        createInput(state);
    }

    @CompTest
    public void setMaster() {
        DrugInputState state = new DrugInputState();
        state.setMaster(IyakuhinMasterData.calonal);
        DrugInput input = createInput(state);
        DrugInputState state2 = new DrugInputState();
        input.getStateTo(state2);
        confirm(state2.equals(state));
    }

    @CompTest
    public void setPrescExample(){
        DrugInputState state = new DrugInputState();
        PrescExampleFullDTO exFull = new PrescExampleFullDTO();
        exFull.master = IyakuhinMasterData.calonal;
        PrescExampleDTO ex = new PrescExampleDTO();
        ex.prescExampleId = 1;
        ex.iyakuhincode = exFull.master.iyakuhincode;
        ex.amount = "3";
        ex.usage = "分３　毎食後";
        ex.category = DrugCategory.Naifuku.getCode();
        ex.days = 5;
        ex.masterValidFrom = exFull.master.validFrom;
        ex.comment = "";
        exFull.prescExample = ex;
        state.setPrescExample(exFull);
        DrugInput input = createInput(state);
        DrugInputState state2 = new DrugInputState();
        input.getStateTo(state2);
        confirm(state2.equals(state));
    }
}
