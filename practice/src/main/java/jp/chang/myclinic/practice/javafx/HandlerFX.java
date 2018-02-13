package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import jp.chang.myclinic.practice.lib.ErrorMessageExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerFX {

    private static Logger logger = LoggerFactory.getLogger(HandlerFX.class);

    public static <T> T exceptionally(Throwable t){
        logger.error("Error:", t);
        String message = ErrorMessageExtractor.extract(t);
        Platform.runLater(() -> GuiUtil.alertError(message));
        return null;
    }

}
