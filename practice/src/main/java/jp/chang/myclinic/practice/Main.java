package jp.chang.myclinic.practice;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.backenddb.DB;
import jp.chang.myclinic.backenddb.DBImpl;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.SerialDB;
import jp.chang.myclinic.practice.guitest.GuiTest;
import jp.chang.myclinic.support.SupportSet;
import jp.chang.myclinic.backendsqlite.SqliteDataSource;
import jp.chang.myclinic.backendsqlite.SqliteTableSet;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.frontend.FrontendBackend;
import jp.chang.myclinic.frontend.FrontendRest;
import jp.chang.myclinic.practice.componenttest.ComponentTest;
import jp.chang.myclinic.practice.guitest.GuiTestRunner;
import jp.chang.myclinic.practice.javafx.MainPane;
import jp.chang.myclinic.support.clinicinfo.ClinicInfoFileProvider;
import jp.chang.myclinic.support.config.ConfigPropertyFile;
import jp.chang.myclinic.support.diseaseexample.DiseaseExampleFileProvider;
import jp.chang.myclinic.support.houkatsukensa.HoukatsuKensaFile;
import jp.chang.myclinic.support.kizaicodes.KizaicodeFileResolver;
import jp.chang.myclinic.support.meisai.MeisaiServiceImpl;
import jp.chang.myclinic.support.shinryoucodes.ShinryoucodeFileResolver;
import jp.chang.myclinic.support.stockdrug.StockDrugFile;
import picocli.CommandLine;

import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main extends Application {

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
            StackPane main = setupStageForComponentTest(stage);
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
            StackPane main = setupStageForComponentTest(stage);
            new Thread(() -> {
                boolean ok = new ComponentTest(stage, main).testOne(className, methodName);
                if (ok) {
                    System.out.println("done");
                } else {
                    System.out.printf("Cannot run test: %s:%s\n", className, methodName);
                    Platform.exit();
                }
            }).start();
        } else {
            SupportSet ss = new SupportSet();
            ss.stockDrugService = new StockDrugFile(Paths.get("config/stock-drug.txt"));
            ss.houkatsuKensaService = new HoukatsuKensaFile(Paths.get("config/houkatsu-kensa.xml"));
            ss.meisaiService = new MeisaiServiceImpl();
            ss.diseaseExampleProvider = new DiseaseExampleFileProvider(Paths.get("config/disease-example.yml"));
            ss.shinryoucodeResolver = new ShinryoucodeFileResolver(new File("config/shinryoucodes.yml"));
            ss.kizaicodeResolver = new KizaicodeFileResolver(new File("config/kizaicodes.yml"));
            ss.clinicInfoProvider = new ClinicInfoFileProvider(Paths.get("config/clinic-info.yml"));
            setupFrontend(ss);
            Context.setConfigService(new ConfigPropertyFile(Paths.get(
                    System.getProperty("user.home"),
                    "myclinic-env",
                    "practice.properties"
            )));
            Context.referService = new ReferServiceFile(Paths.get("config", "refer-list.yml"));
            Context.integrationService = new IntegrationServiceImpl();
            Context.currentPatientService = new CurrentPatientService();
            Context.mainStageService = new MainStageServiceImpl(stage);
            if (opts.guiTest) {
                runGuiTest(stage, "*.*");
            } else if (opts.guiTestSelected != null) {
                runGuiTest(stage, opts.guiTestSelected);
            } else {
                Context.currentPatientService.addOnChangeHandler(this::updateStageTitle);
                updateStageTitle(null, 0);
                MainPane root = new MainPane();
                Context.mainStageService = new MainStageServiceImpl(stage);
                root.getStylesheets().addAll(
                        "css/Practice.css"
                );
                stage.setScene(new Scene(root));
               stage.show();
            }
        }
    }

    @Override
    public void stop() {
        if( Context.frontend != null ){
            Context.frontend.shutdown();
        }
    }

    private void runGuiTest(Stage stage, String filter){
        confirmTestDatabase();
        StackPane main = setupStageForComponentTest(stage);
        Tester tester = new Tester();
        tester.addTargets(GuiTest.listTargets(stage, main));
        tester.runTest(filter);
        System.out.println("done");
    }

    private void setupFrontend(SupportSet ss) {
        CmdOpts opts = Context.cmdOpts;
        String param = opts.serverUrl;
        if (param != null && param.startsWith("sqlite-temp:")) {
            String dbFile = param.split(":", 2)[1];
            if (dbFile.isEmpty()) {
                dbFile = Paths.get(System.getProperty("user.home"),
                        "sqlite-data", "myclinic-test-sqlite.db").toString();
            }
            DataSource ds = SqliteDataSource.createTemporaryFromDbFile(dbFile);
            DB db = new SerialDB(new DBImpl(ds));
            DbBackend dbBackend = new DbBackend(db, SqliteTableSet::create);
            Context.frontend = new FrontendBackend(dbBackend, ss);
        } else {
            Context.frontend = new FrontendRest(param);
        }
    }

    private void confirmTestDatabase() {
        PatientDTO patient = Context.frontend.getPatient(1).join();
        if (patient != null && patient.lastName.equals("試験") && patient.firstName.equals("データ")) {
            // nop
        } else {
            System.err.println("Test database is required for testing.");
            Platform.exit();
        }
    }

    private StackPane setupStageForComponentTest(Stage stage) {
        StackPane main = new StackPane();
        main.setStyle("-fx-padding: 10");
        main.getStylesheets().add("css/Practice.css");
        main.setAlignment(Pos.TOP_LEFT);
        stage.setScene(new Scene(main));
        stage.show();
        return main;
    }

    private void updateStageTitle(PatientDTO patient, int visitId) {
        if (patient == null) {
            Context.mainStageService.setTitle("診察");
        } else {
            String title = String.format("診察 (%d) %s%s",
                    patient.patientId,
                    patient.lastName,
                    patient.firstName);
            Context.mainStageService.setTitle(title);
        }
    }
}
