package jp.chang.myclinic.pharma;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jp.chang.myclinic.pharma.javafx.MainScene;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Main extends Application {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("pharma invoked");
        Application.launch(Main.class, args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        {
            Config config = Config.load();
            System.out.println("Config: " + config);
        }
        List<String> args = getParameters().getUnnamed();
        if( args.size() == 1 ){
            String serverUrl = args.get(0);
            if( !serverUrl.endsWith("/") ){
                serverUrl = serverUrl + "/";
            }
            Service.setServerUrl(serverUrl);
        } else {
            logger.error("Usage: pharma service-url");
            System.exit(1);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("薬局");
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createMenu());
        MainScene root = new MainScene();
        root.getStylesheets().add("Pharma.css");
        borderPane.setCenter(root);
        stage.setScene(new Scene(borderPane));
        stage.show();
    }

    private Node createMenu(){
        MenuBar mbar = new MenuBar();
        {
            Menu menu = new Menu("アクション");
            {
                MenuItem item = new MenuItem("過去のお薬手帳");
                menu.getItems().add(item);
            }
            mbar.getMenus().add(menu);
        }
        {
            Menu menu = new Menu("薬剤情報");
            mbar.getMenus().add(menu);
        }
        {
            Menu menu = new Menu("設定");
            mbar.getMenus().add(menu);
        }
        {
            Menu menu = new Menu("印刷");
            mbar.getMenus().add(menu);
        }
        return mbar;
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
        logger.info("pharma stopped.");
    }

}
