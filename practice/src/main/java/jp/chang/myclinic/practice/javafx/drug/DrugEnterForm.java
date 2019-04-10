package jp.chang.myclinic.practice.javafx.drug;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugForm;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugSearchResultItem;
import jp.chang.myclinic.practice.javafx.drug.lib.SearchResult;
import jp.chang.myclinic.practice.javafx.drug.lib.SearchTextInput;
import jp.chang.myclinic.practice.javafx.drug.lib2.DrugEnterInput;
import jp.chang.myclinic.util.validator.Validated;
import jp.chang.myclinic.utilfx.AlertDialog;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.util.List;
import java.util.function.BiConsumer;

public class DrugEnterForm extends DrugForm {

    private DrugEnterInput input = new DrugEnterInput();
    private SearchTextInput searchTextInput;
    private SearchResult searchResult;
    private Button enterButton;
    private Button closeButton;
    private Runnable onCloseHandler = () -> {};
    private BiConsumer<DrugFullDTO, DrugAttrDTO> onDrugEnteredHandler = (drug, attr) -> {};

    public DrugEnterForm(VisitDTO visit){
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

    public void setOnDrugEnteredHandler(BiConsumer<DrugFullDTO, DrugAttrDTO> handler){
        this.onDrugEnteredHandler = handler;
    }

    public void setOnCloseHandler(Runnable handler){
        this.onCloseHandler = handler;
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

    public void simulateClickCloseButton() {
        closeButton.fire();
    }

    private Node createCommands() {
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getStyleClass().add("commands");
        this.enterButton = new Button("入力");
        this.closeButton = new Button("閉じる");
        Hyperlink clearLink = new Hyperlink("クリア");
        enterButton.setOnAction(evt -> doEnter());
        closeButton.setOnAction(evt -> onCloseHandler.run());
        clearLink.setOnAction(evt -> input.clear());
        hbox.getChildren().addAll(
                enterButton,
                closeButton,
                clearLink
        );
        return hbox;
    }

    private void doEnter(){
        Validated<DrugDTO> validatedDrug = input.getDrug(getVisitId());
        if( validatedDrug.isFailure() ){
            AlertDialog.alert(validatedDrug.getErrorsAsString(), DrugEnterForm.this);
            return;
        }
        DrugDTO drug = validatedDrug.getValue();
        if( drug == null ){
            return;
        }
        Context.frontend.enterDrug(drug)
                .thenCompose(Context.frontend::getDrugFull)
                .thenAccept(enteredDrug -> Platform.runLater(() -> {
                    onDrugEnteredHandler.accept(enteredDrug, null);
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
        input.setPrescExample(example);
    }

    @Override
    protected void onDrugSelected(DrugFullDTO drug) {
        input.setDrug(drug);
    }

}
