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
import jp.chang.myclinic.utilfx.HandlerFX;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

// TODO: put charge in lower place
// TODO: add refresh button
// TODO: automatic syncing
public class Main extends Application {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    private MainRoot root = new MainRoot();
    private Repeater repeater;

    public static void main(String[] args) {
        if (args.length != 1) {
            logger.error("Usage: mock-client server-url");
            System.exit(1);
        }
        String serverUrl = args[0];
        if (!serverUrl.endsWith("/")) {
            serverUrl = args[0] + "/";
        }
        Service.setServerUrl(serverUrl);
        //Service.setLogBody();
        Application.launch(Main.class, args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("診療録閲覧");
        BorderPane pane = new BorderPane();
        pane.setCenter(root);
        pane.setTop(createMenu());
        stage.setScene(new Scene(pane));
        startRepeater();
        stage.show();
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

    private MenuBar createMenu() {
        MenuBar mBar = new MenuBar();
        {
            Menu menu = new Menu("選択");
            {
                MenuItem item = new MenuItem("本日の診察");
                item.setOnAction(evt -> {
                    root.setDate(LocalDate.now());
                    root.trigger();
                });
                menu.getItems().add(item);
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
            root.setDate(date);
            root.trigger();
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

    private void startRepeater(){
        repeater = new Repeater(10, () -> {
            root.trigger();
        });
        root.setOnRefreshCallback(() -> repeater.skip());
        Thread thread = new Thread(repeater);
        thread.setDaemon(true);
        thread.start();
    }

}
