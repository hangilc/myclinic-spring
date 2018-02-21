package jp.chang.myclinic.practice.javafx.parts.searchboxold;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public class SearchBox<M, I extends Node & SearchTextInput, R extends Node & SearchResult<M>> extends VBox {

    private R resultNode;

    public SearchBox(I inputNode, R resultNode){
        super(4);
        this.resultNode = resultNode;
        inputNode.setOnSearchCallback(resultNode::search);
        getChildren().addAll(
                inputNode,
                resultNode
        );
    }

    public void setOnSelectCallback(Consumer<M> cb){
        resultNode.setOnSelectCallback(cb);
    }
}
