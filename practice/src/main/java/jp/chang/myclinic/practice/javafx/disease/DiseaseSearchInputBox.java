package jp.chang.myclinic.practice.javafx.disease;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.practice.javafx.parts.SearchBox;

import java.util.function.Consumer;

public class DiseaseSearchInputBox extends VBox implements SearchBox.InputBox {

    private Consumer<String> onTextCallback = s -> {};

    public DiseaseSearchInputBox(){
        super(4);
        getChildren().addAll(
                createInput()
        );
    }

    private Node createInput(){
        HBox hbox = new HBox(4);
        TextField textField = new TextField();
        Button searchButton = new Button("検索");
        Hyperlink exampleLink = new Hyperlink("例");
        hbox.getChildren().addAll(
                textField,
                searchButton,
                exampleLink
        );
        return hbox;
    }

    @Override
    public void setOnTextCallback(Consumer<String> cb) {
        this.onTextCallback = cb;
    }

    @Override
    public Node asNode() {
        return this;
    }
}
