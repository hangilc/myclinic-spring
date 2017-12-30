package jp.chang.myclinic.reception.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args){
        Application.launch(Main.class, args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainScene.fxml"));
            Parent root = loader.load();
            MainSceneController controller = loader.getController();
            Scene scene = new Scene(root);
            primaryStage.setTitle("受付");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception ex){
            logger.error("failed to start hotline", ex);
            System.exit(1);
        }
    }
}
