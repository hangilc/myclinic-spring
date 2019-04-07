package jp.chang.myclinic.practice.componenttest.drug;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.DrugAttrDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.practice.componenttest.CompTest;
import jp.chang.myclinic.practice.componenttest.ComponentTestBase;
import jp.chang.myclinic.practice.javafx.RecordHoken;
import jp.chang.myclinic.practice.javafx.drug.DrugDisp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrugDispTest extends ComponentTestBase {

    public DrugDispTest(Stage stage, StackPane main) {
        super(stage, main);
    }

    private DrugDisp createDisp(int index, DrugFullDTO drug, DrugAttrDTO attr){
        DrugDisp disp = new DrugDisp(index, drug, attr);
        gui(() -> {
            disp.setPrefHeight(Region.USE_COMPUTED_SIZE);
            disp.setMaxHeight(Region.USE_PREF_SIZE);
            main.setPrefWidth(329);
            main.setPrefHeight(300);
            main.getChildren().setAll(disp);
            stage.sizeToScene();
        });
        return disp;
    }

    @CompTest
    public void disp(){
        DrugFullDTO drugFull = new DrugFullDTO();

    }
}
