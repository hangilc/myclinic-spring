package jp.chang.myclinic.medicalcheck;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.drawer.PaperSize;
import jp.chang.myclinic.medicalcheck.drawerpreview.DrawerPreviewDialog;
import jp.chang.myclinic.medicalcheck.importexam.BloodExamSpec;
import jp.chang.myclinic.medicalcheck.importexam.ImportExamDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class Main extends Application {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    private Form form = new Form();
    private Config config;
    private List<BloodExamSpec> bloodExamSpecs;

    public static void main(String[] args){
        Application.launch(Main.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("健康診断証明書");
        loadConfig();
        loadBloodExamSpecs();
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createMenu());
        Pane root = createRoot();
        root.getStylesheets().add("MedicalCheck.css");
        root.getStyleClass().add("root-pane");
        borderPane.setCenter(root);
        stage.setScene(new Scene(borderPane));
        stage.show();
    }

    private Node createMenu(){
        MenuBar mbar = new MenuBar();
        {
            Menu menu = new Menu("入力");
            {
                MenuItem item = new MenuItem("聴力所見なし");
                item.setOnAction(evt -> form.setHearingAbilityNormal());
                menu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("血液検査読み込み");
                item.setOnAction(evt -> doImportBloodExam());
                menu.getItems().add(item);
            }
            mbar.getMenus().add(menu);
        }
        return mbar;
    }

    private void doImportBloodExam(){
        ImportExamDialog dialog = new ImportExamDialog(bloodExamSpecs){
            @Override
            public void onEnter(List<String> input) {
                form.importExam(input);
                this.close();
            }
        };
        dialog.showAndWait();
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
            form.applyTo(data);
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
        data.clinicAddress1 = config.getPostalCode() + " " + config.getAddress();
        data.clinicAddress2 = "tel: " + config.getTel() + " fax: " + config.getFax();
        data.clinicName = config.getName();
        data.doctorName = config.getDoctorName();
    }

    private void loadConfig(){
        try(InputStream ins = new FileInputStream("config/application.yml")){
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            JsonNode node = mapper.readTree(ins).get("myclinic").get("clinic");
            config = mapper.convertValue(node, Config.class);
        } catch(Exception ex){
            logger.error("Failed to load application.yml", ex);
            System.exit(1);
        }
    }

    private void loadBloodExamSpecs(){
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try(InputStream ins = Main.class.getResourceAsStream("/blood-exams.yml")) {
            bloodExamSpecs = mapper.readValue(ins, new TypeReference<List<BloodExamSpec>>(){});
        } catch(Exception ex){
            logger.error("Failed to load blood exam sepcs.", ex);
            System.exit(1);
        }
    }

}
