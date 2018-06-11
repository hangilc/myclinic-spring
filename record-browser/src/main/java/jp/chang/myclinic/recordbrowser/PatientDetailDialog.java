package jp.chang.myclinic.recordbrowser;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.PatientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PatientDetailDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(PatientDetailDialog.class);

    PatientDetailDialog(PatientDTO patient) {
        setTitle(String.format("患者情報（%s%s）", patient.lastName, patient.firstName));
        VBox root = new VBox(4);
        root.getStyleClass().add("dialog");
        root.getStylesheets().add("Main.css");
        PatientInfo patientInfo = new PatientInfo(patient);
        patientInfo.getStyleClass().add("patient-info");
        root.getChildren().addAll(
                patientInfo,
                createCommands()
        );
        setScene(new Scene(root));
    }

    private Node createCommands(){
        HBox hbox = new HBox(4);
        Button closeButton = new Button("閉じる");
        closeButton.setOnAction(evt -> close());
        hbox.getChildren().addAll(
                closeButton
        );
        return hbox;
    }

}
