package jp.chang.myclinic.practice.componenttest.shinryou;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.ShinryouAttrDTO;
import jp.chang.myclinic.practice.componenttest.CompTest;
import jp.chang.myclinic.practice.componenttest.ComponentTestBase;
import jp.chang.myclinic.practice.javafx.drug.DrugDisp;
import jp.chang.myclinic.practice.javafx.shinryou.ShinryouDisp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShinryouDispTest extends ComponentTestBase {

    public ShinryouDispTest(Stage stage, StackPane main) {
        super(stage, main);
    }

    private ShinryouDisp createDisp(String masterName, ShinryouAttrDTO attr){
        ShinryouDisp disp = new ShinryouDisp(masterName, attr);
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
        createDisp("初診", null);
    }

    @CompTest
    public void click(){
        class Local {
            private boolean confirmClick;
        }
        Local local = new Local();
        ShinryouDisp disp = createDisp("初診", null);
        disp.setOnClickedHandler(() -> local.confirmClick = true);
        gui(() -> click(disp));
        waitForTrue(() -> local.confirmClick);
    }
}
