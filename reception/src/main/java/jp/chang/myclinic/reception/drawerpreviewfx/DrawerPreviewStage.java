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
import jp.chang.myclinic.drawer.printer.DrawerPrinter;
import jp.chang.myclinic.myclinicenv.printer.PrinterEnv;
import jp.chang.myclinic.reception.drawerpreviewfx.create.CreatePrinterSettingStage;
import jp.chang.myclinic.reception.javafx.GuiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                Menu settingNameItem = new Menu("既定の印刷設定");
                createItem.setOnAction(event -> doCreate());
                printerSettingNames.addListener(new ListChangeListener<String>(){

                    @Override
                    public void onChanged(Change<? extends String> c) {
                        setupPrinterSettingMenu(c, settingNameItem);
                    }
                });
                currentSettingName.addListener((obs, oldValue, newValue) -> {
                    System.out.println("setting changed to " + newValue);
                    for(MenuItem item: settingNameItem.getItems()){
                        String name = (String)item.getUserData();
                        if( Objects.equals(name, newValue) ){
                            RadioMenuItem radioItem = (RadioMenuItem)item;
                            radioItem.setSelected(true);
                            return;
                        }
                    }
                });
                setting.getItems().addAll(createItem, settingNameItem);
                mbar.getMenus().add(setting);
            }
            root.setTop(mbar);
            if( printerEnv != null ){
                try {
                    List<String> names = printerEnv.listSettingNames();
                    printerSettingNames.setAll(names);
                } catch(Exception ex){
                    logger.error("Failed to fetch printer setting names", ex);
                }
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

    private void setupPrinterSettingMenu(ListChangeListener.Change<? extends String> change, Menu menu){
        ToggleGroup group = new ToggleGroup();
        RadioMenuItem cancelItem = new RadioMenuItem("既定印刷設定なし");
        cancelItem.setUserData(null);
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
                currentSettingName.setValue(name);
            });
        });
        group.getToggles().addAll(allItems);
        menu.getItems().setAll(allItems);
    }

    private void doCreate(){
        if( printerEnv == null ){
            GuiUtil.alertError("PrinterEnv が指定されていません。");
            return;
        }
        CreatePrinterSettingStage stage = new CreatePrinterSettingStage();
        stage.showAndWait();
    }

    private void doPrint(List<Op> ops){
        DrawerPrinter printer = new DrawerPrinter();
        printer.print(ops);
    }
}
