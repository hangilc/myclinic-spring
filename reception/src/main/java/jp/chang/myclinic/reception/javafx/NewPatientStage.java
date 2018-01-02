package jp.chang.myclinic.reception.javafx;

import javafx.application.Platform;
import javafx.geometry.Insets;
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

import java.util.List;

public class NewPatientStage extends Stage {
    private static Logger logger = LoggerFactory.getLogger(NewPatientStage.class);

    private PatientForm patientForm = new PatientForm();
    private PatientDTO patient;

    public NewPatientStage(){
        VBox vbox = new VBox(10);
        vbox.getChildren().add(patientForm);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        {
            HBox hbox = new HBox(4);
            hbox.setAlignment(Pos.CENTER_RIGHT);
            hbox.getChildren().addAll(enterButton, cancelButton);
            vbox.getChildren().add(hbox);
        }
        enterButton.setOnAction(event -> {
            patient = new PatientDTO();
            List<String> errs = patientForm.save(patient);
            if( errs.size() > 0 ){
                Alert alert = new Alert(Alert.AlertType.ERROR, String.join("\n", errs), ButtonType.OK);
                alert.showAndWait();
                patient = null;
            } else {
                Service.api.enterPatient(patient)
                        .thenAccept(patientId -> {
                            patient.patientId = patientId;
                            Platform.runLater(this::close);
                        })
                        .exceptionally(ex -> {
                            logger.error("failed to save patient", ex);
                            String message = "新規患者の入力に失敗しました。\n" + ex.toString();
                            Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
                            alert.showAndWait();
                            return null;
                        });
            }
        });
        cancelButton.setOnAction(event -> {
            patient = null;
            close();
        });
        vbox.setPadding(new Insets(10, 10, 10, 10));
        Scene scene = new Scene(vbox);
        setScene(scene);
    }

    public PatientDTO getPatient(){
        return patient;
    }

}
