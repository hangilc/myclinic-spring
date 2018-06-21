package jp.chang.myclinic.reception;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.reception.javafx.MainPane;
import jp.chang.myclinic.reception.tracker.Tracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main extends Application {

    private static Logger logger = LoggerFactory.getLogger(Main.class);
    private static String wsUrl;
    private Tracker tracker;

    public static void main(String[] args) throws IOException {
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
        MainPane mainPane = new MainPane();
        mainPane.setPadding(new Insets(10, 10, 10, 10));
        Scene scene = new Scene(mainPane, 600, 400);
        scene.getStylesheets().addAll(
                "css/WqueueTable.css"
        );
        primaryStage.setScene(scene);
        primaryStage.show();
        tracker = new Tracker(wsUrl, mainPane, Service.api);
        tracker.start();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Service.stop();
        tracker.shutdown();
    }

}
