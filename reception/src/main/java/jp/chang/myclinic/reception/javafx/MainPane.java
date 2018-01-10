package jp.chang.myclinic.reception.javafx;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.PaperSize;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.WqueueFullDTO;
import jp.chang.myclinic.reception.ReceptionEnv;
import jp.chang.myclinic.reception.Service;
import jp.chang.myclinic.reception.drawerpreviewfx.DrawerPreviewStage;
import jp.chang.myclinic.reception.receipt.ReceiptDrawer;
import jp.chang.myclinic.reception.receipt.ReceiptDrawerData;
import jp.chang.myclinic.reception.receipt.ReceiptDrawerDataCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MainPane extends VBox {
    private static Logger logger = LoggerFactory.getLogger(MainPane.class);

    private Button searchPatientButton = new Button("患者検索");
    private Button searchPaymentButton = new Button("会計検索");

    private TextField patientIdField = new TextField();
    private Button registerForPracticeButton = new Button("診療受付");
    private Button patientInfoButton = new Button("患者情報");

    private TableView<WqueueFullDTO> wqueueTable = new TableView<>();

    private Button refreshButton = new Button("更新");
    private Button cashierButton = new Button("会計");
    private Button deselectButton = new Button("選択解除");
    private Button deleteButton = new Button("削除");

    public MainPane(){
        setSpacing(4);
        {
            HBox hbox = new HBox(4);
            hbox.setAlignment(Pos.CENTER_LEFT);
            Button newPatientButton = new Button("新規患者");
            Button blankReceiptButton = new Button("領収書用紙");
            newPatientButton.setOnAction(event -> doNewPatient());
            searchPatientButton.setOnAction(event -> doSearchPatient());
            searchPaymentButton.setOnAction(event -> doSearchPayment());
            blankReceiptButton.setOnAction(event -> doBlankReceipt());
            hbox.getChildren().addAll(newPatientButton, searchPatientButton, searchPaymentButton, blankReceiptButton);
            getChildren().add(hbox);
        }
        {
            HBox hbox = new HBox(4);
            hbox.setAlignment(Pos.CENTER_LEFT);
            patientIdField.setPrefWidth(60);
            patientIdField.setMaxWidth(Control.USE_PREF_SIZE);
            patientIdField.setMinWidth(Control.USE_PREF_SIZE);
            patientIdField.setOnAction(event -> doPatientInfo());
            hbox.getChildren().addAll(new Label("患者番号"), patientIdField, registerForPracticeButton, patientInfoButton);
            getChildren().add(hbox);
        }
        {
            getChildren().add(wqueueTable);
        }
        {
            HBox hbox = new HBox(4);
            hbox.getChildren().addAll(refreshButton, cashierButton, deselectButton, deleteButton);
            getChildren().add(hbox);
        }
        patientInfoButton.setOnAction(event -> doPatientInfo());
    }

    private void doNewPatient(){
        EditPatientStage stage = new EditPatientStage(null);
        stage.setTitle("新規患者入力");
        stage.showAndWait();
        PatientDTO formValue = stage.getFormValue();
        if( formValue != null ){
            Service.api.enterPatient(formValue)
                    .thenCompose(patientId -> {
                        formValue.patientId = patientId;
                        return Service.api.listHoken(patientId);
                    })
                    .thenAccept(hokenList -> {
                        Platform.runLater(() -> {
                            PatientWithHokenStage patientWithHokenStage = new PatientWithHokenStage(formValue, hokenList);
                            patientWithHokenStage.showAndWait();
                        });
                    })
                    .exceptionally(ex -> {
                        logger.error("List hoken failed.", ex);
                        Platform.runLater(() -> GuiUtil.alertException(ex));
                        return null;
                    });
        }
    }

    private void doSearchPatient(){
        SearchPatientStage stage = new SearchPatientStage();
        stage.show();
    }

    private void doSearchPayment(){
        SearchPaymentStage stage = new SearchPaymentStage();
        stage.show();
    }

    private void doPatientInfo(){
        String text = patientIdField.getText().trim();
        if( text.isEmpty() ){
            return;
        }
        try {
            int patientId = Integer.parseInt(text);
            Service.api.getPatient(patientId)
                    .thenAccept(patient -> {
                        Platform.runLater(() -> {
                            patientIdField.setText("");
                            PatientInfoStage stage = new PatientInfoStage(patient);
                            stage.show();
                        });
                    })
                    .exceptionally(ex -> {
                        logger.error("failed to fetch patient", ex);
                        Alert alert = new Alert(Alert.AlertType.ERROR, "患者情報が取得できませんでした。" + ex, ButtonType.OK);
                        alert.showAndWait();
                        return null;
                    });
        } catch(NumberFormatException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, "患者番号の入力が適切でありません。", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void doBlankReceipt(){
        //ReceiptPreviewStage stage = new ReceiptPreviewStage();
        ReceiptDrawerDataCreator creator = new ReceiptDrawerDataCreator();
        creator.setClinicInfo(ReceptionEnv.INSTANCE.getClinicInfo());
        ReceiptDrawerData data = creator.getData();
        ReceiptDrawer receiptDrawer = new ReceiptDrawer(data);
        final List<Op> ops = receiptDrawer.getOps();
        DrawerPreviewStage stage = new DrawerPreviewStage(ops, PaperSize.A6_Landscape);
        stage.show();
    }
}
