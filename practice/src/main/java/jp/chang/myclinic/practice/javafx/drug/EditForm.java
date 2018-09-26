package jp.chang.myclinic.practice.javafx.drug;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugEditInput;
import jp.chang.myclinic.practice.javafx.drug.lib.SearchResult;
import jp.chang.myclinic.practice.javafx.drug.lib.Searcher;
import jp.chang.myclinic.practice.javafx.events.DrugDeletedEvent;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.function.Consumer;

public class EditForm extends VBox {

    private static Logger logger = LoggerFactory.getLogger(EditForm.class);
    private LocalDate at;
    private int patientId;
    private DrugEditInput input = new DrugEditInput();
    private SearchModeChooser modeChooser = new SearchModeChooser(
            DrugSearchMode.Master, DrugSearchMode.Example, DrugSearchMode.Previous
    );
    private SearchResult searchResult = new SearchResult();
    private HBox tekiyouBox = new HBox(4);

    public EditForm(DrugFullDTO drug, String drugTekiyou, VisitDTO visit) {
        super(4);
        this.at = LocalDate.parse(visit.visitedAt.substring(0, 10));
        this.patientId = visit.patientId;
        getStyleClass().add("drug-form");
        getStyleClass().add("form");
        input.setDrug(drug);
        input.setTekiyou(drugTekiyou);
        tekiyouBox.setAlignment(Pos.CENTER_LEFT);
        adaptTekiyouBox(drugTekiyou);
        input.tekiyouProperty().addListener((obs, oldValue, newValue) -> adaptTekiyouBox(newValue));
        SearchTextInput searchTextInput = new SearchTextInput();
        searchTextInput.setHandler(this::onSearch);
        HBox modeChooserBox = new HBox(4);
        modeChooserBox.getChildren().addAll(modeChooser.getButtons());
        modeChooser.setValue(DrugSearchMode.Example);
        getChildren().addAll(
                createTitle("処方の編集"),
                input,
                createCommands(),
                searchTextInput,
                modeChooserBox,
                searchResult
        );
    }

    private Node createCommands() {
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getStyleClass().add("commands");
        Button enterButton = new Button("入力");
        Button closeButton = new Button("閉じる");
        Hyperlink deleteLink = new Hyperlink("削除");
        Hyperlink auxLink = new Hyperlink("他");
        enterButton.setOnAction(evt -> doEnter());
        closeButton.setOnAction(evt -> onClose());
        deleteLink.setOnAction(evt -> doDelete());
        auxLink.setOnMouseClicked(evt -> doAux(evt, auxLink));
        hbox.getChildren().addAll(
                enterButton,
                closeButton,
                deleteLink,
                tekiyouBox,
                auxLink
        );
        return hbox;
    }

    private void doAux(MouseEvent event, Node node){
        ContextMenu contextMenu = createAuxContextMenu();
        contextMenu.show(node, event.getScreenX(), event.getScreenY());
    }

    private void adaptTekiyouBox(String tekiyouText) {
        boolean exists = tekiyouText != null && !tekiyouText.isEmpty();
        if (!exists) {
            Hyperlink tekiyouLink = new Hyperlink("摘要入力");
            tekiyouLink.setOnAction(evt -> doEnterTekiyou(tekiyouText));
            tekiyouBox.getChildren().setAll(tekiyouLink);
        } else {
            Hyperlink editTekiyouLink = new Hyperlink("摘要編集");
            Hyperlink deleteTekiyouLink = new Hyperlink("摘要削除");
            editTekiyouLink.setOnAction(evt -> doEnterTekiyou(tekiyouText));
            deleteTekiyouLink.setOnAction(evt -> doDeleteTekiyou());
            tekiyouBox.getChildren().setAll(editTekiyouLink, deleteTekiyouLink);
        }
    }

    private void doEnterTekiyou(String tekiyouText) {
        String curr = tekiyouText;
        if (curr == null) {
            curr = "";
        }
        GuiUtil.askForString("摘要の内容", curr).ifPresent(str -> {
            Service.api.setDrugTekiyou(input.getDrugId(), str)
                    .thenAccept(ok -> {
                        Platform.runLater(() -> {
                            input.tekiyouProperty().setValue(str);
                            onTekiyouModified(str);
                        });
                    })
                    .exceptionally(HandlerFX::exceptionally);
        });
    }

