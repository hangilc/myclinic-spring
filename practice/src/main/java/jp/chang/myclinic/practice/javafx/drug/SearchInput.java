package jp.chang.myclinic.practice.javafx.drug;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchInput extends VBox {

    private static Logger logger = LoggerFactory.getLogger(SearchInput.class);

    private HBox textInputBox = new HBox(4);
    private TextField searchTextInput = new TextField();
    private Runnable onSearchHandler = () -> {};
    //private RadioButtonGroup<DrugSearchMode> modeGroup = new RadioButtonGroup<>();

    public SearchInput() {
        super(4);
        getChildren().addAll(createSearchInput());
    }

    public void setOnSearchHandler(Runnable onSearchHandler) {
        this.onSearchHandler = onSearchHandler;
    }

    public String getSearchText(){
        return searchTextInput.getText();
    }

    public void clear(){
        searchTextInput.setText("");
    }

    public void addToTextInputBox(Node node){
        textInputBox.getChildren().add(node);
    }

    private Node createSearchInput() {
        textInputBox.setAlignment(Pos.CENTER_LEFT);
        Button searchButton = new Button("検索");
        searchTextInput.setOnAction(event -> onSearchHandler.run());
        searchButton.setOnAction(event -> onSearchHandler.run());
        textInputBox.getChildren().addAll(searchTextInput, searchButton);
        return textInputBox;
    }

}
