package jp.chang.myclinic.rcptdrawer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main( String[] args )
    {
        Application.launch(Main.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("レセプト印刷");
        MainRoot root = new MainRoot();
        stage.setScene(new Scene(root));
        stage.show();
    }

}

