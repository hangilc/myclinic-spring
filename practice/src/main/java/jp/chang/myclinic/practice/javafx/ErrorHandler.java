package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import jp.chang.myclinic.practice.lib.HttpErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorHandler {

    private static Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    public void handle(Throwable throwable){
        logger.error("http error", throwable);
        HttpErrorHandler errHandler = new HttpErrorHandler();
        String message = errHandler.getMessage(throwable);
        Platform.runLater(() -> GuiUtil.alertError(message));
    }

}
