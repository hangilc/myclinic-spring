package jp.chang.myclinic.practice.javafx.parts;

import javafx.application.Platform;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.practice.javafx.HandlerFX;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class SearchBox<T> extends VBox {

    private SearchInputBox inputBox = new SearchInputBox();
    private SearchResult<T> resultBox = new SearchResult<>();
    private Function<String,CompletableFuture<List<T>>> searcher =
            t -> CompletableFuture.completedFuture(Collections.emptyList());

    public SearchBox(){
        inputBox.setOnTextCallback(t -> {
            searcher.apply(t)
                    .thenAccept(list -> Platform.runLater(() -> resultBox.setList(list)))
                    .exceptionally(HandlerFX::exceptionally);
        });
        getChildren().addAll(inputBox, resultBox);
    }

    public void setConverter(Function<T,String> converter) {
        resultBox.setConverter(converter);
    }

    public void setSearcher(Function<String,CompletableFuture<List<T>>> searcher) {
        this.searcher = searcher;
    }

    public void setOnSelectCallback(Consumer<T> cb){
        resultBox.setOnSelectCallback(cb);
    }

}
