package jp.chang.myclinic.practice.javafx.drug;

import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class DrugSearchResult extends ListView<SearchResultModel> {

    public DrugSearchResult(){
        super(FXCollections.observableArrayList());
        getStyleClass().add("search-result");
        setCellFactory(listView -> new ListCell<SearchResultModel>(){
            @Override
            protected void updateItem(SearchResultModel item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.rep());
            }
        });
    }

}
