package jp.chang.myclinic.practice.javafx.parts.searchbox;

import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class BasicSearchResultList<M> extends ListView<M> implements SearchResultList<M>{

    private Consumer<M> onSelectCallback = m -> {};
    private Function<M, String> converter = Object::toString;

    public BasicSearchResultList(){
        getStyleClass().add("search-result");
        setCellFactory(listView -> new ListCell<M>(){
            @Override
            protected void updateItem(M item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : converter.apply(item));
            }
        });
        setOnMouseClicked(this::onMouseClick);
    }

    @Override
    public void setResult(List<M> result) {
        itemsProperty().setValue(FXCollections.observableArrayList(result));
    }

    @Override
    public void setOnSelectCallback(Consumer<M> cb) {
        this.onSelectCallback = cb;
    }

    public void clear(){
        setResult(Collections.emptyList());
    }

    public void setConverter(Function<M, String> converter){
        this.converter = converter;
    }

    private void onMouseClick(MouseEvent event){
        M selected = getSelectionModel().getSelectedItem();
        if( selected != null ){
            onSelectCallback.accept(selected);
        }
    }

}
