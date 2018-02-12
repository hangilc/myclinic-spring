package jp.chang.myclinic.practice.javafx.parts;

import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.util.List;
import java.util.function.Function;

public class SearchResult<M> extends ListView<M> {


    public SearchResult(Function<M, String> repMaker){
        getStyleClass().add("search-result");
        setCellFactory(listView -> new ListCell<M>(){
            @Override
            protected void updateItem(M item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : repMaker.apply(item));
            }
        });
        setOnMouseClicked(this::onMouseClick);
    }

    public void setList(List<M> list){
        itemsProperty().setValue(FXCollections.observableArrayList(list));
    }

    protected void onSelect(M selected){

    }

    private void onMouseClick(MouseEvent event){
        M selected = getSelectionModel().getSelectedItem();
        if( selected != null ){
            onSelect(selected);
        }
    }

}
