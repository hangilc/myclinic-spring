package jp.chang.myclinic.pharma.javafx.drawerpreview;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.PaperSize;
import jp.chang.myclinic.drawer.printer.AuxSetting;
import jp.chang.myclinic.drawer.printer.DrawerPrinter;
import jp.chang.myclinic.drawer.printer.manager.PrinterEnv;
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

    public DrawerPreviewDialog(PrinterEnv printerEnv) {
        this.printerEnv = printerEnv;
        BorderPane main = new BorderPane();
        VBox root = new VBox(4);
        root.setStyle("-fx-padding:10px");
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

    public void setContentSize(PaperSize paperSize) {
        setContentSize(paperSize.getWidth(), paperSize.getHeight());
    }

    public void setScaleFactor(double scaleFactor) {
        drawerCanvas.setScaleFactor(scaleFactor);
        this.sizeToScene();
    }

    public void setOps(List<Op> ops) {
        this.ops = ops;
        drawerCanvas.setOps(ops);
    }

    private List<Op> getOps() {
        return ops;
    }

    public void setPrinterEnv(PrinterEnv printerEnv) {
        this.printerEnv = printerEnv;
        updateSettingChoice();
    }

    public void setPrintSettingName(String printSettingName) {
        settingChoice.getSelectionModel().select(printSettingName);
        if (settingChoice.getSelectionModel().getSelectedItem() == null) {
            settingChoice.getSelectionModel().select(NO_PRINTER_SETTING);
        }
    }

    private Node createMenu() {
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
                {
                    MenuItem item = new MenuItem("印刷設定一覧");
                    item.setOnAction(evt -> doListSetting());
                    menu.getItems().add(item);
                }
                {
                    MenuItem item = new MenuItem("既定の印刷設定");
                    item.setOnAction(evt -> doDefaultSetting());
                    menu.getItems().add(item);
                }
                mbar.getMenus().add(menu);
            }
        }
        return mbar;
    }

    private void doNewSetting() {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("新規印刷設定の名前");
        textInputDialog.setHeaderText(null);
        textInputDialog.setContentText("新規印刷設定の名前：");
        Optional<String> result = textInputDialog.showAndWait();
        if (result.isPresent()) {
            String name = result.get();
            if (printerEnv.settingExists(name)) {
                GuiUtil.alertError(name + " という設定名は既に存在します。");
                return;
            }
            DrawerPrinter drawerPrinter = new DrawerPrinter();
            DrawerPrinter.DialogResult dialogResult = drawerPrinter.printDialog(null, null);
            if (dialogResult.ok) {
                byte[] devmode = dialogResult.devmodeData;
                byte[] devnames = dialogResult.devnamesData;
                AuxSetting auxSetting = new AuxSetting();
                try {
                    printerEnv.saveSetting(name, devnames, devmode, auxSetting);
                    updateSettingChoice();
                    EditSettingDialog editSettingDialog = new EditSettingDialog(printerEnv, name, devmode, devnames, auxSetting);
                    editSettingDialog.setTestPrintOps(getOps());
                    editSettingDialog.showAndWait();
                } catch (IOException e) {
                    logger.error("Failed to save printer settng.", e);
                    GuiUtil.alertException("印刷設定の保存に失敗しました。", e);
                } catch (PrinterEnv.SettingDirNotSuppliedException e) {
                    GuiUtil.alertError("Printer setting directory is not specified.");
                }
            }
        }
    }

    private void doListSetting() {
        try {
            List<String> names = printerEnv.listNames();
            ListSettingDialog dialog = new ListSettingDialog(names, printerEnv){
                @Override
                protected void onDelete(String name) {
                    updateSettingChoice();
                }
            };
            dialog.setTestPrintOps(getOps());
            dialog.show();
        } catch (Exception ex) {
            logger.error("Failed to list settings.", ex);
            GuiUtil.alertException("印刷設定のリストの取得に失敗しました。", ex);
        }
    }

    private Node createCommands() {
        HBox hbox = new HBox(4);
        Button printButton = new Button("印刷");
        printButton.setOnAction(evt -> doPrint());
        settingChoice = new ChoiceBox<>();
        settingChoice.getStyleClass().add("printer-setting-choice");
        updateSettingChoice();
        setPrintSettingName(getDefaultPrinterSettingName());
        hbox.getChildren().addAll(printButton, settingChoice);
        return hbox;
    }

    private void updateSettingChoice() {
        String current = settingChoice.getSelectionModel().getSelectedItem();
        settingChoice.getItems().setAll(NO_PRINTER_SETTING);
        if (printerEnv != null) {
            try {
                for (String name : printerEnv.listNames()) {
                    settingChoice.getItems().add(name);
                }
            } catch (IOException e) {
                logger.error("failed to list printer settings.", e);
                GuiUtil.alertException("印刷設定のリストの取得に失敗しました。", e);
            }
        }
        setPrintSettingName(current);
    }

    private String getSettingName() {
        String setting = settingChoice.getSelectionModel().getSelectedItem();
        if (NO_PRINTER_SETTING.equals(setting)) {
            setting = null;
        }
        return setting;
    }

    private Node createCanvas() {
        drawerCanvas = new DrawerCanvas();
        return new ScrollPane(drawerCanvas);
    }

    private void doPrint() {
        if (printerEnv != null) {
            printerEnv.print(Collections.singletonList(ops), getSettingName());
            close();
        } else {
            DrawerPrinter printer = new DrawerPrinter();
            printer.print(ops);
            close();
        }
    }

    protected String getDefaultPrinterSettingName() {
        return null;
    }

    protected void setDefaultPrinterSettingName(String newName){
    }

    private void doDefaultSetting() {
        String defaultSetting = getDefaultPrinterSettingName();
        try {
            List<String> names = printerEnv.listNames();
            SelectDefaultSettingDialog dialog = new SelectDefaultSettingDialog(defaultSetting, names, printerEnv) {
                @Override
                protected void onChange(String newDefaultSetting) {
                    try {
                        setDefaultPrinterSettingName(newDefaultSetting);
                        setPrintSettingName(newDefaultSetting);
                    } catch (Exception e) {
                        GuiUtil.alertException("既定印刷設定名の保存に失敗しました。", e);
                    }
                }
            };
            dialog.setTestPrintOps(ops);
            dialog.showAndWait();
        } catch (IOException e) {
            GuiUtil.alertException("印刷設定のリストの取得に失敗しました。", e);
        }
    }
}