    private void doDeleteTekiyou() {
        if (GuiUtil.confirm("現在の摘要を削除しますか？")) {
            Service.api.deleteDrugTekiyou(input.getDrugId())
                    .thenAccept(ok -> {
                        Platform.runLater(() -> {
                            input.tekiyouProperty().setValue(null);
                            onTekiyouModified(null);
                        });
                    })
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

    private ContextMenu createAuxContextMenu(){
        ContextMenu menu = new ContextMenu();
        {
            MenuItem item = new MenuItem("処方例に追加");
            item.setOnAction(evt -> doAddToPrescExample());
            menu.getItems().add(item);
        }
        return menu;
    }

    private void doAddToPrescExample(){
        Service.api.getDrugFull(input.getDrugId())
                .thenAcceptAsync(drugFull -> {
                    ConvertToPrescExampleDialog dialog = new ConvertToPrescExampleDialog(drugFull);
                    dialog.initOwner(getScene().getWindow());
                    dialog.initModality(Modality.WINDOW_MODAL);
                    dialog.show();
                }, Platform::runLater)
                .exceptionally(HandlerFX::exceptionally);
    }

    private void resolveMaster(int iyakuhincode, Consumer<IyakuhinMasterDTO> handler) {
        Service.api.resolveIyakuhinMaster(iyakuhincode, at.toString())
                .thenAcceptAsync(master -> {
                    if (master == null) {
                        GuiUtil.alertError("使用できない薬剤です。");
                    } else {
                        handler.accept(master);
                    }
                }, Platform::runLater)
                .exceptionally(HandlerFX::exceptionally);
    }

    private void setMaster(IyakuhinMasterDTO origMaster) {
        resolveMaster(origMaster.iyakuhincode, input::setMaster);
    }

    private void setExample(PrescExampleFullDTO example) {
        resolveMaster(example.master.iyakuhincode, master -> {
            example.master = master;
            input.setExample(example);
        });
    }

    private void setDrug(DrugFullDTO drug) {
        resolveMaster(drug.master.iyakuhincode, master -> {
            drug.master = master;
            input.setDrug(drug);
        });
        ;
    }

    private void onSearch(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            return;
        }
        DrugSearchMode mode = modeChooser.getValue();
        if (mode != null) {
            switch (mode) {
                case Master: {
                    Searcher.searchMaster(searchText, at, this::setMaster)
                            .thenAcceptAsync(searchResult::setItems, Platform::runLater)
                            .exceptionally(HandlerFX::exceptionally);
                    break;
                }
                case Example: {
                    Searcher.searchExample(searchText, this::setExample)
                            .thenAcceptAsync(searchResult::setItems, Platform::runLater)
                            .exceptionally(HandlerFX::exceptionally);
                    break;
                }
                case Previous: {
                    Searcher.searchDrug(searchText, patientId, this::setDrug)
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

    private void doEnter() {
        DrugDTO drug = input.createDrug();
        if( drug == null ){
            return;
        }
        if (drug.drugId == 0) {
            throw new RuntimeException("drugId is null.");
        }
        drug.prescribed = 0;
        Service.api.updateDrug(drug)
                .thenCompose(ok -> Service.api.getDrugFull(drug.drugId))
                .thenAcceptAsync(this::onUpdated, Platform::runLater)
                .exceptionally(HandlerFX::exceptionally);
    }

    private void doDelete() {
        if (GuiUtil.confirm("この処方を削除していいですか？")) {
            class Local {
                private DrugDTO drug;
            }
            Local local = new Local();
            Service.api.getDrug(input.getDrugId())
                    .thenCompose(drugDTO -> {
                        local.drug = drugDTO;
                        return Service.api.deleteDrug(drugDTO.drugId);
                    })
                    .thenAccept(ok -> {
                        DrugDeletedEvent event = new DrugDeletedEvent(local.drug);
                        Platform.runLater(() -> EditForm.this.fireEvent(event));
                    })
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

    protected void onUpdated(DrugFullDTO updated) {
    }

    protected void onClose() {
    }

    protected void onTekiyouModified(String newTekiyou) {
    }

    private Node createTitle(String text) {
        Label title = new Label(text);
        title.setMaxWidth(Double.MAX_VALUE);
        title.getStyleClass().add("title");
        return title;
    }

}
