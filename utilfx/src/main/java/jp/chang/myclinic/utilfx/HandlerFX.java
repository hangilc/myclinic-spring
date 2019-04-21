package jp.chang.myclinic.utilfx;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Function;

public class HandlerFX {

    private static Logger logger = LoggerFactory.getLogger(HandlerFX.class);

    private HandlerFX() {

    }

    public static <T> Function<Throwable, T> exceptionally(Window owner){
        return t -> {
            logger.error("Error: ", t);
            String message = t.toString();
            Platform.runLater(() -> AlertDialog.alert(message, owner));
            return null;
        };
    }

    public static <T> Function<Throwable, T> exceptionally(Node node){
        return exceptionally(node.getScene().getWindow());
    }

    public static <T> Function<Throwable, T> exceptionally(){
        return exceptionally((Window)null);
    }

}
