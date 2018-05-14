package jp.chang.myclinic.pharma;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.drawer.printer.manager.PrinterEnv;
import jp.chang.myclinic.pharma.javafx.MainScene;
import jp.chang.myclinic.pharma.javafx.drawerpreview.ListSettingDialog;
import jp.chang.myclinic.pharma.javafx.drawerpreview.NewSetting;
import jp.chang.myclinic.pharma.javafx.drawerpreview.SelectDefaultSettingDialog;
import jp.chang.myclinic.pharma.javafx.pharmadrug.PharmaDrugDialog;
import jp.chang.myclinic.pharma.javafx.prevtechou.PrevTechouDialog;
import jp.chang.myclinic.pharma.javafx.printing.Printing;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Main extends Application {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    // TODO: update wqueue list automatically
    public static void main(String[] args) {
        logger.info("pharma invoked");
        Application.launch(Main.class, args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        List<String> args = getParameters().getUnnamed();
        if( args.size() == 1 ){
            String serverUrl = args.get(0);
            if( !serverUrl.endsWith("/") ){
                serverUrl = serverUrl + "/";
            }
            Service.setServerUrl(serverUrl);
            Globals.clinicInfo = Service.api.getClinicInfoCall().execute().body();
            Globals.printerEnv = new PrinterEnv(Paths.get(System.getProperty("user.home"), "myclinic-env", "printer-settings"));
        } else {
            logger.error("Usage: pharma service-url");
            System.exit(1);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("薬局");
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createMenu());
        MainScene root = new MainScene();
        root.getStylesheets().add("Pharma.css");
        borderPane.setCenter(root);
        stage.setScene(new Scene(borderPane));
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
