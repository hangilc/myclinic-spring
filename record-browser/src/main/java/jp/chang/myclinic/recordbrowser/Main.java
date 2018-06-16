package jp.chang.myclinic.recordbrowser;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import jp.chang.myclinic.recordbrowser.tracking.Dispatcher;
import jp.chang.myclinic.recordbrowser.tracking.TrackingRoot;
import jp.chang.myclinic.recordbrowser.tracking.WebsocketClient;
import jp.chang.myclinic.utilfx.HandlerFX;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main extends Application {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    private static Stage mainStage;
    private static List<Stage> dependentStages = new ArrayList<>();
    public static void setAsChildWindow(Stage stage){
        stage.showingProperty().addListener((obs, oldValue, newValue) -> {
            if( oldValue && !newValue ){
                dependentStages.remove(stage);
            }
        });
        dependentStages.add(stage);
    }
    public static double getXofMainStage(){
        return mainStage.getX();
    }
    public static double getYofMainStage(){
        return mainStage.getY();
    }
    private TrackingRoot root = new TrackingRoot();
    private WebsocketClient websocketClient;
    private Dispatcher dispatcher;
    private ObjectMapper mapper = new ObjectMapper();
    private String wsUrl;
    private ScheduledExecutorService timerExecutor = Executors.newSingleThreadScheduledExecutor();

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
        wsUrl = serverUrl.replace("/json/", "/practice-log");
    }

    @Override
    public void start(Stage stage) {
        stage.showingProperty().addListener((obs, oldValue, newValue) -> {
            if( oldValue && !newValue ) {
                List<Stage> stages = new ArrayList<>(dependentStages);
                dependentStages.clear();
                stages.forEach(Stage::close);
            }
        });
        handleArgs();
        stage.setTitle("診療録閲覧");
        BorderPane pane = new BorderPane();
        pane.setCenter(root);
        pane.setTop(createMenu());
        stage.setScene(new Scene(pane));
        this.dispatcher = new Dispatcher(root);
        Thread dispatcherThread = new Thread(dispatcher);
        dispatcherThread.setDaemon(true);
        dispatcherThread.start();
        startWebSocket();
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        timerExecutor.shutdown();
        OkHttpClient client = Service.client;
        client.dispatcher().executorService().shutdown();
        client.connectionPool().evictAll();
        Cache cache = client.cache();
        websocketClient.shutdown();
        if (cache != null) {
            cache.close();
        }
    }

    private void reload(Runnable cb){
        String today = LocalDate.now().toString();
        Service.api.listAllPracticeLog(today)
                .thenAccept(logs -> {
                    if( logs.size() > 0 ) {
                        int lastId = logs.get(logs.size() - 1).serialId;
                        dispatcher.addAll(logs);
                        probeLogUpdate(3, lastId);
                    }
                    cb.run();
                })
                .exceptionally(HandlerFX::exceptionally);
    }

    private void probeLogUpdate(int delaySeconds, int lastSerialId){
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(delaySeconds * 1000);
                Service.api.listAllPracticeLog(LocalDate.now().toString(), lastSerialId)
                        .thenAccept(logs -> {
                            dispatcher.addAll(logs);
                        })
                        .exceptionally(HandlerFX::exceptionally);
            } catch (InterruptedException e) {
                logger.error("probeLogUpdate failed.", e);
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void startWebSocket(){
        if( websocketClient != null ){
            websocketClient.cancel();
        }
        websocketClient = new WebsocketClient(wsUrl){
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                webSocket.send("hello");
            }

            @Override
            protected void onNewMessage(String text){
                try {
                    PracticeLogDTO plog = mapper.readValue(text, PracticeLogDTO.class);
                    if( dispatcher != null ){
                        dispatcher.add(plog);
                    }
                } catch (IOException e) {
                    logger.error("Cannot parse practice log.", e);
                }
            }

            @Override
            protected void onFail() {
                logger.info("Web socket failed. Scheduling reconnect at 5 seconds later.");
                timerExecutor.schedule(() -> {
                    startWebSocket();
                }, 5, TimeUnit.SECONDS);
            }
        };
        logger.info("Started web socket");
    }

    private MenuBar createMenu() {
        MenuBar mBar = new MenuBar();
        {
            Menu menu = new Menu("選択");
            {
                MenuItem item = new MenuItem("日付を選択");
                item.setOnAction(evt -> selectByDate());
                menu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("患者検索");
                item.setOnAction(evt -> selectPatient());
                menu.getItems().add(item);
            }
            mBar.getMenus().add(menu);
        }
        {
            Menu menu = new Menu("その他");
            {
                MenuItem item = new MenuItem("病名検索");
                item.setOnAction(evt -> doSearchByoumei());
                menu.getItems().add(item);
            }
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
        Main.setAsChildWindow(dialog);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
        dialog.getValue().ifPresent(date -> {
//            root.setDate(date);
//            root.trigger();
        });
    }

    private void selectPatient() {
        SelectPatientDialog dialog = new SelectPatientDialog();
        Main.setAsChildWindow(dialog);
        dialog.show();
    }

    private void doListCharge() {
        Service.api.listVisitChargePatientAt(LocalDate.now().toString())
                .thenAccept(result -> Platform.runLater(() -> {
                    ListChargeDialog dialog = new ListChargeDialog(result);
                    Main.setAsChildWindow(dialog);
                    dialog.show();
                }))
                .exceptionally(HandlerFX::exceptionally);
    }

    private void doSearchByoumei(){
        SearchByoumeiDialog dialog = new SearchByoumeiDialog();
        Main.setAsChildWindow(dialog);
        dialog.show();
    }

}
