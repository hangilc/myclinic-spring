package jp.chang.myclinic.practice.javafx.parts.drawerpreview;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import jp.chang.myclinic.drawer.printer.AuxSetting;
import jp.chang.myclinic.drawer.printer.DevnamesInfo;
import jp.chang.myclinic.drawer.printer.DrawerPrinter;
import jp.chang.myclinic.myclinicenv.printer.PrinterEnv;
import jp.chang.myclinic.practice.javafx.GuiUtil;
import jp.chang.myclinic.practice.javafx.parts.DispGrid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class EditSettingDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(EditSettingDialog.class);
    private PrinterEnv printerEnv;
    private String name;
    private byte[] devmode;
    private byte[] devnames;
    private AuxSetting auxSetting;

    private class PrinterPart extends VBox {
        private Text text;

        PrinterPart(){
            super(4);
            text = new Text("");
            Button modifyButton = new Button("変更");
            modifyButton.setOnAction(evt -> doModify());
            getChildren().addAll(
                    new TextFlow(text),
                    modifyButton
            );
            update();
        }

        final void update(){
            text.setText(createLabel());
        }

        private String createLabel(){
            DevnamesInfo devnamesInfo = new DevnamesInfo(devnames);
            return String.format("%s", devnamesInfo.getDevice());
        }

        private void doModify(){
            DrawerPrinter drawerPrinter = new DrawerPrinter();
            DrawerPrinter.DialogResult result = drawerPrinter.printDialog(devmode, devnames);
            if( result.ok ){
                byte[] newDevmode = result.devmodeData;
                byte[] newDevnames = result.devnamesData;
                try {
                    printerEnv.savePrintSetting(name, newDevnames, newDevmode, auxSetting);
                    EditSettingDialog.this.devmode = newDevmode;
                    EditSettingDialog.this.devnames = newDevnames;
                    update();
                } catch (IOException e) {
                    logger.error("Failed to save printer setting.", e);
                    GuiUtil.alertException("印刷設定の保存に失敗しました。", e);
                } catch (jp.chang.myclinic.drawer.printer.manager.PrinterEnv.SettingDirNotSuppliedException e) {
                    logger.error("No printer setting directory is not specified.", e);
                    GuiUtil.alertException("印刷設定ディレクトリーが設定されていません。", e);
                }
            }
        }
    }

    public EditSettingDialog(PrinterEnv printerEnv, String name, byte[] devmode, byte[] devnames, AuxSetting auxSetting) {
        this.printerEnv = printerEnv;
        this.name = name;
        this.devmode = devmode;
        this.devnames = devnames;
        this.auxSetting = auxSetting;
        setTitle("印刷設定の編集");
        BorderPane root = new BorderPane();
        root.setStyle("-fx-padding:10px");
        root.setCenter(createCenter());
        setScene(new Scene(root));
    }

    private Node createCenter(){
        VBox vbox = new VBox(4);
        vbox.getChildren().addAll(
                createMain()
        );
        return vbox;
    }

    private Node createMain(){
        DispGrid root = new DispGrid();
        root.addRow("名前：", new Label(name));
        root.addRow("プリンター：", new PrinterPart());
        return root;
    }

}
