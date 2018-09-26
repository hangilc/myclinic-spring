package jp.chang.myclinic.practice.javafx.drug;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.function.Consumer;

class SearchTextInput extends HBox {

    //private static Logger logger = LoggerFactory.getLogger(SearchTextInput.class);
    private TextField textInput = new TextField();
    private Consumer<String> onSearchHandler = s -> {};

    SearchTextInput() {
        super(4);
        setAlignment(Pos.CENTER_LEFT);
        Button searchButton = new Button("検索");
        searchButton.setOnAction(evt -> onSearchHandler.accept(textInput.getText()));
        getChildren().addAll(
                textInput,
                searchButton
        );
    }

    void setHandler(Consumer<String> handler){
        this.onSearchHandler = handler;
    }
}
