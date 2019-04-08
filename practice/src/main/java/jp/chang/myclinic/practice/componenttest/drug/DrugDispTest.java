package jp.chang.myclinic.practice.componenttest.drug;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.DrugAttrDTO;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.mockdata.IyakuhinMasterData;
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

    private DrugFullDTO calonal(int visitId){
        DrugFullDTO calonal = new DrugFullDTO();
        calonal.master = IyakuhinMasterData.calonal;
        DrugDTO drug = new DrugDTO();
        drug.iyakuhincode = calonal.master.iyakuhincode;
        drug.visitId = visitId;
        drug.amount = 3.0;
        drug.usage = "分３　毎食後";
        drug.category = DrugCategory.Naifuku.getCode();
        drug.days = 5;
        drug.prescribed = 0;
        calonal.drug = drug;
        return calonal;
    }

    @CompTest
    public void disp(){
        int visitId = 1;
        DrugFullDTO drugFull = calonal(visitId);
        createDisp(1, drugFull, null);
    }

    @CompTest
    public void click(){
        int visitId = 1;
        DrugFullDTO drugFull = calonal(visitId);
        DrugDisp disp = createDisp(1, drugFull, null);
        class Local {
            private boolean confirmClick;
        }
        Local local = new Local();
        disp.setOnClickHandler(() -> local.confirmClick = true);
        gui(() -> disp.fireEvent(createMouseClickedEvent(disp)));
        waitForTrue(() -> local.confirmClick);
    }
}
