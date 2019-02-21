package jp.chang.myclinic.practice.testgui;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.practice.javafx.drug.lib.TestDrugInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TestGui {

    private static Logger logger = LoggerFactory.getLogger(TestGui.class);
    private Stage stage;
    private Pane mainPane;

    public TestGui(Stage stage) {
        this.stage = stage;
    }

    public void run(List<String> args){
        StackPane main = new StackPane();
        main.setStyle("-fx-padding: 10");
        main.getStylesheets().add("css/Practice.css");
        stage.setScene(new Scene(main));
        stage.show();
        try {
            new TestDrugInput(stage, main).run();
            System.out.println("Gui test done.");
            //stage.close();
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

}
