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
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugForm;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugEditInput;
import jp.chang.myclinic.util.validator.Validated;
import jp.chang.myclinic.utilfx.AlertDialog;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.util.function.BiConsumer;

public class DrugEditForm extends DrugForm {

    // form state variables
    private DrugAttrDTO attr; // cache

    private DrugEditInput input;
    private HBox tekiyouBox = new HBox(4);
    private int drugId;
    private int visitId;
    private BiConsumer<DrugFullDTO, DrugAttrDTO> onEnteredHandler = (d, a) -> {
    };
    private Runnable onCancelHandler = () -> {
    };
    private Runnable onDeletedHandler = () -> {
    };

    public DrugEditForm(DrugFullDTO drug, DrugAttrDTO attr, VisitDTO visit) {
        super(visit);
        this.attr = copyAttr(attr);
        this.drugId = drug.drug.drugId;
        this.visitId = drug.drug.visitId;
        this.input = new DrugEditInput(drug, attr);
        tekiyouBox.setAlignment(Pos.CENTER_LEFT);
        adaptTekiyouBox(extractTekiyou(attr));
        getChildren().addAll(
                createTitle("処方の編集"),
                input,
                createCommands(),
                getSearchTextInput(),
                getSearchModeChooserBox(),
                getSearchResult()
        );
    }

    private int getDrugId() {
        return drugId;
    }

    public void setOnEnteredHandler(BiConsumer<DrugFullDTO, DrugAttrDTO> handler) {
        this.onEnteredHandler = handler;
    }

    public void setOnCancelHandler(Runnable handler) {
        this.onCancelHandler = handler;
    }

    public void setOnDeletedHandler(Runnable handler) {
        this.onDeletedHandler = handler;
    }

    private String extractTekiyou(DrugAttrDTO attr) {
        return attr == null ? null : attr.tekiyou;
    }

    private DrugAttrDTO copyAttr(DrugAttrDTO src) {
        if (src == null) {
            return null;
        } else {
            return DrugAttrDTO.copy(src);
        }
    }

    private Node createCommands() {
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getStyleClass().add("commands");
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        Hyperlink deleteLink = new Hyperlink("削除");
        Hyperlink auxLink = new Hyperlink("他");
        enterButton.setOnAction(evt -> doEnter());
        cancelButton.setOnAction(evt -> onCancelHandler.run());
        deleteLink.setOnAction(evt -> doDelete());
        auxLink.setOnMouseClicked(evt -> doAux(evt, auxLink));
        hbox.getChildren().addAll(
                enterButton,
                cancelButton,
                deleteLink,
                tekiyouBox,
                auxLink
        );
        return hbox;
    }

    private boolean isGaiyou() {
        return input.getCategory() == DrugCategory.Gaiyou;
    }

    private void doAux(MouseEvent event, Node node) {
        ContextMenu contextMenu = createAuxContextMenu();
        contextMenu.show(node, event.getScreenX(), event.getScreenY());
    }

    private void adaptTekiyouBox(String tekiyouText) {
        boolean exists = tekiyouText != null && !tekiyouText.isEmpty();
        if (!exists) {
            Hyperlink tekiyouLink = new Hyperlink("摘要入力");
            tekiyouLink.setOnAction(evt -> doEnterTekiyou());
            tekiyouBox.getChildren().setAll(tekiyouLink);
        } else {
            Hyperlink editTekiyouLink = new Hyperlink("摘要編集");
            Hyperlink deleteTekiyouLink = new Hyperlink("摘要削除");
            editTekiyouLink.setOnAction(evt -> doEditTekiyou(tekiyouText));
            deleteTekiyouLink.setOnAction(evt -> doDeleteTekiyou());
            tekiyouBox.getChildren().setAll(editTekiyouLink, deleteTekiyouLink);
        }
    }

    private void doEnterTekiyou() {
        String curr = "";
        if (isGaiyou()) {
            curr = "１日２枚";
        }
        GuiUtil.askForString("摘要の内容", curr).ifPresent(str -> {
            this.attr = DrugAttrDTO.setTekiyou(drugId, attr, str);
            input.setTekiyou(str);
            adaptTekiyouBox(str);
        });
    }

    private void doEditTekiyou(String current) {
        GuiUtil.askForString("摘要の内容", current).ifPresent(str -> {
            this.attr = DrugAttrDTO.setTekiyou(drugId, attr, str);
            input.setTekiyou(str);
            adaptTekiyouBox(str);
        });

    }

    private void doDeleteTekiyou() {
        if (GuiUtil.confirm("現在の摘要を削除しますか？")) {
            this.attr = DrugAttrDTO.deleteTekiyou(drugId, attr);
            input.setTekiyou(null);
            adaptTekiyouBox(null);
        }
    }

    private ContextMenu createAuxContextMenu() {
        ContextMenu menu = new ContextMenu();
        {
            MenuItem item = new MenuItem("処方例に追加");
            item.setOnAction(evt -> doAddToPrescExample());
            menu.getItems().add(item);
        }
        return menu;
    }

    private void doAddToPrescExample() {
        Context.frontend.getDrugFull(input.getDrugId())
                .thenAcceptAsync(drugFull -> {
                    ConvertToPrescExampleDialog dialog = new ConvertToPrescExampleDialog(drugFull);
                    dialog.initOwner(getScene().getWindow());
                    dialog.initModality(Modality.WINDOW_MODAL);
                    dialog.show();
                }, Platform::runLater)
                .exceptionally(HandlerFX.exceptionally(this));
    }

    private void doEnter() {
        Validated<DrugDTO> validatedDrug = input.getDrug(visitId);
        if (validatedDrug.isFailure()) {
            AlertDialog.alert(validatedDrug.getErrorsAsString(), DrugEditForm.this);
            return;
        }
        DrugDTO drug = validatedDrug.getValue();
        drug.prescribed = 0;
        Context.frontend.updateDrugWithAttr(drug, attr)
                .thenAccept(ok -> closeForm())
                .exceptionally(HandlerFX.exceptionally(this));
    }

    private void doDelete() {
        if (GuiUtil.confirm("この処方を削除していいですか？")) {
            Context.frontend.deleteDrugCascading(getDrugId())
                    .thenAcceptAsync(ok -> onDeletedHandler.run(), Platform::runLater)
                    .exceptionally(HandlerFX.exceptionally(this));
        }
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

    private void closeForm() {
        class Local {
            private DrugFullDTO drugFull;
        }
        Local local = new Local();
        Frontend frontend = Context.frontend;
        frontend.getDrugFull(drugId)
                .thenCompose(drugFull -> {
                    local.drugFull = drugFull;
                    return frontend.getDrugAttr(drugId);
                })
                .thenAcceptAsync(attr -> {
                    onEnteredHandler.accept(local.drugFull, attr);
                }, Platform::runLater)
                .exceptionally(HandlerFX.exceptionally(this));

    }
}
