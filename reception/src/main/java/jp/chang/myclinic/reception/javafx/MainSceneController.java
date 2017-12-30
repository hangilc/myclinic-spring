package jp.chang.myclinic.reception.javafx;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class MainSceneController {
    public void onNewPatientClick(ActionEvent actionEvent) {
        Stage stage = new EditPatientStage();
        stage.show();
    }
}
