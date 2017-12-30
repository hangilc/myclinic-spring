package jp.chang.myclinic.reception.javafx;

import javafx.event.ActionEvent;

public class EditPatientSceneController {
    public void onNewShahokokuhoClick(ActionEvent actionEvent) {
        EditShahokokuhoStage stage = new EditShahokokuhoStage();
        stage.show();
    }

    public void onNewKoukikoureiClick(ActionEvent actionEvent) {
        EditKoureiStage stage = new EditKoureiStage();
        stage.show();
    }

    public void onNewKouhiClick(ActionEvent actionEvent) {
        EditKouhiStage stage = new EditKouhiStage();
        stage.show();
    }
}
