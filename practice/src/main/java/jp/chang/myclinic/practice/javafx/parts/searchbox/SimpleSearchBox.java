package jp.chang.myclinic.practice.javafx.parts.searchbox;

import javafx.application.Platform;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.practice.javafx.HandlerFX;
import jp.chang.myclinic.practice.javafx.parts.SelectableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class SimpleSearchBox<T> extends VBox {

    private static Logger logger = LoggerFactory.getLogger(SimpleSearchBox.class);

    private BasicSearchTextInput input;
    private SelectableList<T> result;

    public SimpleSearchBox(Function<String, CompletableFuture<List<T>>> searcher, Function<T, String> converter) {
        super(4);
        this.input = new BasicSearchTextInput();
        this.result = new SelectableList<>(converter);
        input.setOnSearchCallback(t -> {
            searcher.apply(t)
                    .thenAccept(list -> Platform.runLater(() -> result.setList(list)))
                    .exceptionally(HandlerFX::exceptionally);
        });
        getChildren().addAll(
                input,
                result
        );
    }

    public void setOnSelectCallback(Consumer<T> cb){
        result.setOnSelectCallback(cb);
    }

    public void setOnDoubleClickSelectCallback(Consumer<T> cb){
        result.setOnDoubleClickSelectCallback(cb);
    }

}
