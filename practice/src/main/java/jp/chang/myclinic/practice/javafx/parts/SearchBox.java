package jp.chang.myclinic.practice.javafx.parts;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public class SearchBox<I extends Node, SearchTextInput, R extends Node, SearchResultDisp> extends VBox {
    public interface SearchTextInput {
        void setOnSearchCallback(Consumer<String> cb);
    }

    public interface SearchResultDisp {

    }

    public SearchBox(I textInput, R resultDisp){
        super(4);
        getChildren().addAll(
                textInput,
                resultDisp
        );
    }
}
