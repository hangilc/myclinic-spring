package jp.chang.myclinic.hotline.javafx;

import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HandlerFX {

    private static Logger logger = LoggerFactory.getLogger(HandlerFX.class);

    public static <T> T exceptionally(Throwable t){
        logger.error("Error:", t);
        String message = ErrorMessageExtractor.extract(t);
        Platform.runLater(() -> GuiUtil.alertError(message));
        return null;
    }

    public static void alert(List<String> errors){
        GuiUtil.alertError(String.join("\n", errors));
    }

    public static void exception(String message, Throwable t){
        logger.error(message, t);
        GuiUtil.alertException(message, t);
    }
}
