package jp.chang.myclinic.medicalcheck;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.drawer.PaperSize;
import jp.chang.myclinic.medicalcheck.drawerpreview.DrawerPreviewDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    private Form form = new Form();
    private Config config;

    public static void main(String[] args){
        Application.launch(Main.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("健康診断証明書");
        config = new Config();
        config.load();
        Pane root = createRoot();
        root.setStyle("-fx-padding:10px");
        stage.setScene(new Scene(root));
        stage.show();
    }

    private Pane createRoot(){
        VBox root = new VBox(4);
        {
            HBox hbox = new HBox(4);
            Button previewButton = new Button("プレビュー");
            previewButton.setOnAction(evt -> doPreview());
            hbox.getChildren().addAll(previewButton);
            root.getChildren().add(hbox);
        }
        {
            root.getChildren().addAll(form);
        }
        return root;
    }

    private void doPreview(){
        try {
            Data data = new Data();
            Drawer drawer = new Drawer();
            setupClinicInfo(data);
            DrawerPreviewDialog previewDialog = new DrawerPreviewDialog(null);
            previewDialog.setContentSize(PaperSize.A4);
            previewDialog.setScaleFactor(0.6);
            previewDialog.setOps(drawer.render(data));
            previewDialog.showAndWait();
        } catch(Exception ex){
            logger.error("Failed to do preview.", ex);
            System.exit(1);
        }
    }

    private void setupClinicInfo(Data data){
        data.address1 = config.clinicAddr;
        data.address2 = config.clinicPhone + " " + config.clinicFax;
        data.clinicName = config.clinicName;
        data.doctorName = config.doctorName;
    }

}
