package jp.chang.myclinic.practice.javafx.drug.lib;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.dto.PrescExampleFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.function.Consumer;

public class DrugForm extends VBox {

    private static Logger logger = LoggerFactory.getLogger(DrugForm.class);
    private SearchModeChooser modeChooser = new SearchModeChooser(
            DrugSearchMode.Master, DrugSearchMode.Example, DrugSearchMode.Previous
    );
    private HBox modeChooserBox = new HBox(4);
    private LocalDate at;
    private int patientId;
    private int visitId;
    private SearchTextInput searchTextInput = new SearchTextInput();
    private SearchResult searchResult = new SearchResult();
    private DrugSearchResultItem currentInputItem;

    public DrugForm(VisitDTO visit) {
        super(4);
        getStyleClass().add("drug-form");
        getStyleClass().add("form");
        this.at = LocalDate.parse(visit.visitedAt.substring(0, 10));
        this.patientId = visit.patientId;
        this.visitId = visit.visitId;
        modeChooser.setValue(DrugSearchMode.Example);
        modeChooserBox.getChildren().addAll(modeChooser.getButtons());
        searchTextInput.setHandler(this::onSearch);
        searchResult.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldValue, newValue) -> {
                    if (newValue != null) {
                       onSearchResultSelected(newValue);
                    }
                });
    }

    public DrugSearchResultItem getCurrentInputItem(){
        return currentInputItem;
    }

    private void onSearchResultSelected(DrugSearchResultItem item){
        if( item != null ){
            if( item instanceof DrugSearcher.MasterItem ){
                DrugSearcher.MasterItem masterItem = (DrugSearcher.MasterItem)item;
                onMasterSelected(masterItem.getMaster());
            } else if( item instanceof DrugSearcher.ExampleItem ){
                DrugSearcher.ExampleItem exampleItem = (DrugSearcher.ExampleItem)item;
                onPrescExampleSelected(exampleItem.getExample());
            } else if( item instanceof DrugSearcher.DrugItem ){
                DrugSearcher.DrugItem drugItem = (DrugSearcher.DrugItem)item;
                onDrugSelected(drugItem.getDrug());
            } else {
                logger.error("Unknown drug search item. {}", item);
                return;
            }
            this.currentInputItem = item;
        }
    }

    protected int getVisitId() {
        return visitId;
    }

    protected Node createTitle(String text) {
        Label title = new Label(text);
        title.setMaxWidth(Double.MAX_VALUE);
        title.getStyleClass().add("title");
        return title;
    }

    protected HBox getSearchModeChooserBox() {
        return modeChooserBox;
    }

    protected SearchTextInput getSearchTextInput() {
        return searchTextInput;
    }

    protected SearchResult getSearchResult() {
        return searchResult;
    }

    private void resolveMaster(int iyakuhincode, Consumer<IyakuhinMasterDTO> handler) {
        Context.getInstance().getFrontend().resolveIyakuhinMaster(iyakuhincode, at.toString())
                .thenAcceptAsync(master -> {
                    if (master == null) {
                        GuiUtil.alertError("使用できない薬剤です。");
                    } else {
                        handler.accept(master);
                    }
                }, Platform::runLater)
                .exceptionally(HandlerFX::exceptionally);
    }

    protected void onMasterSelected(IyakuhinMasterDTO master) {

    }

    protected void onPrescExampleSelected(PrescExampleFullDTO example) {

    }

    protected void onDrugSelected(DrugFullDTO drug) {

    }

    private void setMaster(IyakuhinMasterDTO origMaster) {
        resolveMaster(origMaster.iyakuhincode, this::onMasterSelected);
    }

    private void setExample(PrescExampleFullDTO example) {
        resolveMaster(example.master.iyakuhincode, master -> {
            example.master = master;
            onPrescExampleSelected(example);
        });
    }

    private void setDrug(DrugFullDTO drug) {
        resolveMaster(drug.master.iyakuhincode, master -> {
            drug.master = master;
            onDrugSelected(drug);
        });
    }

    private void onSearch(String searchText) {
        if (searchText == null) {
            return;
        }
        DrugSearchMode mode = modeChooser.getValue();
        if (mode != null) {
            switch (mode) {
                case Master: {
                    if (searchText.isEmpty()) {
                        return;
                    }
                    DrugSearcher.searchMaster(searchText, at, this::setMaster)
                            .thenAcceptAsync(searchResult::setItems, Platform::runLater)
                            .exceptionally(HandlerFX::exceptionally);
                    break;
                }
                case Example: {
                    if (searchText.isEmpty()) {
                        return;
                    }
                    DrugSearcher.searchExample(searchText, this::setExample)
                            .thenAcceptAsync(searchResult::setItems, Platform::runLater)
                            .exceptionally(HandlerFX::exceptionally);
                    break;
                }
                case Previous: {
                    DrugSearcher.searchDrug(searchText, patientId, this::setDrug)
                            .thenAcceptAsync(searchResult::setItems, Platform::runLater)
                            .exceptionally(HandlerFX::exceptionally);
                    break;
                }
                default: {
                    logger.error("Invalid search mode: " + mode);
                    break;
                }
            }
        }
    }

}
