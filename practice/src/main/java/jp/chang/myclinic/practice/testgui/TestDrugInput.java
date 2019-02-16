package jp.chang.myclinic.practice.testgui;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugEnterInput;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugInputBaseState;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugInputBaseStateController;

class TestDrugInput extends TestBase {

    private Stage stage;
    private Pane mainPane;
    DrugEnterInput input;

    TestDrugInput(Stage stage, Pane mainPane){
        this.stage = stage;
        this.mainPane = mainPane;
        this.input = new DrugEnterInput();
        input.setPrefWidth(323);
        mainPane.getChildren().clear();
        mainPane.getChildren().add(input);
        stage.sizeToScene();
        stage.setTitle("薬剤入力のテスト");
    }

    void run(){
        testState();
    }

    private void testState(){
        DrugInputBaseState baseState = new DrugInputBaseState();
        baseState.setIyakuhincode(620000033);
        baseState.setDrugName("カロナール錠３００　３００ｍｇ");
        baseState.setAmount("3");
        baseState.setAmountUnit("錠");
        baseState.setUsage("分３　毎食後");
        baseState.setDays("5");
        baseState.setCategory(DrugCategory.Naifuku);
        DrugInputBaseStateController controller = new DrugInputBaseStateController();
        controller.adaptToCategory(baseState);
        input.setState(baseState);
        DrugInputBaseState outState = new DrugInputBaseState();
        input.getState(outState);
        confirm(outState.equals(baseState), "testState failed");
    }

}
