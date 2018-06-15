package jp.chang.myclinic.recordbrowser;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.recordbrowser.tracking.Dispatcher;
import jp.chang.myclinic.recordbrowser.tracking.TrackingRoot;
import jp.chang.myclinic.recordbrowser.tracking.WebsocketClient;
import jp.chang.myclinic.utilfx.HandlerFX;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class Main extends Application {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    private TrackingRoot root = new TrackingRoot();
    private WebsocketClient websocketClient;
    private Dispatcher dispatcher;
    private Thread dispatcherThread;

    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }

    private void handleArgs(){
        List<String> args = getParameters().getUnnamed();
        if (args.size() != 1) {
            logger.error("Usage: mock-client server-url");
            System.exit(1);
        }
        String serverUrl = args.get(0);
        if (!serverUrl.endsWith("/")) {
            serverUrl = serverUrl + "/";
        }
        Service.setServerUrl(serverUrl);
        //Service.setLogBody();
        String wsUrl = serverUrl.replace("/json/", "/practice-log");
        websocketClient = new WebsocketClient(wsUrl);
    }

    @Override
    public void start(Stage stage) {
        handleArgs();
        stage.setTitle("診療録閲覧");
        BorderPane pane = new BorderPane();
        pane.setCenter(root);
        pane.setTop(createMenu());
        stage.setScene(new Scene(pane));
        this.dispatcher = new Dispatcher(root);
        this.dispatcherThread = new Thread(dispatcher);
        dispatcherThread.setDaemon(true);
        dispatcherThread.start();
        reload();
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        OkHttpClient client = Service.client;
        client.dispatcher().executorService().shutdown();
        client.connectionPool().evictAll();
        Cache cache = client.cache();
        websocketClient.cancel();
        websocketClient.shutdown();
        if (cache != null) {
            cache.close();
        }
    }

    private void reload(){
        String today = LocalDate.now().toString();
        Service.api.listAllPracticeLog(today)
                .thenAccept(logs -> {
                    logs.forEach(dispatcher::add);
                })
                .exceptionally(HandlerFX::exceptionally);
    }

    private MenuBar createMenu() {
        MenuBar mBar = new MenuBar();
        {
            Menu menu = new Menu("選択");
            {
//                MenuItem item = new MenuItem("本日の診察");
//                item.setOnAction(evt -> {
//                    root.setDate(LocalDate.now());
//                    root.trigger();
//                });
//                menu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("日付を選択");
                item.setOnAction(evt -> selectByDate());
                menu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("患者を選択");
                item.setOnAction(evt -> selectPatient());
                menu.getItems().add(item);
            }
            mBar.getMenus().add(menu);
        }
        {
            Menu menu = new Menu("その他");
            {
                MenuItem item = new MenuItem("会計一覧");
                item.setOnAction(evt -> doListCharge());
                menu.getItems().add(item);
            }
            mBar.getMenus().add(menu);
        }
        return mBar;
    }

    private void selectByDate() {
        SelectDateDialog dialog = new SelectDateDialog();
        dialog.showAndWait();
        dialog.getValue().ifPresent(date -> {
//            root.setDate(date);
//            root.trigger();
        });
    }

    private void selectPatient() {
        SelectPatientDialog dialog = new SelectPatientDialog();
        dialog.showAndWait();
        dialog.getSelectedPatient().ifPresent(patient -> {
            PatientHistoryDialog patientDialog = new PatientHistoryDialog(patient);
            patientDialog.show();
        });
    }

    private void doListCharge() {
        Service.api.listVisitChargePatientAt(LocalDate.now().toString())
                .thenAccept(result -> Platform.runLater(() -> {
                    ListChargeDialog dialog = new ListChargeDialog(result);
                    dialog.show();
                }))
                .exceptionally(HandlerFX::exceptionally);
    }

}
