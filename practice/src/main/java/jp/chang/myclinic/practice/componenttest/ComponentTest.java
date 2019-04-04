package jp.chang.myclinic.practice.componenttest;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.practice.PracticeConfigServiceMock;
import jp.chang.myclinic.practice.testgui.TestEnv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComponentTest implements Runnable {

    private Stage stage;
    private StackPane main;

    public ComponentTest(Stage stage) {
        this.stage = stage;
        this.main = new StackPane();
        main.setStyle("-fx-padding: 10");
        main.getStylesheets().add("css/Practice.css");
        stage.setScene(new Scene(main));
        stage.show();
    }

    @Override
    public void run() {
        System.out.println("component test");
    }
}
