package jp.chang.myclinic.practice.javafx.drug.lib;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SearchResult extends ListView<SearchResultItem> {

    private static Logger logger = LoggerFactory.getLogger(SearchResult.class);

    public SearchResult() {
        getStyleClass().add("search-result");
        setCellFactory(listView -> new ListCell<SearchResultItem>(){
            @Override
            protected void updateItem(SearchResultItem item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getRep());
            }
        });
        getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if( newValue != null ){
                newValue.onSelect();
            }
        });
    }

    public void setItems(List<SearchResultItem> items){
        getItems().clear();
        getItems().setAll(items);
    }

    public void clear(){
        getItems().clear();
    }

}
