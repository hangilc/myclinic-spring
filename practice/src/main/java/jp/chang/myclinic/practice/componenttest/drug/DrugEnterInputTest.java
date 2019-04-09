package jp.chang.myclinic.practice.componenttest.drug;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.mockdata.IyakuhinMasterData;
import jp.chang.myclinic.practice.componenttest.CompTest;
import jp.chang.myclinic.practice.componenttest.ComponentTestBase;
import jp.chang.myclinic.practice.javafx.drug.lib2.DrugEnterInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static jp.chang.myclinic.consts.DrugCategory.Gaiyou;
import static jp.chang.myclinic.consts.DrugCategory.Naifuku;

public class DrugEnterInputTest extends ComponentTestBase {

    public DrugEnterInputTest(Stage stage, StackPane main) {
        super(stage, main);
    }

    private DrugEnterInput createInput(){
        DrugEnterInput input = new DrugEnterInput();
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
    public void disp(){
        createInput();
    }

    @CompTest
    public void setMasterNaifuku(){
        DrugEnterInput input = createInput();
        IyakuhinMasterDTO master = IyakuhinMasterData.calonal;
        gui(() -> input.setMaster(master));
        waitForTrue(() -> input.getCategory() == Naifuku);
    }

    @CompTest
    public void setMasterGaiyou(){
        DrugEnterInput input = createInput();
        IyakuhinMasterDTO master = IyakuhinMasterData.loxoninPap;
        gui(() -> input.setMaster(master));
        waitForTrue(() -> input.getCategory() == Gaiyou);
    }
}
