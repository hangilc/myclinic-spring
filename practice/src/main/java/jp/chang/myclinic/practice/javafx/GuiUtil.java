package jp.chang.myclinic.practice.javafx;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.HttpException;

import java.util.Optional;
import java.util.concurrent.CompletionException;

public class GuiUtil {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ErrorBody {
        public String message;
    }

    private static Logger logger = LoggerFactory.getLogger(GuiUtil.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void alertError(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
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
        if( ex instanceof HttpException){
            HttpException httpException = (HttpException)ex;
            try {
                ErrorBody errorBody = objectMapper.readValue(httpException.response().errorBody().byteStream(), ErrorBody.class);
                String message = prefix + errorBody.message;
                alertError(message);
            } catch(Exception exception){
                logger.error("Failed to get message from http error.", exception);
                alertError(prefix);
            }
        } else {
            alertError(prefix + ex);
        }
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
}
