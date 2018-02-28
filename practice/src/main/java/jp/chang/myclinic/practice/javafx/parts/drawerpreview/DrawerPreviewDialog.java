package jp.chang.myclinic.practice.javafx.parts.drawerpreview;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

    private ScrollPane scrollPane;
    private DrawerCanvas drawerCanvas;
    private List<Op> ops;
    private PrinterEnv printerEnv;
    private String printSettingName;

    public DrawerPreviewDialog() {
        VBox root = new VBox(4);
        root.getStyleClass().add("drawer-preview-dialog");
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
    }

    public void setPrintSettingName(String printSettingName) {
        this.printSettingName = printSettingName;
    }

    private Node createCommands(){
        HBox hbox = new HBox(4);
        Button printButton = new Button("印刷");
        printButton.setOnAction(evt -> doPrint());
        hbox.getChildren().addAll(printButton);
        return hbox;
    }

    private Node createCanvas() {
        drawerCanvas = new DrawerCanvas();
        scrollPane = new ScrollPane(drawerCanvas);
        return scrollPane;
    }

    private void doPrint(){
        if( printerEnv != null ){
            try {
                printerEnv.print(Collections.singletonList(ops), printSettingName);
                close();
            } catch (IOException e) {
                logger.error("Printing error.", e);
                GuiUtil.alertException("印刷に失敗しました。", e);
            }
        }
    }
}
