package jp.chang.myclinic.reception.drawerpreviewfx;

import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
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

import java.util.List;

public class DrawerPreviewStage extends Stage {

    private PrinterEnv printerEnv;
    private String settingKey;

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
            {
                Menu setting = new Menu("印刷設定");
                MenuItem createItem = new MenuItem("印刷設定の新規作成");
                Menu settingNameItem = new Menu("既定の印刷設定");
                createItem.setOnAction(event -> doCreate());
                settingNameItem.setOnShowing(event -> {

                });
                settingNameItem.setOnAction(event -> {

                });
                setting.getItems().addAll(createItem, settingNameItem);
                mbar.getMenus().add(setting);
            }
            root.setTop(mbar);
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
