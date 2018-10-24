package jp.chang.myclinic.reception.javafx;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.List;

public class CashierDialog extends Stage {

    private MeisaiDTO meisai;
    private PatientDTO patient;
    private VisitDTO visit;
    private ChargeDTO charge;

    public CashierDialog(MeisaiDTO meisai, PatientDTO patient, List<PaymentDTO> payments, VisitDTO visit,
                         ChargeDTO charge){
        this.meisai = meisai;
        this.patient = patient;
        this.visit = visit;
        this.charge = charge;
        setTitle(String.format("会計（%s）", patient.lastName + patient.firstName));
        VBox root = new VBox(4);
        root.getStyleClass().add("cashier-dialog");
        root.setStyle("-fx-padding: 10");
        root.getStylesheets().add("css/CashierDialog.css");
        root.getChildren().addAll(
                makePatientPart(patient),
                new MeisaiDetailPane(meisai),
                makeSummary(meisai, payments),
                makeButtons()
        );
        setScene(new Scene(root));
    }

    private Node makePatientPart(PatientDTO patient){
        VBox vbox = new VBox(4);
        Label nameLabel = new Label(patient.lastName + " " + patient.firstName);
        nameLabel.getStyleClass().add("patient-name");
        Label patientIdLabel = new Label("患者番号：" + patient.patientId);
        vbox.getChildren().addAll(nameLabel, patientIdLabel);
        return vbox;
    }

    private Node makeSummary(MeisaiDTO meisai, List<PaymentDTO> payments){
        VBox vbox = new VBox(4);
        Label totalTenLabel = new Label(String.format("総点：%,d", meisai.totalTen));
        Label futanWariLabel = new Label("負担割：" + meisai.futanWari);
        Label futanLabel = new Label(String.format("自己負担金額：%,d円", meisai.charge));
        vbox.getChildren().addAll(totalTenLabel, futanWariLabel, futanLabel);
        PaymentDTO lastPayment = null;
        if( payments.size() > 0 ){
            lastPayment = payments.get(0);
        }
        int chargeValue = charge.charge;
        if( lastPayment != null ){
            chargeValue -= payments.get(0).amount;
            vbox.getChildren().add(new Label(String.format("以前の支払い：%,d円", lastPayment.amount)));
        }
        Label chargeLabel = new Label(String.format("請求額：%,d円", chargeValue));
        chargeLabel.getStyleClass().add("charge-amount");
        vbox.getChildren().add(chargeLabel);
        return vbox;
    }

    private Node makeButtons(){
        HBox hbox = new HBox(4);
        Pane spacer = new Pane();
        Button printReceiptButton = new Button("領収書印刷");
        Button printManualReceiptButton = new Button("手書き領収書印刷");
        HBox.setHgrow(spacer, Priority.SOMETIMES);
        Button finishButton = new Button("終了");
        printReceiptButton.setOnAction(event ->
                ReceiptPreview.previewReceipt(meisai, patient, visit,
                        charge != null ? charge.charge : null));
        printManualReceiptButton.setOnAction(event -> ReceiptPreview.previewReceipt(patient, visit, null));
        finishButton.setOnAction(event -> doFinish());
        hbox.getChildren().addAll(printReceiptButton, printManualReceiptButton, spacer, finishButton);
        return hbox;
    }

    private void doFinish() {
        PaymentDTO payment = new PaymentDTO();
        payment.visitId = visit.visitId;
        payment.amount = charge.charge;
        payment.paytime = DateTimeUtil.toSqlDateTime(LocalDateTime.now());
        ReceptionService.finishCashier(payment, this::close);
    }

}
