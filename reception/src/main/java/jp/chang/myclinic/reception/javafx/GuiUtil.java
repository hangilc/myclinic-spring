package jp.chang.myclinic.reception.javafx;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class GuiUtil {

    public static void alertError(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    public static boolean confirm(String message){
        Optional<ButtonType> response = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK, ButtonType.CANCEL)
                .showAndWait();
        return  response.isPresent() && response.get() == ButtonType.OK;
    }

}
