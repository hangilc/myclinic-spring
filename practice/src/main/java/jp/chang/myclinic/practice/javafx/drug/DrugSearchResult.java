package jp.chang.myclinic.practice.javafx.drug;

import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import jp.chang.myclinic.practice.lib.DrugSearchResultModel;

public class DrugSearchResult extends ListView<DrugSearchResultModel> {

    public DrugSearchResult(){
        super(FXCollections.observableArrayList());
        getStyleClass().add("search-result");
        setCellFactory(listView -> new ListCell<DrugSearchResultModel>(){
            @Override
            protected void updateItem(DrugSearchResultModel item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.rep());
            }
        });
    }

}
