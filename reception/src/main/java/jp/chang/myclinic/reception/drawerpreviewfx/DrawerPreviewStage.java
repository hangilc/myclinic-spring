package jp.chang.myclinic.reception.drawerpreviewfx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.PaperSize;
import jp.chang.myclinic.drawer.printer.AuxSetting;
import jp.chang.myclinic.drawer.printer.DrawerPrinter;
import jp.chang.myclinic.myclinicenv.printer.PrinterEnv;
import jp.chang.myclinic.reception.drawerpreviewfx.printersetting.CreatePrinterSettingStage;
import jp.chang.myclinic.reception.drawerpreviewfx.printersetting.EditPrinterSettingStage;
import jp.chang.myclinic.utilfx.GuiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DrawerPreviewStage extends Stage {

    private static Logger logger = LoggerFactory.getLogger(DrawerPreviewStage.class);
    private PrinterEnv printerEnv;
    private String settingKey;
    private ObservableList<String> printerSettingNames = FXCollections.observableArrayList();
    private StringProperty currentSettingName = new SimpleStringProperty();
    private byte[] devnamesCache;
    private byte[] devmodeCache;
    private AuxSetting auxSettingCache;

    public DrawerPreviewStage(List<Op> ops, PaperSize paperSize, PrinterEnv printerEnv, String settingKey) {
        this(ops, paperSize.getWidth(), paperSize.getHeight(), printerEnv, settingKey);
    }

    public DrawerPreviewStage(List<Op> ops, double mmWidth, double mmHeight, PrinterEnv printerEnv, String settingKey){
        this.printerEnv = printerEnv;
        this.settingKey = settingKey;
        BorderPane root = new BorderPane();
        {
            MenuBar mbar = new MenuBar();
            {
                Menu file = new Menu("ファイル");
                MenuItem printItem = new MenuItem("印刷");
                printItem.setOnAction(event -> doPrint(ops));
                MenuItem closeItem = new MenuItem("閉じる");
                closeItem.setOnAction(event -> close());
                file.getItems().addAll(printItem, closeItem);
                mbar.getMenus().add(file);
            }
            if( printerEnv != null ){
                Menu setting = new Menu("印刷設定");
                MenuItem createItem = new MenuItem("印刷設定の新規作成");
                Menu settingNameItem = new Menu("既定の印刷設定を選択");
                Menu editSettingItem = new Menu("印刷設定の編集");
                createItem.setOnAction(event -> doCreate());
                printerSettingNames.addListener(new ListChangeListener<String>(){

                    @Override
                    public void onChanged(Change<? extends String> c) {
                        setupPrinterSettingMenu(c, settingNameItem);
                        setupEditSettingMenu(c, editSettingItem);
                    }
                });
                currentSettingName.addListener((obs, oldValue, newValue) -> {
                    for(MenuItem item: settingNameItem.getItems()){
                        String name = (String)item.getUserData();
                        if( Objects.equals(name, newValue) ){
                            RadioMenuItem radioItem = (RadioMenuItem)item;
                            radioItem.setSelected(true);
                            return;
                        }
                    }
                });
                setting.getItems().addAll(createItem, settingNameItem, editSettingItem);
                mbar.getMenus().add(setting);
            }
            root.setTop(mbar);
            if( printerEnv != null ){
                try {
                    List<String> names = printerEnv.listSettingNames();
                    printerSettingNames.setAll(names);
                    String defaultName = printerEnv.getDefaultSettingName(settingKey);
                    if( defaultName == null ){
                        defaultName = "";
                    }
                    currentSettingName.setValue(defaultName);
                } catch(Exception ex){
                    logger.error("Failed to fetch printer settings", ex);
                }
                currentSettingName.addListener((obs, oldValue, newValue) -> clearSettingCache());
            }
        }
        {
            VBox center = new VBox(4);
            center.setStyle("-fx-padding: 10");
            StackPane canvasBackground = new StackPane();
            canvasBackground.setStyle("-fx-background-color: white");
            PreviewCanvas canvas = new PreviewCanvas(ops, mmWidth, mmHeight);
            canvasBackground.getChildren().add(canvas);
            center.getChildren().addAll(canvasBackground);
            root.setCenter(center);
        }
        setScene(new Scene(root));
    }

    private void clearSettingCache(){
        devnamesCache = null;
        devmodeCache = null;
        auxSettingCache = null;
    }

    private void ensureSettingCache(){
        if( printerEnv == null ){
            return;
        }
        String settingName = currentSettingName.getValue();
        if( devnamesCache == null && devmodeCache == null && auxSettingCache == null ){
            try {
                devnamesCache = printerEnv.getDevnames(settingName);
                devmodeCache = printerEnv.getDevmode(settingName);
                auxSettingCache = printerEnv.getAuxSetting(settingName);
            } catch (IOException e) {
                logger.error("Failed to get printer setting data.", e);
                GuiUtil.alertException("印刷設定データの取得に失敗しました。", e);
            }
        } else {
            if( devnamesCache == null || devmodeCache == null || auxSettingCache == null ){
                logger.error("Inconsistent printer setting cache.");
                GuiUtil.alertError("Inconsistent printer setting cache.");
            }
        }
    }

    private void setupPrinterSettingMenu(ListChangeListener.Change<? extends String> change, Menu menu){
        ToggleGroup group = new ToggleGroup();
        RadioMenuItem cancelItem = new RadioMenuItem("既定印刷設定なし");
        cancelItem.setUserData("");
        List<RadioMenuItem> items = change.getList().stream().map(name -> {
            RadioMenuItem item = new RadioMenuItem(name);
            item.setUserData(name);
            return item;
        }).collect(Collectors.toList());
        List<RadioMenuItem> allItems = new ArrayList<>();
        allItems.add(cancelItem);
        allItems.addAll(items);
        allItems.forEach(item -> {
            item.setOnAction(event -> {
                String name = (String)item.getUserData();
                try {
                    printerEnv.saveDefaultSettingName(this.settingKey, name);
                    currentSettingName.setValue(name);
                } catch(IOException ex){
                    GuiUtil.alertException("既定の印刷設定の保存に失敗しました。", ex);
                }
            });
        });
        group.getToggles().addAll(allItems);
        menu.getItems().setAll(allItems);
    }

    private void setupEditSettingMenu(ListChangeListener.Change<? extends String> change, Menu menu){
        List<MenuItem> items = change.getList().stream().map(name -> {
            MenuItem item = new MenuItem(name);
            item.setOnAction(event -> doEditSetting(name));
            return item;
        }).collect(Collectors.toList());
        menu.getItems().setAll(items);
    }

    private void doEditSetting(String name){
        if( printerEnv == null || name == null || name.isEmpty() ){
            return;
        }
        try {
            byte[] devnames = printerEnv.getDevnames(name);
            byte[] devmode = printerEnv.getDevmode(name);
            AuxSetting auxSetting = printerEnv.getAuxSetting(name);
            EditPrinterSettingStage editStage = new EditPrinterSettingStage(printerEnv, name, devnames,
                    devmode, auxSetting);
            editStage.setCallback(new EditPrinterSettingStage.Callback() {
                @Override
                public void onEnter(String newName) {
                    if( !name.equals(newName) ){
                        try {
                            printerSettingNames.setAll(printerEnv.listSettingNames());
                        } catch (IOException e) {
                            logger.error("Failed to list printer setting names.", e);
                            GuiUtil.alertException("Failed to list printer setting names.", e);
                        }
                        String currentName = currentSettingName.getValue();
                        if( currentName != null && currentName.equals(name) ){
                            try {
                                printerEnv.saveDefaultSettingName(settingKey, newName);
                            } catch (IOException e) {
                                logger.error("Failed to save current printer setting name.", e);
                                GuiUtil.alertException("Failed to save current printer setting name.", e);
                            }
                            currentSettingName.setValue(newName);
                        }
                    }
                    editStage.close();
                }

                @Override
                public void onDelete() {
                    try {
                        printerSettingNames.setAll(printerEnv.listSettingNames());
                    } catch (IOException e) {
                        logger.error("Failed to list printer setting names.", e);
                        GuiUtil.alertException("Failed to list printer setting names.", e);
                    }
                    String currentName = currentSettingName.getValue();
                    if( currentName != null && currentName.equals(name) ){
                        try {
                            printerEnv.saveDefaultSettingName(settingKey, "");
                        } catch (IOException e) {
                            logger.error("Failed to save current printer setting name.", e);
                            GuiUtil.alertException("Failed to save current printer setting name.", e);
                        }
                        currentSettingName.setValue("");
                    }
                    editStage.close();
                }
            });
            editStage.showAndWait();
        } catch (IOException ex) {
            logger.error("Failed to get printer setting data.", ex);
            GuiUtil.alertException("Failed to get printer setting data.", ex);
        }
    }

    private void doCreate(){
        if( printerEnv == null ){
            GuiUtil.alertError("PrinterEnv が指定されていません。");
            return;
        }
        CreatePrinterSettingStage stage = new CreatePrinterSettingStage(printerEnv);
        stage.showAndWait();
        if( stage.isCreated() ){
            try {
                printerSettingNames.setAll(printerEnv.listSettingNames());
            } catch (IOException e) {
                logger.error("Failed to list printer setting names.", e);
                GuiUtil.alertException("Failed to list printer setting names.", e);
            }
        }
    }

    private void doPrint(List<Op> ops){
        ensureSettingCache();
        DrawerPrinter printer = new DrawerPrinter();
        printer.print(ops, devmodeCache, devnamesCache, auxSettingCache);
    }
}
