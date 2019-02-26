package jp.chang.myclinic.practice.testgui;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.practice.javafx.TestText;
import jp.chang.myclinic.practice.javafx.shohousen.TestShohousen;

public class TestGui extends GuiTestBase {

    private Stage stage;
    private StackPane main = new StackPane();

    {
        addTestGroup("text", () -> new TestText(stage, main));
        addTestGroup("shohousen", TestShohousen::new);
    }

    public TestGui(Stage stage) {
        this.stage = stage;
        main.setStyle("-fx-padding: 10");
        main.getStylesheets().add("css/Practice.css");
        stage.setScene(new Scene(main));
        stage.show();
    }

    @Override
    public void runTest(String test){
        try {
            new Thread(() -> {
                TestGui.super.runTest(test);
                System.out.println("test done");
            }).start();
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

}
