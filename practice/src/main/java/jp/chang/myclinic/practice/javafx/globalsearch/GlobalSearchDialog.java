package jp.chang.myclinic.practice.javafx.globalsearch;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.javafx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalSearchDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(GlobalSearchDialog.class);
    private TextField searchTextInput = new TextField();
    private Nav nav;
    private Result resultBox = new Result();

    public GlobalSearchDialog() {
        VBox vbox = new VBox(4);
        vbox.getStyleClass().add("global-text-search-dialog");
        vbox.getStylesheets().add("css/Practice.css");
        vbox.getChildren().addAll(
                createInputs(),
                createNav(),
                createResult()
        );
        setScene(new Scene(vbox));
    }

    private Node createInputs() {
        HBox hbox = new HBox(4);
        searchTextInput = new TextField();
        searchTextInput.setOnAction(evt -> doSearch(0));
        Button searchButton = new Button("検索");
        searchButton.setOnAction(evt -> doSearch(0));
        hbox.getChildren().addAll(
                searchTextInput,
                searchButton
        );
        return hbox;
    }

    private Node createNav(){
        nav = new Nav(){
            @Override
            protected void onPage(int page) {
                doSearch(page);
            }
        };
        return nav;
    }

    private Node createResult(){
        ScrollPane sc = new ScrollPane(resultBox);
        sc.getStyleClass().add("search-result-scroll");
        sc.setFitToWidth(true);
        return sc;
    }

    private void doSearch(int page) {
        String text = searchTextInput.getText();
        if (!text.isEmpty()) {
            Service.api.searchTextGlobally(text, page)
                    .thenAccept(result -> Platform.runLater(() ->{
                        resultBox.set(result.textVisitPatients, text);
                        nav.set(result.page, result.totalPages);
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

}
