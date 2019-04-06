package jp.chang.myclinic.practice.componenttest.hoken;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.mockdata.MockData;
import jp.chang.myclinic.practice.componenttest.CompTest;
import jp.chang.myclinic.practice.componenttest.ComponentTestBase;
import jp.chang.myclinic.practice.javafx.hoken.HokenDisp;
import jp.chang.myclinic.practice.javafx.text.TextDisp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HokenDispTest extends ComponentTestBase {

    public HokenDispTest(Stage stage, Pane main) {
        super(stage, main);
    }

    private HokenDisp createDisp(HokenDTO hoken){
        HokenDisp disp = new HokenDisp(hoken);
        gui(() -> {
            disp.setPrefWidth(329);
            disp.setPrefHeight(300);
            main.getChildren().setAll(disp);
            stage.sizeToScene();
        });
        return disp;
    }

    @CompTest
    public void testHokenDispDisp(){
        HokenDTO hoken = new HokenDTO();
        MockData mock = new MockData();
        hoken.shahokokuho = mock.pickShahokokuho(1);
        createDisp(hoken);
    }

    @CompTest
    public void testHokenDispEmpty(){
        HokenDisp disp = createDisp(new HokenDTO());
        confirm(disp.getDispText().equals("(保険なし)"));
    }

    @CompTest
    public void testHokenDispClick(){
        HokenDisp disp = createDisp(new HokenDTO());
        class Local {
            private boolean confirmClick;
        }
        Local local = new Local();
        disp.setOnClickHandler(() -> local.confirmClick = true);
        gui(() -> disp.fireEvent(createMouseClickedEvent(disp)));
        waitForTrue(() -> local.confirmClick);
    }


}
