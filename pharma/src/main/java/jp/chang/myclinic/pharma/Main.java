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
import jp.chang.myclinic.pharma.javafx.event.ReloadTrackingEvent;
import jp.chang.myclinic.pharma.javafx.lib.ToggleGroupWithValue;
import jp.chang.myclinic.pharma.javafx.pharmadrug.PharmaDrugDialog;
import jp.chang.myclinic.pharma.javafx.prevtechou.PrevTechouDialog;
import jp.chang.myclinic.pharma.javafx.printing.Printing;
import jp.chang.myclinic.pharma.tracking.ModelDispatchAction;
import jp.chang.myclinic.tracker.Tracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Main extends Application {

    private static Logger logger = LoggerFactory.getLogger(Main.class);
    private String wsUrl;
    private Tracker tracker;
    private static String[] args;

    public static void main(String[] args) {
        logger.info("pharma invoked");
        Application.launch(Main.class, args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        List<String> args = getParameters().getUnnamed();
        String serviceUrl;
        if (args.size() == 0) {
            serviceUrl = System.getenv("MYCLINIC_SERVICE");
        } else {
            serviceUrl = args.get(0);
        }
        if (!serviceUrl.endsWith("/")) {
            serviceUrl += "/";
        }
        Service.setServerUrl(serviceUrl);
        Globals.setClinicInfo(Service.api.getClinicInfoCall().execute().body());
        this.wsUrl = serviceUrl.replace("/json/", "/practice-log");
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("薬局");
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createMenu());
        MainScene root = new MainScene();
        root.getStylesheets().add("Pharma.css");
        borderPane.setCenter(root);
        ModelDispatchAction modelDispatchAction = Globals.createModelDispatchAction();
        tracker = new Tracker(wsUrl, modelDispatchAction, Service.api::listPracticeLogInRangeCall) {
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
        tracker.start(() -> {
        });
        root.addEventHandler(ReloadTrackingEvent.eventType, event -> tracker.probeUpdate());
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Service.stop();
        tracker.shutdown();
        logger.info("pharma stopped.");
    }

    private Node createMenu() {
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
                item.setOnAction(evt -> NewSetting.createNewPrinterSetting(Globals.getPrinterEnv(), name -> {
                }));
                menu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("印刷設定の一覧");
                item.setOnAction(evt -> new ListSettingDialog(Globals.getPrinterEnv()).show());
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
            menu.getItems().add(syncItem);
            RadioMenuItem unsyncItem = new RadioMenuItem("同期しない");
            menu.getItems().add(unsyncItem);
            group.addToggle(syncItem, true);
            group.addToggle(unsyncItem, false);
            group.valueProperty().bindBidirectional(Globals.trackingProperty());
            mbar.getMenus().add(menu);
        }
        return mbar;
    }

    private void openDefaultPrinterSettingDialog(String titleName, Supplier<String> currentGetter,
                                                 Consumer<String> currentSetter) {
        String current = currentGetter.get();
        PrinterEnv printerEnv = Globals.getPrinterEnv();
        SelectDefaultSettingDialog dialog = new SelectDefaultSettingDialog(current, printerEnv) {
            @Override
            protected void onChange(String newDefaultSetting) {
                currentSetter.accept(newDefaultSetting);
            }
        };
        dialog.setTitle(titleName + "の既定印刷設定の選択");
        dialog.show();
    }

    private void openPrescContentPrinterSettingDialog() {
        openDefaultPrinterSettingDialog("印刷内容",
                Globals::getPrescContentPrinterSetting, //Config::getPrescContentPrinterSetting,
                Globals::setPrescContentPrinterSetting  //Config::setPrescContentPrinterSetting
        );
    }

    private void openDrugBagPrinterSettingDialog() {
        openDefaultPrinterSettingDialog("薬袋",
                Globals::getDrugBagPrinterSetting,  //Config::getDrugBagPrinterSetting,
                Globals::setDrugBagPrinterSetting   //Config::setDrugBagPrinterSetting
        );
    }

    private void openTechouPrinterSettingDialog() {
        openDefaultPrinterSettingDialog("薬手帳",
                Globals::getTechouPrinterSetting,  //Config::getTechouPrinterSetting,
                Globals::setTechouPrinterSetting   //Config::setTechouPrinterSetting
        );
    }

}
