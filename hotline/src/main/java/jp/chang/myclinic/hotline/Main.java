package jp.chang.myclinic.hotline;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.hotline.javafx.MainScene;
import jp.chang.myclinic.hotline.tracker.Tracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

//import jp.chang.myclinic.hotline.Service;

public class Main extends Application {
    private static Logger logger = LoggerFactory.getLogger(Main.class);
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
        String serverUrl;
        User sender;
        User recipient;
        if( args.size() == 2 ){
            serverUrl = System.getenv("MYCLINIC_SERVICE");
            sender = User.fromNameIgnoreCase(args.get(0));
            recipient = User.fromNameIgnoreCase(args.get(1));
        } else if( args.size() == 3 ){
            serverUrl = args.get(0);
            sender = User.fromNameIgnoreCase(args.get(1));
            recipient = User.fromNameIgnoreCase(args.get(2));
        } else {
            logger.error("Hotline aborting.");
            logger.error("Usage: [server-url] sender recipient");
            logger.error("ssender/receipient should be one of practice, pharmacy, or reception");
            throw new RuntimeException("Invalid invocation.");
        }
        if( serverUrl == null || serverUrl.isEmpty() ){
            logger.error("Cannot get server-url.");
            throw new RuntimeException("Cannot get server-url.");
        }
        if (!serverUrl.endsWith("/")) {
            serverUrl = serverUrl + "/";
        }
        Service.setServerUrl(serverUrl);
        this.wsUrl = serverUrl.replace("/json/", "/hotline");
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
        Scope scope = new Scope();
        MainScene root = new MainScene(scope);
        root.getStylesheets().add("Hotline.css");
        scope.setShowErrorHandler(root::showErrorMessage);
        scope.setHideErrorHandler(root::hideErrorMessage);
        scope.setResizeStageHandler(stage::sizeToScene);
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createMenu());
        borderPane.setCenter(root);
        stage.setScene(new Scene(borderPane));
        stage.show();
        this.tracker = new Tracker(wsUrl, root, Service.api){
            @Override
            protected void onOpen() {
                scope.hideError();
            }

            @Override
            protected void beforeCatchup() {
                scope.setBeepEnabled(false);
            }

            @Override
            protected void afterCatchup() {
                scope.setBeepEnabled(true);
            }

            @Override
            protected void onError(String message){
                scope.showError(message);
            }
        };
        tracker.start();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Service.stop();
        tracker.shutdown();
        logger.info("Hotline stopped.");
    }

    private Node createMenu(){
        MenuBar mbar = new MenuBar();
        {
            Menu menu = new Menu("メニュー");
            {
                MenuItem item = new MenuItem("手動更新");
                item.setOnAction(evt -> doReload());
                menu.getItems().add(item);
            }
            mbar.getMenus().add(menu);
        }
        return mbar;
    }

    private void doReload(){
        tracker.reload();
    }
}
