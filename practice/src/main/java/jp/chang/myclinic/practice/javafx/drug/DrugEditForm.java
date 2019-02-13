package jp.chang.myclinic.practice.javafx.drug;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.consts.Zaikei;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugEditInput;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugForm;
import jp.chang.myclinic.practice.javafx.events.DrugDeletedEvent;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;

public class DrugEditForm extends DrugForm {

    //private static Logger logger = LoggerFactory.getLogger(EditForm.class);
    private DrugEditInput input = new DrugEditInput();
    private HBox tekiyouBox = new HBox(4);
    private boolean isGaiyou;

    public DrugEditForm(DrugFullDTO drug, String drugTekiyou, VisitDTO visit) {
        super(visit);
        this.isGaiyou = Zaikei.fromCode(drug.master.zaikei) == Zaikei.Gaiyou;
        input.setDrug(drug);
        input.setTekiyou(drugTekiyou);
        tekiyouBox.setAlignment(Pos.CENTER_LEFT);
        adaptTekiyouBox(drugTekiyou);
        input.tekiyouProperty().addListener((obs, oldValue, newValue) -> adaptTekiyouBox(newValue));
        getChildren().addAll(
                createTitle("処方の編集"),
                input,
                createCommands(),
                getSearchTextInput(),
                getSearchModeChooserBox(),
                getSearchResult()
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
            if( isGaiyou ){
                curr = "１日２枚";
            } else {
                curr = "";
            }
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
                        Platform.runLater(() -> DrugEditForm.this.fireEvent(event));
                    })
                    .exceptionally(HandlerFX::exceptionally);
        }
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

    protected void onUpdated(DrugFullDTO updated) {
    }

    protected void onClose() {
    }

    protected void onTekiyouModified(String newTekiyou) {
    }

}
