package jp.chang.myclinic.practice.javafx.drug2;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.practice.javafx.drug.DrugSearchMode;
import jp.chang.myclinic.utilfx.RadioButtonGroup;

public class SearchInput extends VBox {

    //private static Logger logger = LoggerFactory.getLogger(SearchInput.class);

    private TextField searchTextInput = new TextField();
    private RadioButtonGroup<DrugSearchMode> modeGroup = new RadioButtonGroup<>();

    public SearchInput() {
        super(4);
        getChildren().addAll(createSearchInput(), createMode());
    }

    private Node createSearchInput() {
        HBox hbox = new HBox(4);
        Button searchButton = new Button("検索");
        searchTextInput.setOnAction(event -> doSearch());
        searchButton.setOnAction(event -> doSearch());
        hbox.getChildren().addAll(searchTextInput, searchButton);
        return hbox;
    }

    private Node createMode() {
        HBox hbox = new HBox(4);
        modeGroup.createRadioButton("マスター", DrugSearchMode.Master);
        modeGroup.createRadioButton("約束処方", DrugSearchMode.Example);
        modeGroup.createRadioButton("過去の処方", DrugSearchMode.Previous);
        modeGroup.setValue(DrugSearchMode.Example);
        hbox.getChildren().addAll(modeGroup.getButtons());
        return hbox;
    }

    private void doSearch(){

    }
}
