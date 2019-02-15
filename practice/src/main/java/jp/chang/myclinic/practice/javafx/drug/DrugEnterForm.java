package jp.chang.myclinic.practice.javafx.drug;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.javafx.drug.lib.*;
import jp.chang.myclinic.practice.javafx.events.DrugEnteredEvent;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.util.List;

public class DrugEnterForm extends DrugForm {

    //private static Logger logger = LoggerFactory.getLogger(EnterForm.class);
    private DrugEnterInput input = new DrugEnterInput();
    private SearchTextInput searchTextInput;
    private SearchResult searchResult;
    private Button enterButton;

    DrugEnterForm(VisitDTO visit){
        super(visit);
        this.searchTextInput = getSearchTextInput();
        this.searchResult = getSearchResult();
        getChildren().addAll(
                createTitle("新規処方の入力"),
                input,
                createCommands(),
                searchTextInput,
                getSearchModeChooserBox(),
                searchResult
        );
    }

    public void simulateSetSearchText(String text){
        searchTextInput.simulateSetSearchText(text);
    }

    public void simulateClickSearchButton(){
        searchTextInput.simulateClickSearchButton();
    }

    public int getSearchResultSerialId(){
        return searchResult.getSerialId();
    }

    public List<DrugSearchResultItem> getSearchResultItems(){
        return searchResult.getItems();
    }

    public void simulateSelectSearchResultItem(DrugSearchResultItem item){
        searchResult.getSelectionModel().select(item);
    }

    public void simulateClickEnterButton(){
        enterButton.fire();
    }

    protected void onClose(){

    }

    private Node createCommands() {
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getStyleClass().add("commands");
        this.enterButton = new Button("入力");
        Button closeButton = new Button("閉じる");
        Hyperlink clearLink = new Hyperlink("クリア");
        enterButton.setOnAction(evt -> doEnter());
        closeButton.setOnAction(evt -> onClose());
        clearLink.setOnAction(evt -> input.clear());
        hbox.getChildren().addAll(
                enterButton,
                closeButton,
                clearLink
        );
        return hbox;
    }

    private void doEnter(){
        DrugDTO drug = input.createDrug(0, getVisitId(), 0);
        if( drug == null ){
            return;
        }
        Service.api.enterDrug(drug)
                .thenCompose(Service.api::getDrugFull)
                .thenAccept(enteredDrug -> Platform.runLater(() -> {
                    DrugEnterForm.this.fireEvent(new DrugEnteredEvent(enteredDrug, null));
                    input.clear();
                    getSearchTextInput().clear();
                    getSearchResult().clear();
                }))
                .exceptionally(HandlerFX::exceptionally);
    }

    @Override
    protected void onMasterSelected(IyakuhinMasterDTO master) {
        input.setMaster(master);
    }

    @Override
    protected void onPrescExampleSelected(PrescExampleFullDTO example) {
        input.setExample(example);
    }

    @Override
    protected void onDrugSelected(DrugFullDTO drug) {
        input.setDrug(drug);
    }

}