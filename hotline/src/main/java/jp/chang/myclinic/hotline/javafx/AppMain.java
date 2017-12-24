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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AppMain extends Application {
    private static Logger logger = LoggerFactory.getLogger(AppMain.class);

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
        try {
            User sender = resolveUser(args.get(1));
            User recipient = resolveUser(args.get(2));
            Context.INSTANCE = new Context(sender, recipient);
        } catch(Exception ex){
            logger.error("invalid user", ex);
        }
    }

    @Override
    public void start(Stage primaryStage)  {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainScene.fxml"));
            Parent root = loader.load();
            MainSceneController controller = (MainSceneController)loader.getController();
            PeriodicFetcher fetcher = new PeriodicFetcher(hotlines -> {
                Platform.runLater(() -> {
                    System.out.println(hotlines);
                });
            }, error -> {
                logger.error(error);
            });
            Thread fetcherThread = new Thread(fetcher);
            fetcherThread.setDaemon(true);
            fetcherThread.start();
            Scene scene = new Scene(root);
            primaryStage.setTitle("Hotline");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception ex){
            logger.error("failed to start hotline", ex);
            System.exit(1);
        }
    }

    private static User resolveUser(String arg) throws IllegalArgumentException {
        for(User user: User.values()){
            if( user.getName().equalsIgnoreCase(arg) ){
                return user;
            }
        }
        throw new IllegalArgumentException("invalid user name: " + arg);
    }

}
