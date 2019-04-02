package jp.chang.myclinic.practice;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backendsqlite.SqliteDataSource;
import jp.chang.myclinic.backendsqlite.SqliteTableSet;
import jp.chang.myclinic.client.Client;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.frontend.FrontendBackend;
import jp.chang.myclinic.frontend.FrontendClient;
import jp.chang.myclinic.practice.javafx.MainPane;
import jp.chang.myclinic.practice.testgui.TestGui;
import jp.chang.myclinic.practice.testintegration.TestIntegration;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class Main extends Application {

    private static Logger logger = LoggerFactory.getLogger(Main.class);
    private Client client;

    public static void main(String[] args) {
        CmdOpts cmdOpts = new CmdOpts();
        CommandLine commandLine = new CommandLine(cmdOpts);
        commandLine.parse(args);
        if (commandLine.isUsageHelpRequested()) {
            commandLine.usage(System.out);
            return;
        }
        Context.getInstance().setCmdsOpts(cmdOpts);
        Application.launch(Main.class, args);
    }

    @Override
    public void start(Stage stage) {
        CmdOpts opts = Context.getInstance().getCmdsOpts();
        if (opts.getMockSqlite()) {
            String dbFile = opts.getServerUrl();
            DataSource ds = SqliteDataSource.createTemporaryFromDbFile(dbFile);
            DbBackend dbBackend = new DbBackend(ds, SqliteTableSet::create);
            Context.getInstance().setFrontend(new FrontendBackend(dbBackend));
        } else {
//            Service.setServerUrl(opts.getServerUrl());
//            {
//                this.client = new Client(cmdArgs.getServerUrl());
//                Context.getInstance().setFrontend(new FrontendClient(client.getApi()));
//            }
        }
        setupPracticeEnv();
        stage.setTitle("診療");
        PracticeEnv.INSTANCE.currentPatientProperty().addListener((obs, oldValue, newValue) ->
                updateTitle(stage, newValue));
        MainPane root = new MainPane();
        Context.getInstance().setMainPane(root);
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
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        {
            OkHttpClient client = Service.client;
            if (client != null) {
                client.dispatcher().executorService().shutdown();
                client.connectionPool().evictAll();
                Cache cache = client.cache();
                if (cache != null) {
                    cache.close();
                }
            }
        }
        {
            if (client != null) {
                this.client.stop();
            }
        }
    }

    private static void setupPracticeEnv() {
//        CompletableFuture.allOf(
//                Context.getInstance().getFrontend().getClinicInfo().thenAccept(PracticeEnv.INSTANCE::setClinicInfo),
//                Context.getInstance().getFrontend().getReferList().thenAccept(PracticeEnv.INSTANCE::setReferList),
//                Context.getInstance().getFrontend().getPracticeConfig().thenAccept(c -> PracticeEnv.INSTANCE.setKouhatsuKasan(c.kouhatsuKasan))
//        ).exceptionally(t -> {
//            logger.error("setupPracticeEnv failed. {}", t);
//            System.exit(1);
//            return null;
//        }).join();
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
