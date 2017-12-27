package jp.chang.myclinic.hotline.javafx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jp.chang.myclinic.hotline.Context;
import jp.chang.myclinic.hotline.Service;
import jp.chang.myclinic.hotline.User;
import jp.chang.myclinic.hotline.lib.PeriodicFetcher;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AppMain extends Application {
    private static Logger logger = LoggerFactory.getLogger(AppMain.class);
    private Thread fetcherThread;
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        List<String> args = getParameters().getUnnamed();
        if( args.size() != 3 ){
            System.out.println("Usage: server-url sender recipient");
            System.out.println("ssender/receipient should be one of practice, pharmacy, or reception");
            System.exit(1);
        }
        String serverUrl = args.get(0);
        if (!serverUrl.endsWith("/")) {
            serverUrl = serverUrl + "/";
        }
        Service.setServerUrl(serverUrl);
        User sender = User.fromNameIgnoreCase(args.get(1));
        User recipient = User.fromNameIgnoreCase(args.get(2));
        if( sender == null ){
            logger.error("invalid sender name");
            System.exit(1);
        }
        if( recipient == null ){
            logger.error("invalid recipient name");
            System.exit(1);
        }
        Context.INSTANCE = new Context(sender, recipient);
    }

    @Override
    public void start(Stage primaryStage)  {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainScene.fxml"));
            Parent root = loader.load();
            MainSceneController controller = (MainSceneController)loader.getController();
            PeriodicFetcher fetcher = new PeriodicFetcher(hotlines -> {
                Platform.runLater(() -> {
                    controller.addHotlinePosts(hotlines);
                });
            }, error -> {
                logger.error(error);
            });
            Context.INSTANCE.setPeriodicFetcher(fetcher);
            Scene scene = new Scene(root);
            primaryStage.setTitle("Hotline");
            primaryStage.setScene(scene);
            primaryStage.show();
            fetcherThread = new Thread(fetcher);
            fetcherThread.setDaemon(true);
            fetcherThread.start();
        } catch(Exception ex){
            logger.error("failed to start hotline", ex);
            System.exit(1);
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
//        Set<Thread> threads = Thread.getAllStackTraces().keySet();
//        System.out.println(threads);
        OkHttpClient client = Service.client;
        client.dispatcher().executorService().shutdown();
        client.connectionPool().evictAll();
        Cache cache = client.cache();
        if( cache != null ){
            cache.close();
        }
    }
}
