package jp.chang.myclinic.practice.javafx.parts.drawerpreview;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.printer.AuxSetting;
import jp.chang.myclinic.drawer.printer.DrawerPrinter;
import jp.chang.myclinic.myclinicenv.printer.PrinterEnv;
import jp.chang.myclinic.practice.javafx.GuiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DrawerPreviewDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(DrawerPreviewDialog.class);

    private static final String NO_PRINTER_SETTING = "(手動選択)";
    private ChoiceBox<String> settingChoice;
    private DrawerCanvas drawerCanvas;
    private List<Op> ops;
    private PrinterEnv printerEnv;

    public DrawerPreviewDialog() {
        this(null);
    }

    public DrawerPreviewDialog(PrinterEnv printerEnv){
        this.printerEnv = printerEnv;
        BorderPane main = new BorderPane();
        VBox root = new VBox(4);
        root.getStyleClass().add("drawer-preview-dialog");
        root.getStylesheets().add("css/Practice.css");
        root.getChildren().addAll(
                createCommands(),
                createCanvas()
        );
        main.setTop(createMenu());
        main.setCenter(root);
        setScene(new Scene(main));
    }

    public void setContentSize(double mmWidth, double mmHeight) {
        drawerCanvas.setContentSize(mmWidth, mmHeight);
    }

    public void setScaleFactor(double scaleFactor) {
        drawerCanvas.setScaleFactor(scaleFactor);
    }

    public void setOps(List<Op> ops) {
        this.ops = ops;
        drawerCanvas.setOps(ops);
    }

    public void setPrinterEnv(PrinterEnv printerEnv) {
        this.printerEnv = printerEnv;
        updateSettingChoice();
    }

    public void setPrintSettingName(String printSettingName) {
        settingChoice.getSelectionModel().select(printSettingName);
        if( settingChoice.getSelectionModel().getSelectedItem() == null ){
            settingChoice.getSelectionModel().select(NO_PRINTER_SETTING);
        }
    }

    private Node createMenu(){
        MenuBar mbar = new MenuBar();
        {
            {
                Menu menu = new Menu("ファイル");
                {
                    MenuItem item = new MenuItem("終了");
                    item.setOnAction(evt -> DrawerPreviewDialog.this.close());
                    menu.getItems().add(item);
                }
                mbar.getMenus().add(menu);
            }
            {
                Menu menu = new Menu("印刷設定");
                {
                    MenuItem item = new MenuItem("新規印刷設定");
                    item.setOnAction(evt -> doNewSetting());
                    menu.getItems().add(item);
                }
                mbar.getMenus().add(menu);
            }
        }
        return mbar;
    }

    private void doNewSetting(){
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("新規印刷設定の名前");
        textInputDialog.setHeaderText(null);
        textInputDialog.setContentText("新規印刷設定の名前：");
        Optional<String> result = textInputDialog.showAndWait();
        if( result.isPresent() ){
            String name = result.get();
            if( printerEnv.settingExists(name) ){
                GuiUtil.alertError(name + " という設定名は既に存在します。");
                return;
            }
            System.out.println("NAME: " + name);
            DrawerPrinter drawerPrinter = new DrawerPrinter();
            DrawerPrinter.DialogResult dialogResult = drawerPrinter.printDialog(null, null);
            if( dialogResult.ok ){
                byte[] devmode = dialogResult.devmodeData;
                byte[] devnames = dialogResult.devnamesData;
                try {
                    printerEnv.savePrintSetting(name, devnames, devmode, new AuxSetting());
                } catch (IOException e) {
                    logger.error("Failed to save printer settng.", e);
                    GuiUtil.alertException("印刷設定の保存に失敗しました。", e);
                } catch (jp.chang.myclinic.drawer.printer.manager.PrinterEnv.SettingDirNotSuppliedException e) {
                    GuiUtil.alertError("Printer setting directory is not specified.");
                }
            }
        }
    }

    private Node createCommands(){
        HBox hbox = new HBox(4);
        Button printButton = new Button("印刷");
        printButton.setOnAction(evt -> doPrint());
        settingChoice = new ChoiceBox<>();
        settingChoice.getStyleClass().add("printer-setting-choice");
        updateSettingChoice();
        hbox.getChildren().addAll(printButton, settingChoice);
        return hbox;
    }

    private void updateSettingChoice(){
        String current = settingChoice.getSelectionModel().getSelectedItem();
        settingChoice.getItems().setAll(NO_PRINTER_SETTING);
        if( printerEnv != null ){
            try {
                for(String name: printerEnv.listSettingNames()){
                    settingChoice.getItems().add(name);
                }
            } catch (IOException e) {
                logger.error("failed to list printer settings.", e);
                GuiUtil.alertException("印刷設定のリストの取得に失敗しました。", e);
            }
        }
        setPrintSettingName(current);
    }

    private String getSettingName(){
        String setting = settingChoice.getSelectionModel().getSelectedItem();
        if( NO_PRINTER_SETTING.equals(setting) ){
            setting = null;
        }
        return setting;
    }

    private Node createCanvas() {
        drawerCanvas = new DrawerCanvas();
        return new ScrollPane(drawerCanvas);
    }

    private void doPrint(){
        if( printerEnv != null ){
            try {
                printerEnv.print(Collections.singletonList(ops), getSettingName());
                close();
            } catch (IOException e) {
                logger.error("Printing error.", e);
                GuiUtil.alertException("印刷に失敗しました。", e);
            }
        }
    }
}
