package jp.chang.myclinic.practice;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.practice.javafx.MainPane;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

// TODO: improve printer setting (~/practice-env)
public class Main extends Application {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        CommandArgs commandArgs = new CommandArgs(args);
        Service.setServerUrl(commandArgs.getServerUrl());
        setupPracticeEnv(commandArgs, () -> Application.launch(Main.class, args));
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("診療");
        PracticeEnv.INSTANCE.currentPatientProperty().addListener((obs, oldValue, newValue) ->
                updateTitle(stage, newValue));
        MainPane root = new MainPane();
        root.getStylesheets().addAll(
                "css/Practice.css"
        );
        stage.setScene(new Scene(root));
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

    private static void setupPracticeEnv(CommandArgs commandArgs, Runnable cb) throws IOException {
        PracticeEnv.INSTANCE = new PracticeEnv(commandArgs);
        Service.api.getClinicInfo()
                .thenCompose(clinicInfo -> {
                    PracticeEnv.INSTANCE.setClinicInfo(clinicInfo);
                    return Service.api.getReferList();
                })
                .thenCompose(referItems -> {
                    PracticeEnv.INSTANCE.setReferList(referItems);
                    return Service.api.getPracticeConfig();
                })
                .thenAccept(config -> {
                    PracticeEnv.INSTANCE.setKouhatsuKasan(config.kouhatsuKasan);
                    cb.run();
                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    return null;
                });
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
