package jp.chang.myclinic.recordbrowser;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jp.chang.myclinic.client.Service;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public void start(Stage stage) {
        stage.setTitle("診療録閲覧");
        MainRoot root = new MainRoot();
        BorderPane pane = new BorderPane();
        pane.setCenter(root);
        pane.setTop(createMenu(root));
        stage.setScene(new Scene(pane));
        stage.show();
        root.loadTodaysVisits();
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

    private MenuBar createMenu(MainRoot root){
        MenuBar mBar = new MenuBar();
        {
            Menu menu = new Menu("選択");
            {
                MenuItem item = new MenuItem("本日の診察");
                item.setOnAction(evt -> root.loadTodaysVisits());
                menu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("日付を選択");
                item.setOnAction(evt -> selectByDate(root));
                menu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("患者を選択");
                item.setOnAction(evt -> selectPatient(root));
                menu.getItems().add(item);
            }
            mBar.getMenus().add(menu);
        }
        return mBar;
    }

    private void selectByDate(MainRoot root){
        SelectDateDialog dialog = new SelectDateDialog();
        dialog.showAndWait();
        dialog.getValue().ifPresent(root::loadVisitsAt);
    }

    private void selectPatient(MainRoot root){
        SelectPatientDialog dialog = new SelectPatientDialog();
        dialog.showAndWait();
        dialog.getSelectedPatient().ifPresent(root::loadVisitsOfPatient);
    }

}

