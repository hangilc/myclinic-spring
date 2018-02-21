package jp.chang.myclinic.practice.javafx.parts.searchboxold;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import jp.chang.myclinic.practice.javafx.HandlerFX;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class SearchResultBase<M> extends ListView<M> implements SearchResult<M>{

    private Consumer<M> onSelectCallback = m -> {};

    public SearchResultBase(){
        getStyleClass().add("search-result");
        setCellFactory(listView -> new ListCell<M>(){
            @Override
            protected void updateItem(M item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : convert(item));
            }
        });
        setOnMouseClicked(this::onMouseClick);
    }

    @Override
    public void search(String text) {
        doSearch(text)
                .thenAccept(result -> Platform.runLater(() -> setList(result)))
                .exceptionally(HandlerFX::exceptionally);
    }

    @Override
    public void setOnSelectCallback(Consumer<M> cb) {
        this.onSelectCallback = cb;
    }

    protected abstract CompletableFuture<List<M>> doSearch(String text);

    protected abstract String convert(M model);

    private void setList(List<M> list){
        itemsProperty().setValue(FXCollections.observableArrayList(list));
    }

    private void onMouseClick(MouseEvent event){
        M selected = getSelectionModel().getSelectedItem();
        if( selected != null ){
            onSelectCallback.accept(selected);
        }
    }

}
