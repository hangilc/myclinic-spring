package jp.chang.myclinic.practice.javafx.parts.searchbox;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.function.Consumer;

public class BasicSearchTextInput extends HBox implements SearchTextInput {

    private Consumer<String> onSearchCallback = t -> {};
    private TextField textField = new TextField();

    public BasicSearchTextInput(){
        super(4);
        textField.getStyleClass().add("search-text-input");
        Button searchButton = new Button("検索");
        textField.setOnAction(evt -> doSearch());
        searchButton.setOnAction(evt -> doSearch());
        getChildren().addAll(
                textField,
                searchButton
        );
    }

    public void extend(Node... nodes){
        getChildren().addAll(nodes);
    }

    public void clear(){
        textField.clear();
    }

    private void doSearch(){
        String text = textField.getText();
        if( !text.isEmpty() ){
            onSearchCallback.accept(text);
        }
    }

    @Override
    public void setOnSearchCallback(Consumer<String> cb) {
        this.onSearchCallback = cb;
    }

}
