package jp.chang.myclinic.practice.testgui;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugEnterInput;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugInputBaseState;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugInputBaseStateController;

class TestDrugInput extends TestBase {

    private Stage stage;
    private Pane mainPane;
    private DrugEnterInput input;
    private DrugInputBaseStateController controller = new DrugInputBaseStateController();
    private DrugInputBaseState sampleBaseState = new DrugInputBaseState();
    {
        sampleBaseState.setIyakuhincode(620000033);
        sampleBaseState.setDrugName("カロナール錠３００　３００ｍｇ");
        sampleBaseState.setAmount("3");
        sampleBaseState.setAmountUnit("錠");
        sampleBaseState.setUsage("分３　毎食後");
        sampleBaseState.setDays("5");
        sampleBaseState.setCategory(DrugCategory.Naifuku);
        controller.adaptToCategory(sampleBaseState);
    }

    TestDrugInput(Stage stage, Pane mainPane){
        this.stage = stage;
        this.mainPane = mainPane;
        this.input = new DrugEnterInput();
        input.setPrefWidth(323);
        this.mainPane.getChildren().clear();
        this.mainPane.getChildren().add(input);
        this.stage.sizeToScene();
        this.stage.setTitle("薬剤入力のテスト");
    }

    void run(){
        testState();
        testCategory();
        testExample();
    }

    private void testState(){
        DrugInputBaseState state = new DrugInputBaseState();
        sampleBaseState.copy(state);
        input.setState(state);
        DrugInputBaseState outState = new DrugInputBaseState();
        input.getState(outState);
        confirm(outState.equals(state), "testState failed");
    }

    private void testCategory(){
        DrugInputBaseState state = new DrugInputBaseState();
        sampleBaseState.copy(state);
        input.setState(state);
        input.simulateSelectCategory(DrugCategory.Tonpuku);
        DrugInputBaseState outState = new DrugInputBaseState();
        input.getState(outState);
        confirm(outState.getAmountLabel().equals("一回：") &&
                outState.getDaysLabel().equals("回数：") &&
                outState.getDaysUnit().equals("回分") &&
                outState.isDaysVisible(),
                "Failed to adapt to category.");
        input.simulateSelectCategory(DrugCategory.Gaiyou);
        input.getState(outState);
        confirm(outState.getAmountLabel().equals("用量：") &&
                        !outState.isDaysVisible(),
                "Failed to adapt to category.");
        input.simulateSelectCategory(DrugCategory.Naifuku);
        input.getState(outState);
        confirm(outState.getAmountLabel().equals("用量：") &&
                        outState.getDaysLabel().equals("日数：") &&
                        outState.getDaysUnit().equals("日分") &&
                        outState.isDaysVisible(),
                "Failed to adapt to category.");
    }

    private void testExample(){
        stage.toFront();
        input.simulateClickExample();
        Window.getWindows().forEach(w -> System.out.println(w.getClass()));
    }

}
