package jp.chang.myclinic.hotline.javafx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jp.chang.myclinic.hotline.Context;
import jp.chang.myclinic.hotline.ResizeRequiredEvent;
import jp.chang.myclinic.hotline.Service;
import jp.chang.myclinic.hotline.User;
import jp.chang.myclinic.hotline.lib.PeriodicFetcher;
import jp.chang.myclinic.hotline.tracker.Tracker;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AppMain extends Application {
    private static Logger logger = LoggerFactory.getLogger(AppMain.class);
    private String wsUrl;
    private Tracker tracker;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        logger.info("Hotline invoked.");
        List<String> args = getParameters().getUnnamed();
        if (args.size() != 3) {
            logger.error("Hotline aborting.");
            logger.error("Usage: server-url sender recipient");
            logger.error("ssender/receipient should be one of practice, pharmacy, or reception");
            System.exit(1);
        }
        String serverUrl = args.get(0);
        if (!serverUrl.endsWith("/")) {
            serverUrl = serverUrl + "/";
        }
        Service.setServerUrl(serverUrl);
        this.wsUrl = serverUrl.replace("/json/", "/hotline");
        User sender = User.fromNameIgnoreCase(args.get(1));
        User recipient = User.fromNameIgnoreCase(args.get(2));
        if (sender == null) {
            logger.error("invalid sender name");
            System.exit(1);
        }
        if (recipient == null) {
            logger.error("invalid recipient name");
            System.exit(1);
        }
        Context.INSTANCE = new Context(sender, recipient);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Hotline to " + Context.INSTANCE.getRecipient().getDispName());
        stage.addEventHandler(ResizeRequiredEvent.eventType, evt -> {
            stage.sizeToScene();
        });
        MainScene root = new MainScene();
        root.getStylesheets().add("Hotline.css");
        PeriodicFetcher fetcher = new PeriodicFetcher((hotlines, initialSetup) -> {
            Platform.runLater(() -> {
                root.addHotlinePosts(hotlines, initialSetup);
            });
        }, error -> {
            Platform.runLater(() -> {
                root.showErrorMessage(error);
            });
        });
        Context.INSTANCE.setPeriodicFetcher(fetcher);
        Thread fetcherThread = new Thread(fetcher);
        fetcherThread.setDaemon(true);
        fetcherThread.start();
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createMenu());
        borderPane.setCenter(root);
        stage.setScene(new Scene(borderPane));
        stage.show();
        this.tracker = new Tracker(wsUrl, root, Service.api);
        tracker.start();
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
        if (cache != null) {
            cache.close();
        }
        tracker.shutdown();
        logger.info("Hotline stopped.");
    }

    private Node createMenu(){
        MenuBar mbar = new MenuBar();
        {
            Menu menu = new Menu("メニュー");
            {
                MenuItem item = new MenuItem("再読み込み");
                item.setOnAction(evt -> doReload());
                menu.getItems().add(item);
            }
            mbar.getMenus().add(menu);
        }
        return mbar;
    }

    private void doReload(){
        Context.INSTANCE.getPeriodicFetcher().trigger(PeriodicFetcher.CMD_RESET);
    }
}
