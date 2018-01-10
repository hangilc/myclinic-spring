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

import java.util.List;

public class DrawerPreviewStage extends Stage {

    public DrawerPreviewStage(List<Op> ops, PaperSize paperSize) {
        this(ops, paperSize.getWidth(), paperSize.getHeight());
    }

    public DrawerPreviewStage(List<Op> ops, double mmWidth, double mmHeight){
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
                Menu settingNameItem = new Menu("既定の印刷設定");
                settingNameItem.setOnShowing(event -> {

                });
                settingNameItem.setOnAction(event -> {

                });
                setting.getItems().addAll(settingNameItem);
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

    private void doPrint(List<Op> ops){
        DrawerPrinter printer = new DrawerPrinter();
        printer.print(ops);
    }
}
