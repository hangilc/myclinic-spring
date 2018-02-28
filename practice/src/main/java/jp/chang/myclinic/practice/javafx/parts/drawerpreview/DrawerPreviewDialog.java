package jp.chang.myclinic.practice.javafx.parts.drawerpreview;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.myclinicenv.printer.PrinterEnv;
import jp.chang.myclinic.practice.javafx.GuiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

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
        VBox root = new VBox(4);
        root.getStyleClass().add("drawer-preview-dialog");
        root.getStylesheets().add("css/Practice.css");
        root.getChildren().addAll(
                createCommands(),
                createCanvas()
        );
        setScene(new Scene(root));
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
