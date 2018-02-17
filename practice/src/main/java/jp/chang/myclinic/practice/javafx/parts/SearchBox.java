package jp.chang.myclinic.practice.javafx.parts;

import javafx.application.Platform;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.practice.javafx.HandlerFX;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class SearchBox<T> extends VBox {

    private SearchResult<T> resultBox = new SearchResult<>();

    public SearchBox(Function<String, CompletableFuture<List<T>>> searcher,
                     Function<T, String> converter) {
        super(4);
        SearchInputBox inputBox = new SearchInputBox();
        inputBox.setOnTextCallback(t -> {
            searcher.apply(t)
                    .thenAccept(list -> Platform.runLater(() -> resultBox.setList(list)))
                    .exceptionally(HandlerFX::exceptionally);
        });
        resultBox.setConverter(converter);
        getChildren().addAll(inputBox, resultBox);
    }

    public void setOnSelectCallback(Consumer<T> cb) {
        resultBox.setOnSelectCallback(cb);
    }

}
