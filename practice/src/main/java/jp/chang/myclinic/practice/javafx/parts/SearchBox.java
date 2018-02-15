package jp.chang.myclinic.practice.javafx.parts;

import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Consumer;

public class SearchBox<T> extends VBox {

    private SearchInputBox inputBox;
    private SearchResult<T> resultBox;

    public SearchBox(){
        SearchInputBox inputBox = new SearchInputBox(){
            @Override
            protected void onSearch(String text) {
                if( text.isEmpty() ){
                    return;
                }
                search(text, result -> {
                    resultBox.setList(result);
                });
            }
        };
        resultBox = new SearchResult<T>(this::convert){
            @Override
            protected void onSelect(T selected) {
                SearchBox.this.onSelect(selected);
            }
        };
        getChildren().addAll(inputBox, resultBox);
    }

    protected String convert(T result){
        return result.toString();
    }

    protected void search(String text, Consumer<List<T>> cb){

    }

    protected void onSelect(T selection){

    }

}
