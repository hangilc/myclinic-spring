package jp.chang.myclinic.practice.testgui;

import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugEnterInput;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugInputBaseState;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugInputBaseStateController;

import java.util.Arrays;

import static java.util.stream.Collectors.toList;

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
        input.simulateClickExample();
        ContextMenu cmenu = null;
        for(Window window: Window.getWindows()){
            if( window instanceof  ContextMenu ){
                cmenu = (ContextMenu)window;
                break;
            }
        }
        confirm( cmenu != null, "Failed to invoke drug example.");
        String[] foundItems = cmenu.getItems().stream()
                .map(MenuItem::getText).collect(toList()).toArray(new String[]{});
        confirm(Arrays.equals(foundItems, input.getExampleTexts()),
                "inconsistent example context menu");
        cmenu.hide();
        DrugInputBaseState state = new DrugInputBaseState();
        for(int i=0;i<foundItems.length;i++){
            input.simulateClickExample();
            cmenu = null;
            for(Window window: Window.getWindows()){
                if( window instanceof  ContextMenu ){
                    cmenu = (ContextMenu)window;
                    break;
                }
            }
            confirm( cmenu != null, "Failed to invoke drug example.");
            cmenu.getItems().get(i).fire();
            input.getState(state);
            confirm(state.getUsage().equals(foundItems[i]), "example context menu failed");
            cmenu.hide();
        }

    }

}
