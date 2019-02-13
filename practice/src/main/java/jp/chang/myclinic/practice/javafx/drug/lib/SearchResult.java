package jp.chang.myclinic.practice.javafx.drug.lib;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.util.List;

public class SearchResult extends ListView<DrugSearchResultItem> {

    //private static Logger logger = LoggerFactory.getLogger(SearchResult.class);
    private int serialId = 0;

    public SearchResult() {
        getStyleClass().add("search-result");
        setCellFactory(listView -> new ListCell<DrugSearchResultItem>(){
            @Override
            protected void updateItem(DrugSearchResultItem item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getRep());
            }
        });
    }

    public void setItems(List<DrugSearchResultItem> items){
        getItems().clear();
        getItems().setAll(items);
        serialId += 1;
    }

    public void clear(){
        getItems().clear();
    }

    public int getSerialId(){
        return serialId;
    }

}
