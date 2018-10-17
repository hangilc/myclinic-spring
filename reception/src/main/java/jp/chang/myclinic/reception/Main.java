package jp.chang.myclinic.reception;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.reception.event.RefreshEvent;
import jp.chang.myclinic.reception.javafx.MainPane;
import jp.chang.myclinic.reception.javafx.WqueueDTOModel;
import jp.chang.myclinic.reception.javafx.WqueueModel;
import jp.chang.myclinic.reception.javafx.WqueueTable;
import jp.chang.myclinic.reception.tracker.Tracker;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

// TODO: check hokenshabangou etc
// TODO: add default koukikourei hokenshabangou
public class Main extends Application {

    private static Logger logger = LoggerFactory.getLogger(Main.class);
    private static String wsUrl;
    private Tracker tracker;
    private MainPane mainPane;

    public static void main(String[] args) {
        String serviceUrl;
        if( args.length == 0 ){
            serviceUrl = System.getenv("MYCLINIC_SERVICE");
        } else {
            serviceUrl = args[0];
        }
        if( serviceUrl.isEmpty() ){
            logger.error("Cannot find service url.");
            System.exit(1);
        }
        if( !serviceUrl.endsWith("/") ){
            serviceUrl += "/";
        }
        Service.setServerUrl(serviceUrl);
        wsUrl = serviceUrl.replace("/json/", "/practice-log");
        Service.api.getClinicInfo()
                .thenAccept(clinicInfo -> {
                    Globals.setClinicInfo(clinicInfo);
                    Application.launch(Main.class, args);
                })
                .exceptionally(ex -> {
                    logger.error("Failed to start reception.", ex);
                    System.exit(1);
                    return null;
                });
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("受付");
        BorderPane root = new BorderPane();
        mainPane = new MainPane();
        mainPane.setPadding(new Insets(10, 10, 10, 10));
        root.setCenter(mainPane);
        root.setTop(createMenuBar());
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().addAll(
                "css/WqueueTable.css"
        );
        primaryStage.setScene(scene);
        primaryStage.show();
        tracker = new Tracker(wsUrl, mainPane, Service.api);
        tracker.start(() -> Platform.runLater(() -> Globals.setTracking(true)));
        mainPane.addEventHandler(RefreshEvent.eventType, evt -> {
            if (tracker.isRunning()) {
                tracker.reload();
            } else {
                doManualUpdate();
            }
        });
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Service.stop();
        tracker.shutdown();
    }

    private MenuBar createMenuBar() {
        MenuBar mbar = new MenuBar();
        {
            Menu menu = new Menu("同期");
            ToggleGroup toggleGroup = new ToggleGroup();
            RadioMenuItem syncItem = new RadioMenuItem("同期する");
            menu.getItems().add(syncItem);
            RadioMenuItem unsyncItem = new RadioMenuItem("同期しない");
            menu.getItems().add(unsyncItem);
            toggleGroup.getToggles().addAll(syncItem, unsyncItem);
            if( Globals.isTracking() ){
                syncItem.setSelected(true);
            } else {
                unsyncItem.setSelected(true);
            }
            Globals.trackingProperty().addListener((obs, oldValue, newValue) -> {
                if( Globals.isTracking() ){
                    syncItem.setSelected(true);
                } else {
                    unsyncItem.setSelected(true);
                }
            });
            syncItem.setOnAction(evt -> doRestartTracking());
            unsyncItem.setOnAction(evt -> doStopTracking());
            mbar.getMenus().add(menu);
        }
        {
            Menu menu = new Menu("設定");
            CheckMenuItem item = new CheckMenuItem("保険者番号をチェックする");
            item.setSelected(true);
            Globals.checkingHokenshaBangouProperty().bindBidirectional(item.selectedProperty());
            menu.getItems().add(item);
            mbar.getMenus().add(menu);
        }
        return mbar;
    }

    private void doRestartTracking(){
        if( !tracker.isRunning() ){
            List<WqueueTable.Model> models = tracker.getWqueueList().stream()
                    .map(WqueueModel::new).collect(Collectors.toList());
            mainPane.setWqueueModels(models);
            tracker.restart(() -> Platform.runLater(() -> Globals.setTracking(true)));
        }
    }

    private void doStopTracking() {
        tracker.shutdown();
        Globals.setTracking(false);
    }

    private void doManualUpdate(){
        Service.api.listWqueueFull()
                .thenAccept(result -> {
                    List<WqueueTable.Model> list = result.stream()
                            .map(WqueueDTOModel::new).collect(Collectors.toList());
                    mainPane.setWqueueModels(list);
                })
                .exceptionally(HandlerFX::exceptionally);
    }

}
