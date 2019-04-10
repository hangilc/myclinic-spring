package jp.chang.myclinic.practice.componenttest.drug;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.DrugAttrDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.mockdata.SampleData;
import jp.chang.myclinic.practice.componenttest.CompTest;
import jp.chang.myclinic.practice.componenttest.ComponentTestBase;
import jp.chang.myclinic.practice.javafx.drug.lib2.DrugEditInput;

public class DrugEditInputTest extends ComponentTestBase {

    public DrugEditInputTest(Stage stage, StackPane main) {
        super(stage, main);
    }

    private DrugEditInput createInput(DrugFullDTO drugFull, DrugAttrDTO attr){
        DrugEditInput input = new DrugEditInput(drugFull, attr);
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
        createInput(SampleData.calonalDrugFull, null);
    }

    @CompTest
    public void dispTekiyou(){
        DrugAttrDTO attr = new DrugAttrDTO();
        attr.tekiyou = "摘要のテスト";
        createInput(SampleData.calonalDrugFull, attr);
    }

}
