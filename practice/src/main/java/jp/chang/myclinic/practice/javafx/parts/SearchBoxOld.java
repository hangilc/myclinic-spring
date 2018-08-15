package jp.chang.myclinic.practice.javafx.parts;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class SearchBoxOld<T> extends VBox {

    public interface InputBox {
        void setOnTextCallback(Consumer<String> cb);
        Node asNode();
    }

    private Function<String, CompletableFuture<List<T>>> searcher;
    private SearchResult<T> resultBox = new SearchResult<>();

    public SearchBoxOld(){
        this(s -> CompletableFuture.completedFuture(Collections.emptyList()),
                Object::toString);
    }

    public SearchBoxOld(Function<String, CompletableFuture<List<T>>> searcher,
                        Function<T, String> converter) {
        super(4);
        this.searcher = searcher;
        InputBox inputBox = createInputBox();
        inputBox.setOnTextCallback(t -> {
            getSearcher().apply(t)
                    .thenAccept(list -> Platform.runLater(() -> resultBox.setList(list)))
                    .exceptionally(HandlerFX::exceptionally);
        });
        resultBox.setConverter(converter);
        getChildren().addAll(inputBox.asNode(), resultBox);
    }

    private Function<String, CompletableFuture<List<T>>> getSearcher(){
        return searcher;
    }

    public void setSearcher(Function<String, CompletableFuture<List<T>>> searcher){
        this.searcher = searcher;
    }

    public void setConverter(Function<T, String> converter){
        resultBox.setConverter(converter);
    }

    public void setOnSelectCallback(Consumer<T> cb) {
        resultBox.setOnSelectCallback(cb);
    }

    protected InputBox createInputBox(){
        return new SearchInputBox();
    }
}
