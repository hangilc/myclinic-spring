package jp.chang.myclinic.practice.javafx.shohousen;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.drawer.PaperSize;
import jp.chang.myclinic.dto.ClinicInfoDTO;
import jp.chang.myclinic.myclinicenv.printer.PrinterEnv;
import jp.chang.myclinic.practice.PracticeEnv;
import jp.chang.myclinic.practice.javafx.GuiUtil;
import jp.chang.myclinic.practice.javafx.parts.drawerpreview.DrawerPreviewDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShohousenDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(ShohousenDialog.class);

    public ShohousenDialog() {
        VBox root = new VBox(4);
        root.setStyle("-fx-padding:10px");
        root.getChildren().addAll(
                createCommands()
        );
        setScene(new Scene(root));
    }

    private Node createCommands(){
        HBox hbox = new HBox(4);
        Button previewButton = new Button("プレビュー");
        previewButton.setOnAction(evt -> doPreview());
        hbox.getChildren().addAll(
                previewButton
        );
        return hbox;
    }

    private void doPreview(){
        try {
            PrinterEnv printerEnv = PracticeEnv.INSTANCE.getMyclinicEnv().getPrinterEnv();
            DrawerPreviewDialog previewDialog = new DrawerPreviewDialog(printerEnv);
            previewDialog.setContentSize(PaperSize.A5);
            ShohousenDrawer drawer = new ShohousenDrawer();
            ClinicInfoDTO clinicInfo = PracticeEnv.INSTANCE.getClinicInfo();
            ShohousenUtil.setClinicInfo(drawer, clinicInfo);
            previewDialog.setOps(drawer.getOps());
            previewDialog.showAndWait();
        } catch(Exception ex){
            logger.error("Failed to preview shohousen.", ex);
            GuiUtil.alertException("処方箋のプレビューに失敗しました。", ex);
        }
    }

}
