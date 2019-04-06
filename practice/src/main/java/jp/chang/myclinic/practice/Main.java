package jp.chang.myclinic.practice;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.SupportSet;
import jp.chang.myclinic.backendsqlite.SqliteDataSource;
import jp.chang.myclinic.backendsqlite.SqliteTableSet;
import jp.chang.myclinic.client.Client;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.frontend.FrontendBackend;
import jp.chang.myclinic.frontend.FrontendClient;
import jp.chang.myclinic.practice.componenttest.ComponentTest;
import jp.chang.myclinic.practice.javafx.MainPane;
import jp.chang.myclinic.practice.javafx.text.TextEditForm;
import jp.chang.myclinic.practice.testgui.TestGui;
import jp.chang.myclinic.practice.testintegration.TestIntegration;
import jp.chang.myclinic.support.clinicinfo.ClinicInfoFileProvider;
import jp.chang.myclinic.support.clinicinfo.ClinicInfoProvider;
import jp.chang.myclinic.support.config.ConfigPropertyFile;
import jp.chang.myclinic.support.diseaseexample.DiseaseExampleFileProvider;
import jp.chang.myclinic.support.diseaseexample.DiseaseExampleProvider;
import jp.chang.myclinic.support.houkatsukensa.HoukatsuKensaFile;
import jp.chang.myclinic.support.houkatsukensa.HoukatsuKensaService;
import jp.chang.myclinic.support.kizainames.KizaiNamesFile;
import jp.chang.myclinic.support.kizainames.KizaiNamesService;
import jp.chang.myclinic.support.meisai.MeisaiService;
import jp.chang.myclinic.support.meisai.MeisaiServiceImpl;
import jp.chang.myclinic.support.shinryounames.ShinryouNamesFile;
import jp.chang.myclinic.support.shinryounames.ShinryouNamesService;
import jp.chang.myclinic.support.stockdrug.StockDrugFile;
import jp.chang.myclinic.support.stockdrug.StockDrugService;
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
            Pane main = setupStageForComponentTest(stage);
            new Thread(() -> {
                new ComponentTest(stage, main).testAll();
                System.out.println("done");
            }).start();
        } else if (opts.componentTestOne != null) {
            if (opts.componentTestOne.length == 0 || opts.componentTestOne.length >= 3) {
                System.err.println("CLASSNAME[:METHODNAME] expected");
                System.exit(1);
            }
            String className = opts.componentTestOne[0];
            String methodName = opts.componentTestOne.length == 2 ? opts.componentTestOne[1] : "";
            Pane main = setupStageForComponentTest(stage);
            new Thread(() -> {
                boolean ok = new ComponentTest(stage, main).testOne(className, methodName);
                if( ok ){
                    System.out.println("done");
                } else {
                    System.out.printf("Cannot run test: %s:%s\n", className, methodName);
                    Platform.exit();
                }
            }).start();
        } else if (opts.sqliteTemp != null) {
            String dbFile = opts.sqliteTemp;
            DataSource ds = SqliteDataSource.createTemporaryFromDbFile(dbFile);
            SupportSet ss = new SupportSet();
            ss.stockDrugService = new StockDrugFile(Paths.get("config/stock-drug.txt"));
            ss.houkatsuKensaService = new HoukatsuKensaFile(Paths.get("config/houkatsu-kensa.xml"));
            ss.meisaiService = new MeisaiServiceImpl();
            ss.diseaseExampleProvider = new DiseaseExampleFileProvider(Paths.get("config/disease-example.yml"));
            ss.shinryouNamesService = new ShinryouNamesFile(Paths.get("config/shinryou-names.yml"));
            ss.kizaiNamesService = new KizaiNamesFile(Paths.get("config/kizai-names.yml"));
            ss.clinicInfoProvider = new ClinicInfoFileProvider(Paths.get("config/clinic-info-yml"));
            DbBackend dbBackend = new DbBackend(ds, SqliteTableSet::create, ss);
            Context.frontend = new FrontendBackend(dbBackend);
            Context.configService = new ConfigPropertyFile(Paths.get(
                    System.getenv("user.home"),
                    "practice.properties"
            ));
        } else {
            setupPracticeEnv();
            stage.setTitle("診療");
            PracticeEnv.INSTANCE.currentPatientProperty().addListener((obs, oldValue, newValue) ->
                    updateTitle(stage, newValue));
            MainPane root = new MainPane();
            Context.mainPane = root;
            Context.mainStageService = new MainStageServiceImpl(stage);
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

    private Pane setupStageForComponentTest(Stage stage) {
        Pane main = new StackPane();
        main.setStyle("-fx-padding: 10");
        main.getStylesheets().add("css/Practice.css");
        stage.setScene(new Scene(main));
        stage.show();
        return main;
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
