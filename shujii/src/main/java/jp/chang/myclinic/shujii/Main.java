package jp.chang.myclinic.shujii;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.drawer.PaperSize;
import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.shujii.drawerpreview.DrawerPreviewDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Main extends Application {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }

    private ShujiiForm shujiiForm = new ShujiiForm();

    private static class Config {
        String doctorName = "";
        String clinicName = "";
        String clinicAddr = "";
        String clinicPhone = "";
        String clinicFax = "";
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("主治医意見書");
        VBox root = new VBox(4);
        root.setStyle("-fx-padding:10px");
        {
            HBox hbox = new HBox(4);
            Button previewButton = new Button("プレビュー");
            previewButton.setOnAction(evt -> doPreview());
            hbox.getChildren().addAll(previewButton);
            root.getChildren().add(hbox);
        }
        {
            root.getChildren().add(shujiiForm);
        }
        Config config = readConfig();
        if( config != null ){
            applyConfig(config);
        }
        stage.setScene(new Scene(root));
        stage.show();
    }
    // TODO: update drawer preview and save printer setting key
    private void doPreview() {
        try {
            ShujiiDrawer drawer = new ShujiiDrawer();
            drawer.setDoctorName(shujiiForm.getDoctorName());
            drawer.setClinicName(shujiiForm.getClinicName());
            drawer.setClinicAddr(shujiiForm.getClinicAddr());
            drawer.setClinicPhone(shujiiForm.getClinicPhone());
            drawer.setClinicFax(shujiiForm.getClinicFax());
            drawer.setDetail(shujiiForm.getDetail());
            DrawerPreviewDialog previewDialog = new DrawerPreviewDialog(new PrinterEnv());
            previewDialog.setContentSize(PaperSize.A4);
            previewDialog.setScaleFactor(0.6);
            previewDialog.setOps(drawer.getOps());
            previewDialog.showAndWait();
        } catch(Exception ex){
            logger.error("Failed to do preview.", ex);
            System.exit(1);
        }
    }

    private void applyConfig(Config config){
        shujiiForm.setDoctorName(config.doctorName);
        shujiiForm.setClinicName(config.clinicName);
        shujiiForm.setClinicAddr(config.clinicAddr);
        shujiiForm.setClinicPhone(config.clinicPhone);
        shujiiForm.setClinicFax(config.clinicFax);
    }

    private Config readConfig() {
        Path path = Paths.get("config", "application.yml");
        Yaml yaml = new Yaml();
        try (InputStream ins = new FileInputStream(path.toFile())) {
            Map map = yaml.load(ins);
            Map myclinicMap = (Map)map.get("myclinic");
            Map clinicMap = (Map)myclinicMap.get("clinic");
            Config config = new Config();
            config.doctorName = (String)clinicMap.get("doctor-name");
            config.clinicName = (String)clinicMap.get("name");
            String postalCode = (String)clinicMap.get("postal-code");
            String address = (String)clinicMap.get("address");
            config.clinicAddr = String.join(" ", postalCode, address);
            config.clinicPhone = (String)clinicMap.get("tel");
            config.clinicFax = (String)clinicMap.get("fax");
            return config;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            logger.error("Failed to open config file.", e);
            return null;
        }
    }
}
