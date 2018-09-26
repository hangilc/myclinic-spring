package jp.chang.myclinic.practice.javafx.prescexample;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.PrescExampleDTO;
import jp.chang.myclinic.practice.javafx.drug.lib.*;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

abstract class PrescExampleBaseDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(PrescExampleBaseDialog.class);
    private PrescExampleInput input = new PrescExampleInput();
    private SearchTextInput searchTextInput = new SearchTextInput();
    private SearchResult searchResult = new SearchResult();
    private SearchModeChooser searchModeChooser;
    private LocalDate at = LocalDate.now();

    PrescExampleBaseDialog(SearchModeChooser searchModeChooser) {
        this.searchModeChooser = searchModeChooser;
        Parent mainPane = createMainPane();
        mainPane.getStyleClass().add("presc-example-dialog");
        mainPane.getStylesheets().add("css/Practice.css");
        setScene(new Scene(mainPane));
    }

    int getPrescExampleId() {
        return input.getPrescExampleId();
    }

    PrescExampleInput getInput(){
        return input;
    }

    SearchResult getSearchResult(){
        return searchResult;
    }

    void addToSearchTextInputBox(Node node){
        searchTextInput.getChildren().add(node);
    }

    private Parent createMainPane() {
        VBox vbox = new VBox(4);
        HBox modeChooserBox = new HBox(4);
        searchModeChooser.setValue(DrugSearchMode.Example);
        modeChooserBox.getChildren().addAll(searchModeChooser.getButtons());
        searchTextInput.setHandler(this::doSearch);
        VBox.setVgrow(searchResult, Priority.ALWAYS);
        vbox.getChildren().addAll(
                input,
                new Separator(),
                createCommands(),
                new Separator(),
                searchTextInput,
                modeChooserBox,
                searchResult
        );
        return vbox;
    }

    private void doSearch(String text) {
        DrugSearchMode mode = searchModeChooser.getValue();
        if (mode != null) {
            switch (mode) {
                case Master: {
                    Searcher.searchMaster(text, at, input::setMaster)
                            .thenAcceptAsync(searchResult::setItems, Platform::runLater)
                            .exceptionally(HandlerFX::exceptionally);
                    break;
                }
                case Example: {
                    Searcher.searchExample(text, input::setExample)
                            .thenAcceptAsync(searchResult::setItems, Platform::runLater)
                            .exceptionally(HandlerFX::exceptionally);
                    break;
                }
                default: {
                    logger.error("Invalid search mode: " + mode);
                    break;
                }
            }
        } else {
            logger.error("Null search mode.");
        }
    }

    abstract Node createCommands();

    void setSearchMode(DrugSearchMode mode) {
        searchModeChooser.setValue(mode);
    }

    PrescExampleDTO createPrescExample() {
        return input.createPrescExample();
    }

    void doClear() {
        input.clear();
        searchTextInput.clear();
        searchResult.getItems().clear();
    }

    LocalDate getLocalDate() {
        return at;
    }

}
