package jp.chang.myclinic.utilfx;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletionException;

public class GuiUtil {

    private static Logger logger = LoggerFactory.getLogger(GuiUtil.class);

    public static void alertError(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    public static void alertInfo(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public static void alertException(String prefix, Throwable ex){
        if( prefix == null ){
            prefix = "";
        }
        if( !prefix.isEmpty() && !prefix.endsWith("\n") ){
            prefix += "\n";
        }
        if( ex instanceof CompletionException){
            ex = ex.getCause();
        }
        alertError(prefix + ex);
    }

    public static boolean confirm(String message){
        Optional<ButtonType> response = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK, ButtonType.CANCEL)
                .showAndWait();
        return  response.isPresent() && response.get() == ButtonType.OK;
    }

    public static Optional<String> askForString(String prompt, String defaultValue){
        TextInputDialog textInputDialog = new TextInputDialog(defaultValue);
        textInputDialog.setHeaderText(prompt);
        return textInputDialog.showAndWait();
    }

    public static  boolean copyToClipboard(String text){
        Map<DataFormat,Object> content = new HashMap<>();
        content.put(DataFormat.PLAIN_TEXT, text);
        Clipboard clipboard = Clipboard.getSystemClipboard();
        return clipboard.setContent(content);
    }

}
