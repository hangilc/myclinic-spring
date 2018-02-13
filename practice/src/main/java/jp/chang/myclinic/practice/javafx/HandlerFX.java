package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import jp.chang.myclinic.practice.lib.HttpErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class HandlerFX {

    private static Logger logger = LoggerFactory.getLogger(HandlerFX.class);

    public static <T> CompletableFuture<T> exceptionally(Throwable t){
        logger.error("Error:", t);
        HttpErrorHandler handler = new HttpErrorHandler();
        String message = handler.getMessage(t);
        Platform.runLater(() -> GuiUtil.alertError(message));
        return null;
    }

}
