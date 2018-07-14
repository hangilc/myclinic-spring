package jp.chang.myclinic.practice.javafx.drug;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.dto.PrescExampleFullDTO;
import jp.chang.myclinic.practice.javafx.HandlerFX;
import jp.chang.myclinic.utilfx.RadioButtonGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

class Search extends VBox {

    private static Logger logger = LoggerFactory.getLogger(Search.class);

    private int patientId;
    private String at;
    private TextField searchTextInput;
    private RadioButtonGroup<DrugSearchMode> modeGroup;
    private DrugSearchResult searchResult;

    Search(int patientId, String at) {
        super(4);
        this.patientId = patientId;
        this.at = at;
        getChildren().addAll(
                createSearchInput(),
                createMode(),
                createResult()
        );
        searchResult.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                dispatchSelect(newValue);
            }
        });
    }

    protected void onMasterSelect(IyakuhinMasterDTO master) {

    }

    protected void onExampleSelect(PrescExampleFullDTO example){

    }

    protected void onPrevSelect(DrugFullDTO drug){

    }

    private void dispatchSelect(DrugSearchResultModel model){
        if( model instanceof MasterSearchResult ){
            MasterSearchResult masterSearchResult = (MasterSearchResult)model;
            onMasterSelect(masterSearchResult.getMaster());
        } else if( model instanceof ExampleSearchResult ){
            ExampleSearchResult exampleSearchResult = (ExampleSearchResult) model;
            PrescExampleFullDTO example = exampleSearchResult.getPrescExample();
            String at = exampleSearchResult.getVisitedAt();
            int origIyakuhincode = example.master.iyakuhincode;
            Service.api.resolveIyakuhinMaster(origIyakuhincode, at)
                    .thenAccept(master -> {
                        PrescExampleFullDTO resolved = PrescExampleFullDTO.copy(example);
                        resolved.master = master;
                        Platform.runLater(() -> onExampleSelect(resolved));
                    })
                    .exceptionally(HandlerFX::exceptionally);
        } else if( model instanceof PreviousPrescSearchResult ){
            PreviousPrescSearchResult prevSearchResult = (PreviousPrescSearchResult) model;
            DrugFullDTO drugFull = prevSearchResult.getDrug();
            String at = prevSearchResult.getVisitedAt();
            int origIyakuhincode = drugFull.master.iyakuhincode;
            Service.api.resolveIyakuhinMaster(origIyakuhincode, at)
                    .thenAccept(master -> {
                        DrugFullDTO resolved = DrugFullDTO.copy(drugFull);
                        resolved.master = master;
                        Platform.runLater(() -> onPrevSelect(resolved));
                    })
                    .exceptionally(HandlerFX::exceptionally);
        } else {
            logger.error("Invalid model: {}", model);
        }
    }

    public void clear() {
        searchTextInput.clear();
        searchResult.clear();
    }

    private Node createSearchInput() {
        HBox hbox = new HBox(4);
        searchTextInput = new TextField();
        Button searchButton = new Button("検索");
        searchTextInput.setOnAction(event -> doSearch());
        searchButton.setOnAction(event -> doSearch());
        hbox.getChildren().addAll(searchTextInput, searchButton);
        return hbox;
    }

    private Node createMode() {
        HBox hbox = new HBox(4);
        modeGroup = new RadioButtonGroup<>();
        modeGroup.createRadioButton("マスター", DrugSearchMode.Master);
        modeGroup.createRadioButton("約束処方", DrugSearchMode.Example);
        modeGroup.createRadioButton("過去の処方", DrugSearchMode.Previous);
        modeGroup.setValue(DrugSearchMode.Example);
        hbox.getChildren().addAll(modeGroup.getButtons());
        return hbox;
    }

    private void doSearch() {
        String text = searchTextInput.getText();
        DrugSearchMode mode = modeGroup.getValue();
        if (mode != null) {
            switch (mode) {
                case Master:
                    doMasterSearch(text);
                    break;
                case Example:
                    doExampleSearch(text);
                    break;
                case Previous:
                    doPreviousSearch(text, patientId);
                    break;
            }
        }
    }

    private void doMasterSearch(String text) {
        if (text == null || text.isEmpty()) {
            return;
        }
        Service.api.searchIyakuhinMaster(text, at)
                .thenAccept(result -> {
                    List<DrugSearchResultModel> models = result.stream()
                            .map(MasterSearchResult::new).collect(Collectors.toList());
                    Platform.runLater(() -> searchResult.itemsProperty().getValue().setAll(models));
                })
                .exceptionally(HandlerFX::exceptionally);
    }

    private void doExampleSearch(String text) {
        if (text == null || text.isEmpty()) {
            return;
        }
        Service.api.searchPrescExample(text)
                .thenAccept(result -> {
                    List<DrugSearchResultModel> models = result.stream()
                            .map(example -> new ExampleSearchResult(example, at))
                            .collect(Collectors.toList());
                    Platform.runLater(() -> searchResult.itemsProperty().getValue().setAll(models));
                })
                .exceptionally(HandlerFX::exceptionally);
    }

    private void doPreviousSearch(String text, int patientId) {
        Service.api.searchPrevDrug(text, patientId)
                .thenAccept(result -> {
                    List<DrugSearchResultModel> models = result.stream()
                            .map(d -> new PreviousPrescSearchResult(d, at))
                            .collect(Collectors.toList());
                    Platform.runLater(() -> searchResult.itemsProperty().getValue().setAll(models));
                })
                .exceptionally(HandlerFX::exceptionally);
    }

    private Node createResult() {
        searchResult = new DrugSearchResult();
        return searchResult;
    }

}
