package jp.chang.myclinic.reception.javafx;

import javafx.event.ActionEvent;

public class EditPatientSceneController {
    public void onNewShahokokuhoClick(ActionEvent actionEvent) {
        EditShahokokuhoStage stage = new EditShahokokuhoStage();
        stage.show();
    }
}
