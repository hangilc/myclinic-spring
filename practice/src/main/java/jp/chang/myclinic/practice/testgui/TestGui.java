package jp.chang.myclinic.practice.testgui;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.practice.javafx.TestText;
import jp.chang.myclinic.practice.javafx.drug.lib.TestDrugInput;

public class TestGui extends GuiTestBase {

    private Stage stage;
    private StackPane main = new StackPane();

    private void setupTests(){
        addTestGroup("text", new TestText(stage, main));
        //addTestProc("drug", () -> new TestDrugInput(stage, main).run());
    }

    public TestGui(Stage stage) {
        this.stage = stage;
        main.setStyle("-fx-padding: 10");
        main.getStylesheets().add("css/Practice.css");
        stage.setScene(new Scene(main));
        stage.show();
        setupTests();
    }

    @Override
    public void runTest(String test){
        try {
            new Thread(() -> TestGui.super.runTest(test)).start();
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

}
