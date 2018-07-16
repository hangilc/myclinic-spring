package jp.chang.myclinic.reception;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.reception.event.RefreshEvent;
import jp.chang.myclinic.reception.javafx.MainPane;
import jp.chang.myclinic.reception.tracker.Tracker;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {

    private static Logger logger = LoggerFactory.getLogger(Main.class);
    private static String wsUrl;
    private Tracker tracker;
    private Scope scope;

    public static void main(String[] args) {
        ReceptionArgs receptionArgs = ReceptionArgs.parseArgs(args);
        Service.setServerUrl(receptionArgs.serverUrl);
        wsUrl = receptionArgs.serverUrl.replace("/json/", "/practice-log");
        ReceptionEnv.INSTANCE.updateWithArgs(receptionArgs);
        Service.api.getClinicInfo()
                .thenAccept(clinicInfo -> {
                    ReceptionEnv.INSTANCE.setClinicInfo(clinicInfo);
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
        this.scope = new Scope();
        BorderPane root = new BorderPane();
        MainPane mainPane = new MainPane(scope);
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
        tracker.start(() -> Platform.runLater(() -> scope.setTracking(true)));
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
            if( scope.isTracking() ){
                syncItem.setSelected(true);
            } else {
                unsyncItem.setSelected(true);
            }
            scope.trackingProperty().addListener((obs, oldValue, newValue) -> {
                if( scope.isTracking() ){
                    syncItem.setSelected(true);
                } else {
                    unsyncItem.setSelected(true);
                }
            });
            syncItem.setOnAction(evt -> doRestartTracking());
            unsyncItem.setOnAction(evt -> doStopTracking());
            mbar.getMenus().add(menu);
        }
        return mbar;
    }

    private void doRestartTracking(){
        tracker.restart(() -> Platform.runLater(() -> scope.setTracking(true)));
    }

    private void doStopTracking() {
        tracker.shutdown();
        scope.setTracking(false);
    }

    private void doManualUpdate(){
        Service.api.listWqueueFull()
                .thenAccept(result -> {
                    System.out.println(result);
                })
                .exceptionally(HandlerFX::exceptionally);
    }

}
