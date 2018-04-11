package jp.chang.myclinic.pharma.javafx.drawerpreview;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.printer.AuxSetting;
import jp.chang.myclinic.drawer.printer.DrawerPrinter;
import jp.chang.myclinic.drawer.printer.manager.PrinterEnv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DrawerPreviewDialogEx extends Stage {

    private static Logger logger = LoggerFactory.getLogger(DrawerPreviewDialogEx.class);
    private static final String NO_PRINTER_SETTING = "(手動選択)";
    private ChoiceBox<String> settingChoice;
    private DrawerCanvas drawerCanvas;
    private PrinterEnv printerEnv;
    private List<List<Op>> pages = Collections.emptyList();
    private PageNav pageNav;
    private Parent root;
    private HBox commandsBox;

    public DrawerPreviewDialogEx(double width, double height, double scaleFactor) {
        this(null, width, height, scaleFactor);
    }

    public DrawerPreviewDialogEx(PrinterEnv printerEnv, double width, double height, double scaleFactor) {
        this.printerEnv = printerEnv;
        BorderPane main = new BorderPane();
        VBox root = new VBox(4);
        root.getStyleClass().add("drawer-preview-dialog");
        root.getChildren().addAll(
                createCommands(),
                createCanvas(width, height, scaleFactor)
        );
        main.setTop(createMenu());
        main.setCenter(root);
        setScene(new Scene(main));
        this.root = root;
    }

    public void addStylesheet(String path){
        root.getStylesheets().add(path);
    }

    public void setPages(List<List<Op>> pages){
        this.pages = pages;
        pageNav.set(pages.size());
        pageNav.trigger();
    }

    public void setSinglePage(List<Op> page){
        setPages(Collections.singletonList(page));
    }

    public void addToCommands(Node node){
        commandsBox.getChildren().add(node);
    }

    private void showPage(int page){
        if( page >= 0 && page < pages.size() ){
            List<Op> ops = pages.get(page);
            drawerCanvas.setOps(ops);
        }
    }

    private Node createCommands() {
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        Button printButton = new Button("印刷");
        printButton.setOnAction(evt -> doPrint());
        settingChoice = new ChoiceBox<>();
        settingChoice.getStyleClass().add("printer-setting-choice");
        updateSettingChoice();
        setPrinterSettingName(getDefaultPrinterSettingName());
        if( printerEnv == null ){
            settingChoice.setManaged(false);
            settingChoice.setVisible(false);
        }
        this.pageNav = new PageNav(){
            @Override
            protected void onPage(int page) {
                showPage(page);
            }
        };
        hbox.getChildren().addAll(printButton, settingChoice, pageNav);
        this.commandsBox = hbox;
        return hbox;
    }

    private Node createCanvas(double width, double height, double scaleFactor) {
        drawerCanvas = new DrawerCanvas();
        drawerCanvas.setContentSize(width, height);
        drawerCanvas.setScaleFactor(scaleFactor);
        return new ScrollPane(drawerCanvas);
    }

    private Node createMenu(){
        MenuBar mbar = new MenuBar();
        if( printerEnv != null ){
            {
                Menu menu = new Menu("ファイル");
                {
                    MenuItem item = new MenuItem("終了");
                    item.setOnAction(evt -> close());
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

    private void setPrinterSettingName(String printSettingName) {
        settingChoice.getSelectionModel().select(printSettingName);
        int index = settingChoice.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            settingChoice.getSelectionModel().select(NO_PRINTER_SETTING);
        }
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
        setPrinterSettingName(current);
    }

    protected String getDefaultPrinterSettingName() {
        return null;
    }

    protected void setDefaultPrinterSettingName(String newName){
    }

    private String getSettingName() {
        String setting = settingChoice.getSelectionModel().getSelectedItem();
        if (NO_PRINTER_SETTING.equals(setting)) {
            setting = null;
        }
        return setting;
    }

    private void doPrint(){
        String settingName = getSettingName();
        if (printerEnv != null && settingName != null) {
            printerEnv.print(pages, getSettingName());
            close();
        } else {
            DrawerPrinter printer = new DrawerPrinter();
            printer.printPages(pages);
            close();
        }

    }

    private List<Op> getOpsForTesting(){
        if( pages.size() > 0 ){
            return pages.get(0);
        } else {
            return Collections.emptyList();
        }
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
                    EditSettingDialog editSettingDialog = new EditSettingDialog(
                            printerEnv, name, devmode, devnames, auxSetting);
                    editSettingDialog.setTestPrintOps(getOpsForTesting());
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

    private void deleteOrphanDefault(String settingName){
        if( settingName != null && !settingName.isEmpty() && printerEnv != null ){
            String currentDefault = getDefaultPrinterSettingName();
            if( settingName.equals(currentDefault) ){
                setDefaultPrinterSettingName(null);
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
                    deleteOrphanDefault(name);
                }
            };
            dialog.setTestPrintOps(getOpsForTesting());
            dialog.show();
        } catch (Exception ex) {
            logger.error("Failed to list settings.", ex);
            GuiUtil.alertException("印刷設定のリストの取得に失敗しました。", ex);
        }
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
                        setPrinterSettingName(newDefaultSetting);
                    } catch (Exception e) {
                        GuiUtil.alertException("既定印刷設定名の保存に失敗しました。", e);
                    }
                }
            };
            dialog.setTestPrintOps(getOpsForTesting());
            dialog.showAndWait();
        } catch (IOException e) {
            GuiUtil.alertException("印刷設定のリストの取得に失敗しました。", e);
        }
    }

}
