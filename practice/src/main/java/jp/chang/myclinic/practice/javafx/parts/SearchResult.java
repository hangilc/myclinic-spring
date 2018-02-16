package jp.chang.myclinic.practice.javafx.parts;

import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class SearchResult<M> extends ListView<M> {

    private Function<M,String> converter = m::toString;
    private Consumer<M> onSelectCallback = m -> {};

    public SearchResult(){
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

    public void setList(List<M> list){
        itemsProperty().setValue(FXCollections.observableArrayList(list));
    }

    public void setOnSelectCallback(Consumer<M> cb){
        this.onSelectCallback = cb;
    }

    public void setConverter(Function<M,String> converter){
        this.converter = converter;
    }

    private void onMouseClick(MouseEvent event){
        M selected = getSelectionModel().getSelectedItem();
        if( selected != null ){
            onSelectCallback.accept(selected);
        }
    }

}
