package jp.chang.myclinic.practice.javafx.parts;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.function.Consumer;

public class SearchInputBox extends HBox implements SearchBoxOld.InputBox {

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

    @Override
    public void setOnTextCallback(Consumer<String> cb){
        this.onTextCallback = cb;
    }

    @Override
    public Node asNode(){
        return this;
    }

    private void doSearch(){
        String text = inputField.getText();
        if( !text.isEmpty() ){
            this.onTextCallback.accept(inputField.getText());
        }
    }

}
