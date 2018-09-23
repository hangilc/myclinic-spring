package jp.chang.myclinic.practice.javafx.drug2;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SearchResult extends ListView<DrugData> {

    private static Logger logger = LoggerFactory.getLogger(SearchResult.class);

    public SearchResult() {
        getStyleClass().add("search-result");
        setCellFactory(listView -> new ListCell<DrugData>(){
            @Override
            protected void updateItem(DrugData item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.rep());
            }
        });
    }

    public void setItems(List<DrugData> items){
        getItems().clear();
        getItems().setAll(items);
    }

    public void clear(){
        getItems().clear();
    }
}
