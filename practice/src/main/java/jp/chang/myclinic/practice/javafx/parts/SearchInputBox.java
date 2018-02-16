package jp.chang.myclinic.practice.javafx.parts;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.function.Consumer;

public class SearchInputBox extends HBox {

    private TextField inputField = new TextField();
    private Consumer<String> onTextCallback = s -> {};

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

    public void setOnTextCallback(Consumer<String> cb){
        this.onTextCallback = cb;
    }

    private void doSearch(){
        this.onTextCallback.accept(inputField.getText());
    }

}
