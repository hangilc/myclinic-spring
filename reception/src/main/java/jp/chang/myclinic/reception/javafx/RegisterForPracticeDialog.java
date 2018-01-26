package jp.chang.myclinic.reception.javafx;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.PatientDTO;

public class RegisterForPracticeDialog extends Stage {

    private boolean ok;

    public RegisterForPracticeDialog(PatientDTO patient){
        setTitle("診療受付の確認");
        VBox root = new VBox(4);
        {
            PatientInfo patientInfo = new PatientInfo(patient);
            root.getChildren().add(patientInfo);
        }
        root.getChildren().add(new Label("この患者の診療を受け付けますか？"));
        {
            HBox hbox = new HBox(4);
            hbox.setAlignment(Pos.CENTER_RIGHT);
            Button okButton = new Button("はい");
            Button noButton = new Button("いいえ");
            okButton.setOnAction(event -> {
                ok = true;
                close();
            });
            noButton.setOnAction(event -> close());
            hbox.getChildren().addAll(okButton, noButton);
            root.getChildren().add(hbox);
        }
        root.setStyle("-fx-padding: 10");
        setScene(new Scene(root));
        sizeToScene();
    }

    public boolean isOk() {
        return ok;
    }

}
