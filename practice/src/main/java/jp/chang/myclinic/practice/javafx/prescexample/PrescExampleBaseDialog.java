package jp.chang.myclinic.practice.javafx.prescexample;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.PrescExampleDTO;
import jp.chang.myclinic.practice.javafx.drug2.*;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.time.LocalDate;
import java.util.List;

abstract class PrescExampleBaseDialog extends Stage {

    //private static Logger logger = LoggerFactory.getLogger(PrescExampleBaseDialog.class);
    private Input input = new Input();
    private SearchInput searchInput = new SearchInput();
    private SearchResult searchResult = new SearchResult();
    private SearchModeChooser searchModeChooser;
    private TextField commentInput = new TextField();
    private LocalDate at = LocalDate.now();
    private int prescExampleId = 0;

    PrescExampleBaseDialog(SearchModeChooser searchModeChooser) {
        this.searchModeChooser = searchModeChooser;
        Parent mainPane = createMainPane();
        mainPane.getStyleClass().add("presc-example-dialog");
        mainPane.getStylesheets().add("css/Practice.css");
        setScene(new Scene(mainPane));
    }

    private Parent createMainPane() {
        VBox vbox = new VBox(4);
        {
            input.addRow(new Label("注釈："), commentInput);
        }
        {
            HBox hbox = new HBox(4);
            searchModeChooser.setValue(DrugSearchMode.Master);
            hbox.getChildren().addAll(searchModeChooser.getButtons());
            searchInput.getChildren().add(hbox);
        }
        searchInput.setOnSearchHandler(() -> {
            String text = searchInput.getSearchText().trim();
            if (!text.isEmpty()) {
                DrugSearchMode mode = searchModeChooser.getValue();
                DrugSearcher.search(text, mode, at)
                        .thenAcceptAsync(this::setSearchResultList, Platform::runLater)
                        .exceptionally(HandlerFX::exceptionally);
            }
        });
        searchResult.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                int prescExampleId = newValue.getPrescExampleId();
                if( prescExampleId != 0 ){
                    this.prescExampleId = prescExampleId;
                }
                input.setData(newValue);
                String comment = newValue.getComment();
                if( comment != null && !comment.isEmpty()) {
                    commentInput.setText(comment);
                }
            }
        });
        VBox.setVgrow(searchResult, Priority.ALWAYS);
        vbox.getChildren().addAll(
                input,
                new Separator(),
                createCommands(),
                new Separator(),
                searchInput,
                searchResult
        );
        return vbox;
    }

    void setSearchResultList(List<DrugData> list){
        searchResult.getItems().clear();
        searchResult.getItems().addAll(list);
    }

    abstract Node createCommands();

    Input getInput(){
        return input;
    }

    int getPrescExampleId() {
        return prescExampleId;
    }

    void setSearchMode(DrugSearchMode mode){
        searchModeChooser.setValue(mode);
    }

    void addToSearchTextInputBox(Node node){
        searchInput.addToTextInputBox(node);
    }

    PrescExampleDTO createPrescExample() {
        return input.createPrescExample(prescExampleId, commentInput.getText().trim());
    }

    void doClear(){
        input.clear();
        commentInput.setText("");
        searchInput.clear();
        searchResult.getItems().clear();
        this.prescExampleId = 0;
    }

    LocalDate getLocalDate(){
        return at;
    }

}
