package jp.chang.myclinic.practice.javafx.globalsearch;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalSearchDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(GlobalSearchDialog.class);
    private TextField searchTextInput = new TextField();

    public GlobalSearchDialog() {
        VBox vbox = new VBox(4);
        vbox.getStyleClass().add("global-text-search-dialog");
        vbox.getStylesheets().add("css/Practice.css");
        vbox.getChildren().addAll(
            createInputs()
        );
        setScene(new Scene(vbox));
    }

    private Node createInputs(){
        HBox hbox = new HBox(4);
        searchTextInput = new TextField();
        searchTextInput.setOnAction(evt -> doSearch());
        Button searchButton = new Button("検索");
        searchButton.setOnAction(evt -> doSearch());
        hbox.getChildren().addAll(
                searchTextInput,
                searchButton
        );
        return hbox;
    }

    private void doSearch(){
        String text = searchTextInput.getText();
        if( !text.isEmpty() ){

        }
    }

}
