package jp.chang.myclinic.practice.javafx.parts;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class SearchInputBox extends HBox {

    private TextField inputField = new TextField();


    public SearchInputBox(){
        super(4);
        Button searchButton = new Button("検索");
        inputField.setOnAction(evt -> doSearch());
        searchButton.setOnAction(evt -> doSearch());
        getChildren().addAll(
                inputField,
                searchButton
        );
    }

    private void doSearch(){
        onSearch(inputField.getText());
    }

    protected void onSearch(String text){

    }
}
