package jp.chang.myclinic.reception.javafx;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.reception.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatientInfoStage extends Stage {

    private static Logger logger = LoggerFactory.getLogger(PatientInfoStage.class);

    public PatientInfoStage(PatientDTO patient){
        setTitle("患者情報");
        VBox vbox = new VBox(4);
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setPatient(patient);
        patientInfo.setPrefWidth(300);
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        Button editButton = new Button("編集");
        Button closeButton = new Button("閉じる");
        editButton.setOnAction(event -> doEdit(patient));
        closeButton.setOnAction(event -> close());
        hbox.getChildren().addAll(editButton, closeButton);
        vbox.getChildren().addAll(patientInfo, hbox);
        vbox.setStyle("-fx-padding: 10");
        Scene scene = new Scene(vbox);
        setScene(scene);
        sizeToScene();
    }

    private void doEdit(PatientDTO patient){
        Service.api.listHoken(patient.patientId)
                .thenAccept(hokenList -> {
                    System.out.println(hokenList);
                })
                .exceptionally(ex -> {
                    logger.error("Failed to fetch hoken list.", ex);
                    Alert alert = new Alert(Alert.AlertType.ERROR, "保険情報の取得に失敗しました。" + ex, ButtonType.OK);
                    alert.showAndWait();
                    return null;
                });
    }

}
