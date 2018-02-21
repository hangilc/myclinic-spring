package jp.chang.myclinic.practice.javafx.parts.searchbox;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class ExtendedSearchTextInput extends VBox implements SearchTextInput {

    private static Logger logger = LoggerFactory.getLogger(ExtendedSearchTextInput.class);
    private BasicSearchTextInput basicInput;

    protected ExtendedSearchTextInput() {
        super(4);
        basicInput = new BasicSearchTextInput();
        getChildren().add(basicInput);
    }

    protected void extendBasic(Node... nodes){
        basicInput.extend(nodes);
    }

    protected void addRow(Node node){
        getChildren().add(node);
    }

    public void clear(){
        basicInput.clear();
    }

    @Override
    public void setOnSearchCallback(Consumer<String> cb) {
        basicInput.setOnSearchCallback(cb);
    }

}
