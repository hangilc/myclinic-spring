package jp.chang.myclinic.practice.javafx.parts.searchbox;

import javafx.application.Platform;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.practice.javafx.parts.SelectableList;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class SimpleSearchBox<T> extends VBox {

    private BasicSearchTextInput input;
    private SelectableList<T> result;

    public SimpleSearchBox(Function<String, CompletableFuture<List<T>>> searcher, Function<T, String> converter) {
        super(4);
        this.input = new BasicSearchTextInput();
        this.result = new SelectableList<>(converter);
        this.result.getStyleClass().add("search-result");
        input.setOnSearchCallback(t -> {
            searcher.apply(t)
                    .thenAccept(list -> Platform.runLater(() -> result.setList(list)))
                    .exceptionally(HandlerFX.exceptionally(this));
        });
        getChildren().addAll(
                input,
                result
        );
    }

    public void simulateSearchTextInsert(String text){
        input.simulateSearchTextInsert(text);
    }

    public void simulateSearchButtonClick(){
        input.simulateSearchButtonClick();
    }

    public void simulateSearchTextFocus() {
        input.simulateSearchTextFocus();
    }

    public List<T> getSearchResults() {
        return result.getItems();
    }

    public void setOnSelectCallback(Consumer<T> cb){
        result.setOnSelectCallback(cb);
    }

    public void setOnDoubleClickSelectCallback(Consumer<T> cb){
        result.setOnDoubleClickSelectCallback(cb);
    }
}
