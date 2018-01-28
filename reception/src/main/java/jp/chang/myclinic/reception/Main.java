package jp.chang.myclinic.reception;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.WqueueFullDTO;
import jp.chang.myclinic.reception.javafx.MainPane;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Main extends Application {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        ReceptionArgs receptionArgs = ReceptionArgs.parseArgs(args);
        Service.setServerUrl(receptionArgs.serverUrl);
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
        MainPane mainPane = new MainPane();
        mainPane.setPadding(new Insets(10, 10, 10, 10));
        Scene scene = new Scene(mainPane, 600, 400);
        scene.getStylesheets().addAll(
                "css/WqueueTable.css"
        );
        primaryStage.setScene(scene);
        WqueueReloader reloader = new WqueueReloader(5000);
        reloader.setCallback(new WqueueReloader.Callback() {
            @Override
            public void onLoad(List<WqueueFullDTO> list) {
                Platform.runLater(() -> {
                    List<WqueueFullDTO> curr = ReceptionEnv.INSTANCE.getWqueueList();
                    if( !Objects.equals(list, curr) ){
                        ReceptionEnv.INSTANCE.setWqueueList(list);
                    }
                });
            }

            @Override
            public void onError(Throwable ex) {

            }
        });
        Thread reloaderThread = new Thread(reloader);
        reloaderThread.setDaemon(true);
        reloaderThread.start();
        ReceptionEnv.INSTANCE.setWqueueReloader(reloader);
        primaryStage.show();
        reloader.trigger();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        OkHttpClient client = Service.client;
        client.dispatcher().executorService().shutdown();
        client.connectionPool().evictAll();
        Cache cache = client.cache();
        if( cache != null ){
            cache.close();
        }
    }

}
