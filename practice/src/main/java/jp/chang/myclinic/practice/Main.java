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
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.frontend.FrontendBackend;
import jp.chang.myclinic.frontend.FrontendClient;
import jp.chang.myclinic.practice.componenttest.ComponentTest;
import jp.chang.myclinic.practice.javafx.MainPane;
import jp.chang.myclinic.practice.testgui.TestGui;
import jp.chang.myclinic.practice.testintegration.TestIntegration;
import jp.chang.myclinic.support.diseaseexample.DiseaseExampleFileProvider;
import jp.chang.myclinic.support.houkatsukensa.HoukatsuKensaFile;
import jp.chang.myclinic.support.kizainames.KizaiNamesFile;
import jp.chang.myclinic.support.meisai.MeisaiServiceImpl;
import jp.chang.myclinic.support.shinryounames.ShinryouNamesFile;
import jp.chang.myclinic.support.stockdrug.StockDrugFile;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import javax.sql.DataSource;
import java.nio.file.Paths;
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
        Context.cmdOpts = cmdOpts;
        Application.launch(Main.class, args);
    }

    @Override
    public void start(Stage stage) {
        CmdOpts opts = Context.cmdOpts;
        if (opts.componentTest) {
            new ComponentTest(stage).runAll();
        } else if( opts.componentTestOne != null ){
            if( opts.componentTestOne.length != 2 ){
                System.err.println("CLASSNAME:METHODNAME expected");
                System.exit(1);
            }
            new ComponentTest(stage).runOne(opts.componentTestOne[0], opts.componentTestOne[1]);
        } else if (opts.sqliteTemp != null) {
            String dbFile = opts.sqliteTemp;
            DataSource ds = SqliteDataSource.createTemporaryFromDbFile(dbFile);
            DbBackend dbBackend = new DbBackend(ds, SqliteTableSet::create,
                    new StockDrugFile(Paths.get("config/stock-drug.txt")),
                    new HoukatsuKensaFile(Paths.get("config/houkatsu-kensa.xml")),
                    new MeisaiServiceImpl(),
                    new DiseaseExampleFileProvider(Paths.get("config/disease-example.yml")),
                    new ShinryouNamesFile(Paths.get("config/shinryou-names.yml")),
                    new KizaiNamesFile(Paths.get("config/kizai-names.yml")));
            Context.frontend = new FrontendBackend(dbBackend);
        } else {
//            Service.setServerUrl(opts.getServerUrl());
//            {
//                this.client = new Client(cmdArgs.getServerUrl());
//                Context.getInstance().setFrontend(new FrontendClient(client.getApi()));
//            }
            setupPracticeEnv();
            stage.setTitle("診療");
            PracticeEnv.INSTANCE.currentPatientProperty().addListener((obs, oldValue, newValue) ->
                    updateTitle(stage, newValue));
            MainPane root = new MainPane();
            Context.mainPane = root;
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
    }

    @Override
    public void stop() throws Exception {
        super.stop();
//        {
//            OkHttpClient client = Service.client;
//            if (client != null) {
//                client.dispatcher().executorService().shutdown();
//                client.connectionPool().evictAll();
//                Cache cache = client.cache();
//                if (cache != null) {
//                    cache.close();
//                }
//            }
//        }
        {
            if (client != null) {
                this.client.stop();
            }
        }
    }

    private static void setupPracticeEnv() {
//        CompletableFuture.allOf(
//                Context.frontend.getClinicInfo().thenAccept(PracticeEnv.INSTANCE::setClinicInfo),
//                Context.frontend.getReferList().thenAccept(PracticeEnv.INSTANCE::setReferList),
//                Context.frontend.getPracticeConfig().thenAccept(c -> PracticeEnv.INSTANCE.setKouhatsuKasan(c.kouhatsuKasan))
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
