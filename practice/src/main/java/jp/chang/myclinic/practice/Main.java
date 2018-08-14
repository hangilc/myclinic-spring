package jp.chang.myclinic.practice;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.practice.javafx.MainPane;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

// TODO: add delete gazou label link
@SpringBootApplication
public class Main extends Application implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(Main.class);
    private static ConfigurableApplicationContext ctx;

    public static void main(String[] args) throws IOException {
        Application.launch(Main.class, args);
    }

    @Override
    public void start(Stage stage) {
        ctx = SpringApplication.run(Main.class, getParameters().getRaw().toArray(new String[]{}));
        MainScope mainScope = ctx.getBean(MainScope.class);
        if( mainScope.debugHttp ){
            Service.setLogBody();
        }
        setupPracticeEnv();
        stage.setTitle("診療");
        PracticeEnv.INSTANCE.currentPatientProperty().addListener((obs, oldValue, newValue) ->
                updateTitle(stage, newValue));
        MainPane root = ctx.getBean(MainPane.class);
        root.getStylesheets().addAll(
                "css/Practice.css"
        );
        stage.setScene(new Scene(root));
        stage.showingProperty().addListener((obs, oldVaue, newValue) -> {
            if( !newValue ){
                PracticeEnv.INSTANCE.closeRemainingWindows();
            }
        });
        stage.show();
    }

    @Override
    public void run(String... args){
        String serverUrl = null;
        if( args.length == 0 ){
            serverUrl = System.getenv("MYCLINIC_SERVICE");
        } else {
            serverUrl = args[0];
        }
        if( serverUrl == null ){
            logger.error("Cannot find server url.");
            System.exit(1);
        }
        Service.setServerUrl(serverUrl);
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
        ctx.close();
    }

    private static void setupPracticeEnv() {
        CompletableFuture.allOf(
                Service.api.getClinicInfo().thenAccept(PracticeEnv.INSTANCE::setClinicInfo),
                Service.api.getReferList().thenAccept(PracticeEnv.INSTANCE::setReferList),
                Service.api.getPracticeConfig().thenAccept(c -> PracticeEnv.INSTANCE.setKouhatsuKasan(c.kouhatsuKasan))
        ).exceptionally(t -> {
            logger.error("setupPracticeEnv failed. {}", t);
            System.exit(1);
            return null;
        }).join();
    }

    private void updateTitle(Stage stage, PatientDTO patient) {
        if (patient == null) {
            stage.setTitle("診察");
        } else {
            String title = String.format("診察 (%d) %s%s",
                    patient.patientId,
                    patient.lastName,
                    patient.firstName);
            stage.setTitle(title);
        }
    }
}
