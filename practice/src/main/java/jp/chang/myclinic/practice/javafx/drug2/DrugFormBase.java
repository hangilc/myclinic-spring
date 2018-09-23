package jp.chang.myclinic.practice.javafx.drug2;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

class DrugFormBase extends VBox {

    private static Logger logger = LoggerFactory.getLogger(DrugFormBase.class);
    private int visitId;
    private Input input = new Input();
    private SearchInput searchInput = new SearchInput();
    private SearchModeChooser searchModeChooser = new SearchModeChooser(
            DrugSearchMode.Master, DrugSearchMode.Example, DrugSearchMode.Previous
    );
    private SearchResult searchResult = new SearchResult();
    private LocalDate at;
    private HBox commandBox = new HBox(4);

    DrugFormBase(VisitDTO visit, String title) {
        super(4);
        this.visitId = visit.visitId;
        this.at = LocalDate.parse(visit.visitedAt.substring(0, 10));
        getStyleClass().add("drug-form");
        getStyleClass().add("form");
        searchInput.getChildren().add(createSearchModeBox());
        getChildren().addAll(createTitle(title), input, createCommands(), searchInput, searchResult);
        searchInput.setOnSearchHandler(this::doSearch);
        searchResult.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if( newValue != null ){
                Service.api.resolveIyakuhinMaster(newValue.getIyakuhincode(), at.toString())
                        .thenAcceptAsync(master -> {
                            if( master == null ){
                                GuiUtil.alertError("現在使用できない薬剤です。");
                                return;
                            }
                            newValue.setMaster(master);
                            input.setData(newValue, getSetOptions());
                        }, Platform::runLater)
                        .exceptionally(HandlerFX::exceptionally);
            }
        });
    }

    int getVisitId(){
        return visitId;
    }

    Input getInput(){
        return input;
    }

    SearchInput getSearchInput(){
        return searchInput;
    }

    SearchResult getSearchResult(){
        return searchResult;
    }

    void addToCommandBox(Node node){
        commandBox.getChildren().add(node);
    }

    LocalDate getLocalDate(){
        return at;
    }

    private Node createTitle(String text) {
        Label title = new Label(text);
        title.setMaxWidth(Double.MAX_VALUE);
        title.getStyleClass().add("title");
        return title;
    }

    protected void onClose(){

    }

    private Node createCommands(){
        commandBox.setAlignment(Pos.CENTER_LEFT);
        commandBox.getStyleClass().add("commands");
        Button enterButton = new Button("入力");
        Button closeButton = new Button("閉じる");
        Hyperlink clearLink = new Hyperlink("クリア");
        enterButton.setOnAction(event -> doEnter());
        closeButton.setOnAction(event -> onClose());
        clearLink.setOnAction(event -> doClearInput());
        commandBox.getChildren().addAll(enterButton, closeButton, clearLink);
        return commandBox;
    }

    private Node createSearchModeBox(){
        HBox hbox = new HBox(4);
        searchModeChooser.setValue(DrugSearchMode.Example);
        hbox.getChildren().addAll(searchModeChooser.getButtons());
        return hbox;
    }

    void doEnter(){
    }

    void doClearInput(){
        input.clear(getSetOptions());
    }

    private void doSearch(){
        String text = searchInput.getSearchText().trim();
        if( text.isEmpty() ){
            return;
        }
        DrugSearcher.search(text, searchModeChooser.getValue(), at)
                .thenAcceptAsync(searchResult::setItems, Platform::runLater)
                .exceptionally(HandlerFX::exceptionally);
    }

    Set<Input.SetOption> getSetOptions(){
        return Collections.emptySet();
    }

}
