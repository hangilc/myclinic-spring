package jp.chang.myclinic.practice.componenttest.shinryou;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.mockdata.SampleShinryouMaster;
import jp.chang.myclinic.practice.componenttest.CompTest;
import jp.chang.myclinic.practice.componenttest.ComponentTestBase;
import jp.chang.myclinic.practice.javafx.shinryou.ShinryouInput;

public class ShinryouInputTest extends ComponentTestBase {

    public ShinryouInputTest(Stage stage, StackPane main) {
        super(stage, main);
    }

    private ShinryouInput createInput(){
        ShinryouInput input = new ShinryouInput();
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
        ShinryouInput input = createInput();
        gui(() -> {
            input.setMaster(SampleShinryouMaster.初診);
            input.setTekiyou("摘要のテスト");
        });
    }
}
