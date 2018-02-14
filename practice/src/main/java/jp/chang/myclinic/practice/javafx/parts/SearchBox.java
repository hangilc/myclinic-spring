package jp.chang.myclinic.practice.javafx.parts;

import javafx.scene.layout.VBox;

public class SearchBox<T> extends VBox {

    public SearchBox(){
        SearchInputBox inputBox = new SearchInputBox();
        SearchResult<T> resultBox = new SearchResult<>(this::convert);
    }

    protected String convert(T result){
        return result.toString();
    }

}
