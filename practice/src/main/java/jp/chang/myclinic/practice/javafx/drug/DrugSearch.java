package jp.chang.myclinic.practice.javafx.drug;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.practice.lib.PracticeLib;
import jp.chang.myclinic.practice.lib.RadioButtonGroup;
import jp.chang.myclinic.practice.lib.drug.DrugSearchResultModel;
import jp.chang.myclinic.practice.lib.drug.ExampleSearchResult;
import jp.chang.myclinic.practice.lib.drug.MasterSearchResult;
import jp.chang.myclinic.practice.lib.drug.PreviousPrescSearchResult;

import java.util.List;
import java.util.stream.Collectors;

public class DrugSearch extends VBox {

    public interface Callback {
        void onSelect(DrugSearchResultModel searchResultModel);
    }

    private int patientId;
    private String at;
    private TextField searchTextInput;
    private RadioButtonGroup<DrugSearchMode> modeGroup;
    private DrugSearchResult searchResult;
    private Callback callback;

    public DrugSearch(int patientId, String at){
        super(4);
        this.patientId = patientId;
        this.at = at;
        getChildren().addAll(
                createSearchInput(),
                createMode(),
                createResult()
        );
        searchResult.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if( newValue != null ){
                if( callback != null ){
                    callback.onSelect(newValue);
                }
            }
        });
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    public void clear() {
        searchTextInput.clear();
        searchResult.clear();
    }

    private Node createSearchInput(){
        HBox hbox = new HBox(4);
        searchTextInput = new TextField();
        Button searchButton = new Button("検索");
        searchTextInput.setOnAction(event -> doSearch());
        searchButton.setOnAction(event -> doSearch());
        hbox.getChildren().addAll(searchTextInput, searchButton);
        return hbox;
    }

    private Node createMode(){
        HBox hbox = new HBox(4);
        modeGroup = new RadioButtonGroup<>();
        modeGroup.createRadioButton("マスター", DrugSearchMode.Master);
        modeGroup.createRadioButton("約束処方", DrugSearchMode.Example);
        modeGroup.createRadioButton("過去の処方", DrugSearchMode.Previous);
        modeGroup.setValue(DrugSearchMode.Example);
        hbox.getChildren().addAll(modeGroup.getButtons());
        return hbox;
    }

    private void doSearch(){
        String text = searchTextInput.getText();
        DrugSearchMode mode = modeGroup.getValue();
        if( mode != null ){
            switch(mode){
                case Master: doMasterSearch(text); break;
                case Example: doExampleSearch(text); break;
                case Previous: doPreviousSearch(text, patientId); break;
            }
        }
    }

    private void doMasterSearch(String text){
        if( text == null || text.isEmpty() ){
            return;
        }
        PracticeLib.searchIyakuhinMaster(text, at, result -> {
            List<DrugSearchResultModel> models = result.stream().map(MasterSearchResult::new).collect(Collectors.toList());
            searchResult.itemsProperty().getValue().setAll(models);
        });
    }

    private void doExampleSearch(String text){
        if( text == null || text.isEmpty() ){
            return;
        }
       PracticeLib.searchPrescExample(text, result -> {
            List<DrugSearchResultModel> models = result.stream()
                    .map(example -> new ExampleSearchResult(example, at))
                    .collect(Collectors.toList());
            searchResult.itemsProperty().getValue().setAll(models);
        });
    }

    private void doPreviousSearch(String text, int patientId){
        PracticeLib.searchPreviousPresc(text, patientId, result -> {
            List<DrugSearchResultModel> models = result.stream()
                    .map(d -> new PreviousPrescSearchResult(d, at))
                    .collect(Collectors.toList());
            searchResult.itemsProperty().getValue().setAll(models);
        });
    }

    private Node createResult(){
        searchResult = new DrugSearchResult();
        return searchResult;
    }

}
