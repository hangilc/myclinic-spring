package jp.chang.myclinic.practice.javafx.parts;

import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class SelectableList<T> extends ListView<T> {

    private static Logger logger = LoggerFactory.getLogger(SelectableList.class);

    private Consumer<T> onSelectCallback = t -> {};

    public SelectableList(Function<T, String> converter) {
        setCellFactory(listView -> new ListCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : converter.apply(item));
            }
        });
    }

    public SelectableList(Function<T, String> converter, List<T> dataList) {
        this(converter);
        setList(dataList);
    }

    public void setList(List<T> result) {
        if( result.size() == 1 ){
            onSelectCallback.accept(result.get(0));
        }
        itemsProperty().setValue(FXCollections.observableArrayList(result));
    }

    public void clear() {
        setList(Collections.emptyList());
    }

    public void setOnSelectCallback(Consumer<T> cb) {
        setupClickEvent(cb, 1);
    }

    public void setOnDoubleClickSelectCallback(Consumer<T> cb){
        setupClickEvent(cb, 2);
    }

    private void setupClickEvent(Consumer<T> cb, int clickCount){
        this.onSelectCallback = cb;
        setOnMouseClicked(event -> {
            if( event.getButton().equals(MouseButton.PRIMARY) ){
                if( event.getClickCount() == clickCount ){
                    doSelect(cb);
                }
            }
        });
    }

    private void doSelect(Consumer<T> cb){
        T selected = getSelectionModel().getSelectedItem();
        if (selected != null) {
            cb.accept(selected);
        }
    }


}
