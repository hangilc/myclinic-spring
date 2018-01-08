package jp.chang.myclinic.reception.javafx;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.reception.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchPaymentStage extends Stage {
    private static Logger logger = LoggerFactory.getLogger(SearchPaymentStage.class);

    private PaymentTable paymentTable = new PaymentTable();

    public SearchPaymentStage(){
        VBox root = new VBox(4);
        {
            Button recentPaymentButton = new Button("最近の会計");
            recentPaymentButton.setOnAction(event -> doRecentPayment());
            root.getChildren().add(recentPaymentButton);
        }
        {
            HBox hbox = new HBox(4);
            hbox.setAlignment(Pos.CENTER_LEFT);
            TextField patientIdInput = new TextField();
            patientIdInput.setPrefWidth(100);
            Button searchButton = new Button("検索");
            hbox.getChildren().addAll(new Label("患者番号"), patientIdInput, searchButton);
            root.getChildren().add(hbox);
        }
        {
            paymentTable.setPrefWidth(460);
            paymentTable.setPrefHeight(226);
            root.getChildren().add(paymentTable);
        }
        {
            HBox hbox = new HBox(4);
            hbox.setAlignment(Pos.CENTER_LEFT);
            Button printReceiptAgainButton = new Button("領収書再発行");
            Button rcptDetailButton = new Button("明細情報表示");
            rcptDetailButton.setOnAction(event -> paymentTable.printColumnWidths());
            hbox.getChildren().addAll(printReceiptAgainButton, rcptDetailButton);
            root.getChildren().add(hbox);
        }
        root.setStyle("-fx-padding: 10");
        setScene(new Scene(root));
        sizeToScene();
    }

    private void doRecentPayment(){
        Service.api.listRecentPayment(30)
                .thenAccept(list -> {
                    paymentTable.setRows(list);
                })
                .exceptionally(ex -> {
                    logger.error("Failed to list recent payments.", ex);
                    Platform.runLater(() -> GuiUtil.alertException(ex));
                    return null;
                });

    }
}
