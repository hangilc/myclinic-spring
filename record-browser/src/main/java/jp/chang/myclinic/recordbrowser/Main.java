package jp.chang.myclinic.recordbrowser;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
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
        mainStage = stage;
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
        TrackingRoot root = new TrackingRoot() {
            @Override
            protected void onRefreshRequest() {
                websocketClient.sendMessage("hello");
            }
        };
        StackPane centerStackPane = new StackPane(root);
        pane.setCenter(centerStackPane);
        pane.setTop(createMenu());
        stage.setScene(new Scene(pane));
        StackPane curtain = new StackPane(new Label("同期中"));
        curtain.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        this.dispatcher = new Dispatcher(root){
            @Override
            protected void beforeCatchup() {
                //Platform.runLater(() -> centerStackPane.getChildren().add(curtain));
            }

            @Override
            protected void afterCatchup() {
                //Platform.runLater(() -> centerStackPane.getChildren().remove(curtain));
            }
        };
        Thread dispatcherThread = new Thread(dispatcher);
        dispatcherThread.setDaemon(true);
        dispatcherThread.start();
        startWebSocket();
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        timerExecutor.shutdownNow();
        OkHttpClient client = Service.client;
        client.dispatcher().executorService().shutdown();
        client.connectionPool().evictAll();
        Cache cache = client.cache();
        if (cache != null) {
            cache.close();
        }
        websocketClient.shutdown();
    }

    private void startWebSocket(){
        websocketClient = new WebsocketClient(wsUrl, timerExecutor){
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
            ByDateDialog byDateDialog = new ByDateDialog(date);
            setAsChildWindow(byDateDialog);
            byDateDialog.setX(getXofMainStage() + 40);
            byDateDialog.setY(getYofMainStage() + 20);
            byDateDialog.show();
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
