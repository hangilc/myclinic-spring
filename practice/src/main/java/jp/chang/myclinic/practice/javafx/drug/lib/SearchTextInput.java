package jp.chang.myclinic.practice.javafx.drug.lib;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.function.Consumer;

public class SearchTextInput extends HBox {

    //private static Logger logger = LoggerFactory.getLogger(SearchTextInput.class);
    private TextField textInput = new TextField();
    private Button searchButton;
    private Consumer<String> onSearchHandler = s -> {};

    public SearchTextInput() {
        super(4);
        setAlignment(Pos.CENTER_LEFT);
        this.searchButton = new Button("検索");
        textInput.setOnAction(evt -> doEnter());
        searchButton.setOnAction(evt -> doEnter());
        getChildren().addAll(
                textInput,
                searchButton
        );
    }

    public void setHandler(Consumer<String> handler){
        this.onSearchHandler = handler;
    }

    public void clear(){
        textInput.setText("");
    }

    private void doEnter(){
        onSearchHandler.accept(textInput.getText());
    }

    public void simulateSetSearchText(String text) {
        textInput.setText(text);
    }

    public void simulateClickSearchButton() {
        searchButton.fire();
    }
}
