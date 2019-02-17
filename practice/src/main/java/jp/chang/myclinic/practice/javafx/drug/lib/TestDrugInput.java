package jp.chang.myclinic.practice.javafx.drug.lib;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.practice.testgui.TestBase;

import java.util.Arrays;

import static java.util.stream.Collectors.toList;

public class TestDrugInput extends TestBase {

    private Stage stage;
    private Pane mainPane;
    private DrugInputBaseState sampleBaseState = new DrugInputBaseState();

    {
        DrugInputBaseStateController controller = new DrugInputBaseStateController();
        sampleBaseState.setIyakuhincode(620000033);
        sampleBaseState.setDrugName("カロナール錠３００　３００ｍｇ");
        sampleBaseState.setAmount("3");
        sampleBaseState.setAmountUnit("錠");
        sampleBaseState.setUsage("分３　毎食後");
        sampleBaseState.setDays("5");
        sampleBaseState.setCategory(DrugCategory.Naifuku);
        controller.adaptToCategory(sampleBaseState);
    }

    public TestDrugInput(Stage stage, Pane mainPane) {
        this.stage = stage;
        this.mainPane = mainPane;
        this.stage.setTitle("薬剤入力のテスト");
    }

    public void run() {
        {
            InputBase inputBase = new InputBase();
            inputBase.setPrefWidth(323);
            mainPane.getChildren().setAll(inputBase);
            stage.sizeToScene();
            testInputBase(inputBase);
        }
        {
            DrugInput drugInput = new DrugInput();
            drugInput.setPrefWidth(323);
            mainPane.getChildren().setAll(drugInput);
            stage.sizeToScene();
            testDrugInput(drugInput);
        }
    }

    private void testInputBase(InputBase inputBase) {
        testInputBaseState(inputBase);
        testInputBaseCategory(inputBase);
        testInputBaseCategory(inputBase);
        testInputBaseExample(inputBase);
    }

    private DrugInputBaseState getInputBaseState(InputBase inputBase){
        DrugInputBaseState state = new DrugInputBaseState();
        inputBase.getStateTo(state);
        return state;
    }

    private void testInputBaseState(InputBase inputBase) {
        DrugInputBaseState state = sampleBaseState.copy();
        inputBase.setStateFrom(state);
        DrugInputBaseState outState = getInputBaseState(inputBase);
        confirm(outState.equals(state), "testState failed");
    }

    private void testInputBaseCategory(InputBase inputBase) {
        DrugInputBaseState state = sampleBaseState.copy();
        inputBase.setStateFrom(state);
        inputBase.simulateSelectCategory(DrugCategory.Tonpuku);
        DrugInputBaseState outState = getInputBaseState(inputBase);
        confirm(outState.getAmountLabel().equals("一回：") &&
                        outState.getDaysLabel().equals("回数：") &&
                        outState.getDaysUnit().equals("回分") &&
                        outState.isDaysVisible(),
                "Failed to adapt to category.");
        inputBase.simulateSelectCategory(DrugCategory.Gaiyou);
        outState = getInputBaseState(inputBase);
        confirm(outState.getAmountLabel().equals("用量：") &&
                        !outState.isDaysVisible(),
                "Failed to adapt to category.");
        inputBase.simulateSelectCategory(DrugCategory.Naifuku);
        outState = getInputBaseState(inputBase);
        confirm(outState.getAmountLabel().equals("用量：") &&
                        outState.getDaysLabel().equals("日数：") &&
                        outState.getDaysUnit().equals("日分") &&
                        outState.isDaysVisible(),
                "Failed to adapt to category.");
    }

    private void testInputBaseExample(InputBase inputBase) {
        inputBase.simulateClickExample();
        ContextMenu cmenu = null;
        for (Window window : Window.getWindows()) {
            if (window instanceof ContextMenu) {
                cmenu = (ContextMenu) window;
                break;
            }
        }
        confirm(cmenu != null, "Failed to invoke drug example.");
        String[] foundItems = cmenu.getItems().stream()
                .map(MenuItem::getText).collect(toList()).toArray(new String[]{});
        confirm(Arrays.equals(foundItems, inputBase.getExampleTexts()),
                "inconsistent example context menu");
        cmenu.hide();
        DrugInputBaseState state = new DrugInputBaseState();
        for (int i = 0; i < foundItems.length; i++) {
            inputBase.simulateClickExample();
            cmenu = null;
            for (Window window : Window.getWindows()) {
                if (window instanceof ContextMenu) {
                    cmenu = (ContextMenu) window;
                    break;
                }
            }
            confirm(cmenu != null, "Failed to invoke drug example.");
            cmenu.getItems().get(i).fire();
            state = getInputBaseState(inputBase);
            confirm(state.getUsage().equals(foundItems[i]), "example context menu failed");
            cmenu.hide();
        }
    }

    private void testDrugInput(DrugInput drugInput) {
        {
            DrugInputState state = new DrugInputState(sampleBaseState);
            drugInput.setStateFrom(state);
            stage.sizeToScene();
            DrugInputState outState = new DrugInputState();
            drugInput.getStateTo(outState);
            confirm(outState.equals(state), "save and get state failed", () -> {
                System.out.println(state);
                System.out.println(outState);
            });
        }
        {
            DrugInputState state = new DrugInputState(sampleBaseState);
            state.setComment("コメント");
            DrugInputStateController controller = new DrugInputStateController();
            controller.adapt(state);
            drugInput.setStateFrom(state);
            stage.sizeToScene();
            DrugInputState outState = new DrugInputState();
            drugInput.getStateTo(outState);
            confirm(outState.isCommentVisible() && !outState.isTekiyouVisible(), "visible comment failed");
        }
        {
            DrugInputState state = new DrugInputState(sampleBaseState);
            state.setTekiyou("２８日分");
            DrugInputStateController controller = new DrugInputStateController();
            controller.adapt(state);
            drugInput.setStateFrom(state);
            stage.sizeToScene();
            DrugInputState outState = new DrugInputState();
            drugInput.getStateTo(outState);
            confirm(outState.isTekiyouVisible() && !outState.isCommentVisible(), "visible tekiyou failed");
        }
        {
            DrugInputState state = new DrugInputState(sampleBaseState);
            state.setComment("コメント");
            state.setTekiyou("２８日分");
            DrugInputStateController controller = new DrugInputStateController();
            controller.adapt(state);
            drugInput.setStateFrom(state);
            stage.sizeToScene();
            DrugInputState outState = new DrugInputState();
            drugInput.getStateTo(outState);
            confirm(outState.isTekiyouVisible() && outState.isCommentVisible(),
                    "visible comment and tekiyou failed");
        }
    }

}
