package jp.chang.myclinic.practice.javafx.parts;

import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class SelectableList<T> extends ListView<T> {

    private static Logger logger = LoggerFactory.getLogger(SelectableList.class);
    private Consumer<T> onSelectCallback = m -> {};

    public SelectableList(Function<T, String> converter) {
        setCellFactory(listView -> new ListCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : converter.apply(item));
            }
        });
        setOnMouseClicked(this::onMouseClick);
    }

    public SelectableList(Function<T, String> converter, List<T> dataList) {
        this(converter);
        setList(dataList);
    }

    public void setList(List<T> result) {
        itemsProperty().setValue(FXCollections.observableArrayList(result));
    }

    public void setOnSelectCallback(Consumer<T> cb) {
        this.onSelectCallback = cb;
    }

    public void clear() {
        setList(Collections.emptyList());
    }

    private void onMouseClick(MouseEvent event) {
        T selected = getSelectionModel().getSelectedItem();
        if (selected != null) {
            onSelectCallback.accept(selected);
        }
    }

    public void updateItem(Function<T,Boolean> pred, T newValue){
        int n = getItems().size();
        int index = -1;
        for(int i=0;i<n;i++){
            T item = getItems().get(i);
            if( pred.apply(item) ){
                index = i;
                break;
            }
        }
        if( index >= 0 ){
            getItems().set(index, newValue);
        }
    }
}
