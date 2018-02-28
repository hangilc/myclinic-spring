package jp.chang.myclinic.practice;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jp.chang.myclinic.practice.javafx.MainPane;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main extends Application {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        CommandArgs commandArgs = new CommandArgs(args);
        Service.setServerUrl(commandArgs.getServerUrl());
        PracticeEnv.INSTANCE = new PracticeEnv(commandArgs);
        Application.launch(Main.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("診療");
        MainPane root = new MainPane();
        root.getStylesheets().addAll(
                "css/Practice.css"
        );
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        OkHttpClient client = Service.client;
        client.dispatcher().executorService().shutdown();
        client.connectionPool().evictAll();
        Cache cache = client.cache();
        if( cache != null ){
            cache.close();
        }
    }

}
