package jp.chang.myclinic.reception;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jp.chang.myclinic.reception.javafx.MainPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args){
        Application.launch(Main.class, args);
    }

    @Override
    public void start(Stage primaryStage) {
        MainPane mainPane = new MainPane();
        mainPane.setPadding(new Insets(10, 10, 10, 10));
        Scene scene = new Scene(mainPane, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
