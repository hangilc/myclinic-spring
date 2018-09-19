package jp.chang.myclinic.practice.javafx.drug2;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class EnterForm extends VBox {

    private static Logger logger = LoggerFactory.getLogger(EnterForm.class);
    private int visitId;
    private CheckBox daysFixedCheck = new CheckBox("固定");
    private Input input = new Input();
    private SearchInput searchInput = new SearchInput();
    private SearchModeChooser searchModeChooser = new SearchModeChooser(
            DrugSearchMode.Master, DrugSearchMode.Example, DrugSearchMode.Previous
    );
    private SearchResult searchResult = new SearchResult();
    private LocalDate at;

    public EnterForm(VisitDTO visit) {
        super(4);
        this.visitId = visit.visitId;
        this.at = LocalDate.parse(visit.visitedAt.substring(0, 10));
        getStyleClass().add("drug-form");
        getStyleClass().add("form");
        daysFixedCheck.setSelected(true);
        searchInput.getChildren().add(createSearchModeBox());
        getChildren().addAll(createTitle(), input, createCommands(), searchInput, searchResult);
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
                            input.setData(newValue);
                        }, Platform::runLater)
                        .exceptionally(HandlerFX::exceptionally);
            }
        });
    }

    private Node createTitle() {
        Label title = new Label("新規処方の入力");
        title.setMaxWidth(Double.MAX_VALUE);
        title.getStyleClass().add("title");
        return title;
    }

    protected void onClose(){

    }

    private Node createCommands(){
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getStyleClass().add("commands");
        Button enterButton = new Button("入力");
        Button closeButton = new Button("閉じる");
        Hyperlink clearLink = new Hyperlink("クリア");
        enterButton.setOnAction(event -> doEnter());
        closeButton.setOnAction(event -> onClose());
        clearLink.setOnAction(event -> doClearInput());
        hbox.getChildren().addAll(enterButton, closeButton, clearLink);
        return hbox;
    }

    private Node createSearchModeBox(){
        HBox hbox = new HBox(4);
        searchModeChooser.setValue(DrugSearchMode.Example);
        hbox.getChildren().addAll(searchModeChooser.getButtons());
        return hbox;
    }

    private void doEnter(){
        DrugDTO drug = input.createDrug();
        Service.api.enterDrug(drug)
    }

    private void doClearInput(){
        input.clear();
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

}
