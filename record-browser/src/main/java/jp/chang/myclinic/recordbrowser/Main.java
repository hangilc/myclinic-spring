package jp.chang.myclinic.recordbrowser;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jp.chang.myclinic.client.Service;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class Main extends Application {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main( String[] args )
    {
        if( args.length != 1 ){
            logger.error("Usage: mock-client server-url");
            System.exit(1);
        }
        String serverUrl = args[0];
        if( !serverUrl.endsWith("/") ){
            serverUrl = args[0] + "/";
        }
        Service.setServerUrl(serverUrl);
        Application.launch(Main.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        MainRoot root = new MainRoot();
        stage.setScene(new Scene(root));
        stage.show();
        //root.loadTodaysVisits();
        root.loadVisitsAt(LocalDate.of(2018, 3, 23));
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        OkHttpClient client = Service.client;
        client.dispatcher().executorService().shutdown();
        client.connectionPool().evictAll();
        Cache cache = client.cache();
        if (cache != null) {
            cache.close();
        }
    }

}

