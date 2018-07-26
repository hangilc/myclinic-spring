package jp.chang.myclinic.pharma;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.pharma.javafx.MainScene;
import jp.chang.myclinic.pharma.javafx.drawerpreview.ListSettingDialog;
import jp.chang.myclinic.pharma.javafx.drawerpreview.NewSetting;
import jp.chang.myclinic.pharma.javafx.drawerpreview.SelectDefaultSettingDialog;
import jp.chang.myclinic.pharma.javafx.lib.ToggleGroupWithValue;
import jp.chang.myclinic.pharma.javafx.pharmadrug.PharmaDrugDialog;
import jp.chang.myclinic.pharma.javafx.prevtechou.PrevTechouDialog;
import jp.chang.myclinic.pharma.javafx.printing.Printing;
import jp.chang.myclinic.pharma.tracking.ModelDispatchAction;
import jp.chang.myclinic.tracker.Tracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.nio.file.Paths;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

@SpringBootApplication
public class Main extends Application {

    private static Logger logger = LoggerFactory.getLogger(Main.class);
    private static ConfigurableApplicationContext ctx;
    private String wsUrl;
    private Tracker tracker;
    private static String[] args;
    private MainScope mainScope;

    public static void main(String[] args) {
        logger.info("pharma invoked");
        Main.args = args;
        Application.launch(Main.class, args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        ctx = SpringApplication.run(Main.class, args);
        List<String> args = getParameters().getUnnamed();
        if( args.size() == 1 ){
            String serverUrl = args.get(0);
            if( !serverUrl.endsWith("/") ){
                serverUrl = serverUrl + "/";
            }
            Service.setServerUrl(serverUrl);
            Globals.clinicInfo = Service.api.getClinicInfoCall().execute().body();
            Globals.printerEnv = new PrinterEnv(Paths.get(System.getProperty("user.home"), "myclinic-env", "printer-settings"));
            this.wsUrl = serverUrl.replace("/json/", "/practice-log");
        } else {
            logger.error("Usage: pharma service-url");
            System.exit(1);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("薬局");
        mainScope = ctx.getBean(MainScope.class);
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createMenu());
        MainScene root = ctx.getBean(MainScene.class);
        root.getStylesheets().add("Pharma.css");
        borderPane.setCenter(root);
        ModelDispatchAction modelDispatchAction = mainScope.getModelDispatchAction();
        tracker = new Tracker(wsUrl, modelDispatchAction, Service.api::listPracticeLogInRangeCall){
            @Override
            protected void beforeCatchup() {
                System.out.println("beforeCatchup");
            }

            @Override
            protected void afterCatchup() {
                System.out.println("afterCatchup");
            }
        };
        tracker.setCallbackWrapper(Platform::runLater);
        stage.setScene(new Scene(borderPane));
        stage.show();
        tracker.start(() -> {});
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Service.stop();
        tracker.shutdown();
        ctx.close();
        logger.info("pharma stopped.");
    }

    private Node createMenu(){
        MenuBar mbar = new MenuBar();
        {
            Menu menu = new Menu("アクション");
            {
                MenuItem item = new MenuItem("過去のお薬手帳");
                item.setOnAction(evt -> {
                    new PrevTechouDialog().show();
                });
                menu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("薬剤情報管理");
                item.setOnAction(evt -> {
                    new PharmaDrugDialog().show();
                });
                menu.getItems().add(item);
            }
            mbar.getMenus().add(menu);
        }
        {
            Menu menu = new Menu("設定");
            {
                MenuItem item = new MenuItem("処方内容印刷設定");
                item.setOnAction(evt -> openPrescContentPrinterSettingDialog());
                menu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("薬袋印刷設定");
                item.setOnAction(evt -> openDrugBagPrinterSettingDialog());
                menu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("薬手帳印刷設定");
                item.setOnAction(evt -> openTechouPrinterSettingDialog());
                menu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("新規印刷設定");
                item.setOnAction(evt -> NewSetting.createNewPrinterSetting(Globals.printerEnv, name -> {}));
                menu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("印刷設定の一覧");
                item.setOnAction(evt -> new ListSettingDialog(Globals.printerEnv).show());
                menu.getItems().add(item);
            }
            mbar.getMenus().add(menu);
        }
        {
            Menu menu = new Menu("印刷");
            {
                MenuItem item = new MenuItem("内服薬袋印刷");
                item.setOnAction(evt -> Printing.previewDrugBag(DrugCategory.Naifuku));
                menu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("頓服薬袋印刷");
                item.setOnAction(evt -> Printing.previewDrugBag(DrugCategory.Tonpuku));
                menu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("外用薬袋印刷");
                item.setOnAction(evt -> Printing.previewDrugBag(DrugCategory.Gaiyou));
                menu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("おくすり薬袋印刷");
                item.setOnAction(evt -> Printing.previewDrugBag(null));
                menu.getItems().add(item);
            }
            mbar.getMenus().add(menu);
        }
        {
            Menu menu = new Menu("同期");
            ToggleGroupWithValue<Boolean> group = new ToggleGroupWithValue<>();
            RadioMenuItem syncItem = new RadioMenuItem("同期する");
            syncItem.setSelected(mainScope.isTracking());
            menu.getItems().add(syncItem);
            RadioMenuItem unsyncItem = new RadioMenuItem("同期しない");
            unsyncItem.setSelected(!mainScope.isTracking());
            menu.getItems().add(unsyncItem);
            group.addToggle(syncItem, true);
            group.addToggle(unsyncItem, false);
            group.valueProperty().addListener((obs, oldValue, newValue) -> {
                mainScope.setTracking(newValue);
            });
            mbar.getMenus().add(menu);
        }
        return mbar;
    }

    private void openDefaultPrinterSettingDialog(String titleName, Function<Config, String> currentGetter,
                                                 BiConsumer<Config, String> currentSetter){
        Config.load().ifPresent(config -> {
            String current = currentGetter.apply(config);
            PrinterEnv printerEnv = Globals.printerEnv;
            SelectDefaultSettingDialog dialog = new SelectDefaultSettingDialog(current, printerEnv) {
                @Override
                protected void onChange(String newDefaultSetting) {
                    Config.load().ifPresent(config -> {
                        currentSetter.accept(config, newDefaultSetting);
                        config.save();
                    });
                }
            };
            dialog.setTitle(titleName + "の既定印刷設定の選択");
            dialog.show();
        });
    }

    private void openPrescContentPrinterSettingDialog() {
        openDefaultPrinterSettingDialog("印刷内容", Config::getPrescContentPrinterSetting,
                Config::setPrescContentPrinterSetting);
    }

    private void openDrugBagPrinterSettingDialog(){
        openDefaultPrinterSettingDialog("薬袋", Config::getDrugBagPrinterSetting,
                Config::setDrugBagPrinterSetting);
    }

    private void openTechouPrinterSettingDialog(){
        openDefaultPrinterSettingDialog("薬手帳", Config::getTechouPrinterSetting,
                Config::setTechouPrinterSetting);
    }

}
