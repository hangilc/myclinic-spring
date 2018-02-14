package jp.chang.myclinic.practice.javafx.parts;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchTextBox extends HBox {

    private static Logger logger = LoggerFactory.getLogger(SearchTextBox.class);

    private TextField inputField = new TextField();

    public SearchTextBox() {
        super(4);
        inputField.getStyleClass().add("search-text-input");
        inputField.setOnAction(evt -> doEnter());
        Button enterButton = new Button("検索");
        enterButton.setOnAction(evt -> doEnter());
        getChildren().addAll(inputField, enterButton);
    }

    private void doEnter(){
        onEnter(inputField.getText());
    }

    protected void onEnter(String text){

    }

}
