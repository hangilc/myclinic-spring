package jp.chang.myclinic.practice;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.practice.javafx.MainPane;
import jp.chang.myclinic.practice.testgui.TestGui;
import jp.chang.myclinic.practice.testintegration.TestIntegration;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.CompletableFuture;

@SpringBootApplication
public class Main extends Application {

    private static Logger logger = LoggerFactory.getLogger(Main.class);
    private static ConfigurableApplicationContext ctx;
    private static CmdArgs cmdArgs;

    public static void main(String[] args) {
        cmdArgs = new CmdArgs(args);
        Application.launch(Main.class, args);
    }

    @Override
    public void start(Stage stage) {
        if (cmdArgs.isTestGui()) {
            new TestGui(stage).run(cmdArgs.getTestGui());
        } else {
            Service.setServerUrl(cmdArgs.getServerUrl());
            ctx = SpringApplication.run(Main.class, getParameters().getRaw().toArray(new String[]{}));
            MainScope mainScope = ctx.getBean(MainScope.class);
            if (mainScope.debugHttp) {
                Service.setLogBody();
            }
            setupPracticeEnv();
            stage.setTitle("診療");
            PracticeEnv.INSTANCE.currentPatientProperty().addListener((obs, oldValue, newValue) ->
                    updateTitle(stage, newValue));
            MainPane root = ctx.getBean(MainPane.class);
            Globals.getInstance().setMainPane(root);
            root.getStylesheets().addAll(
                    "css/Practice.css"
            );
            stage.setScene(new Scene(root));
            stage.showingProperty().addListener((obs, oldVaue, newValue) -> {
                if (!newValue) {
                    PracticeEnv.INSTANCE.closeRemainingWindows();
                }
            });
            stage.show();
            if (cmdArgs.isTestIntegration() || cmdArgs.getTestIntegrationOne() != null) {
                Thread selfTestExecutor = new Thread(() -> {
                    try {
                        if( cmdArgs.isTestIntegration() ){
                            new TestIntegration().runAll();
                            Platform.exit();
                        } else {
                            new TestIntegration().runTest(cmdArgs.getTestIntegrationOne());
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
                selfTestExecutor.setDaemon(true);
                selfTestExecutor.start();
            }
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        OkHttpClient client = Service.client;
        if( client != null ) {
            client.dispatcher().executorService().shutdown();
            client.connectionPool().evictAll();
            Cache cache = client.cache();
            if (cache != null) {
                cache.close();
            }
            ctx.close();
        }
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
