package jp.chang.myclinic.practice.componenttest.drug;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.dto.PrescExampleFullDTO;
import jp.chang.myclinic.mockdata.SampleData;
import jp.chang.myclinic.practice.componenttest.CompTest;
import jp.chang.myclinic.practice.componenttest.ComponentTestBase;
import jp.chang.myclinic.practice.javafx.drug.lib2.DrugEnterInput;

import static jp.chang.myclinic.consts.DrugCategory.*;

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
        IyakuhinMasterDTO master = SampleData.calonal;
        gui(() -> input.setMaster(master));
        waitForTrue(() -> input.getCategory() == Naifuku);
    }

    @CompTest
    public void setMasterGaiyou(){
        DrugEnterInput input = createInput();
        IyakuhinMasterDTO master = SampleData.loxoninPap;
        gui(() -> input.setMaster(master));
        waitForTrue(() -> input.getCategory() == Gaiyou);
    }

    @CompTest
    public void setPrescExampleNaifuku(){
        DrugEnterInput input = createInput();
        PrescExampleFullDTO data = SampleData.calonalPrescExampleFull;
        gui(() -> input.setPrescExample(data));
        waitForTrue(() -> input.getCategory() == Naifuku);
    }

    @CompTest
    public void setPrescExampleTonpuku(){
        DrugEnterInput input = createInput();
        PrescExampleFullDTO data = SampleData.loxoninTonpukuPrescExampleFull;
        gui(() -> input.setPrescExample(data));
        waitForTrue(() -> input.getCategory() == Tonpuku);
    }

    @CompTest
    public void setPrescExampleGaiyou(){
        DrugEnterInput input = createInput();
        PrescExampleFullDTO data = SampleData.loxoninGaiyouPrescExampleFull;
        gui(() -> input.setPrescExample(data));
        waitForTrue(() -> input.getCategory() == Gaiyou);
    }

    @CompTest
    public void setDrugNaifuku(){
        DrugEnterInput input = createInput();
        DrugFullDTO data = SampleData.calonalDrugFull;
        gui(() -> input.setDrug(data));
        waitForTrue(() -> input.getCategory() == Naifuku);
    }

    @CompTest
    public void setDrugTonpuku(){
        DrugEnterInput input = createInput();
        DrugFullDTO data = SampleData.loxoninTonpukuDrugFull;
        gui(() -> input.setDrug(data));
        waitForTrue(() -> input.getCategory() == Tonpuku);
    }

    @CompTest
    public void setDrugGaiyou(){
        DrugEnterInput input = createInput();
        DrugFullDTO data = SampleData.loxoninGaiyouDrugFull;
        gui(() -> input.setDrug(data));
        waitForTrue(() -> input.getCategory() == Gaiyou);
    }
}
