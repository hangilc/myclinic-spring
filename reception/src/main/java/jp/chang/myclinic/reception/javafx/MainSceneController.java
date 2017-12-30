package jp.chang.myclinic.reception.javafx;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class MainSceneController {
    public void onNewPatientClick(ActionEvent actionEvent) {
        Stage stage = new EditPatientStage();
        stage.show();
    }

    public void onSearchPatientClick(ActionEvent actionEvent) {
        SearchPatientStage stage = new SearchPatientStage();
        stage.show();
    }

    public void onSearchPaymentClick(ActionEvent actionEvent) {
        SearchPaymentStage stage = new SearchPaymentStage();
        stage.show();
    }

    public void onReceiptPreviewClick(ActionEvent actionEvent) {
        ReceiptPreviewStage stage = new ReceiptPreviewStage();
        stage.show();
    }
}
