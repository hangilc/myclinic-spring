package jp.chang.myclinic.practice;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.practice.grpc.MgmtServer;
import jp.chang.myclinic.practice.javafx.MainPane;
import jp.chang.myclinic.practice.testgui.PracticeTestGui;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@SpringBootApplication
public class Main extends Application {

    private static Logger logger = LoggerFactory.getLogger(Main.class);
    private static ConfigurableApplicationContext ctx;
    private static CmdArgs cmdArgs;
    private MgmtServer mgmtServer;

    public static void main(String[] args) throws IOException {
        cmdArgs = new CmdArgs(args);
        Service.setServerUrl(cmdArgs.getServerUrl());
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
        Globals.getInstance().setMainPane(root);
        root.getStylesheets().addAll(
                "css/Practice.css"
        );
        stage.setScene(new Scene(root));
        stage.showingProperty().addListener((obs, oldVaue, newValue) -> {
            if( !newValue ){
                PracticeEnv.INSTANCE.closeRemainingWindows();
            }
        });
        Integer managementPort = cmdArgs.getManagementPort();
        if( managementPort != null && managementPort > 0 ){
            {
                ch.qos.logback.classic.Logger log = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger("io.grpc");
                log.setLevel(ch.qos.logback.classic.Level.ERROR);
            }
            this.mgmtServer = new MgmtServer(managementPort);
            mgmtServer.start();
            System.out.printf("Listening to management port :%d\n", managementPort);
        }
        if( cmdArgs.isTestGui() ){
            Thread selfTestExecutor = new Thread(() -> {
                try {
                    new PracticeTestGui().run();
                    //Platform.exit();
                } catch(Exception ex){
                    ex.printStackTrace();
                }
            });
            selfTestExecutor.setDaemon(true);
            selfTestExecutor.start();
        }
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
        ctx.close();
        if( mgmtServer !=  null ){
            mgmtServer.stop();
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
