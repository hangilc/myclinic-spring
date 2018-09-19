package jp.chang.myclinic.practice.javafx.drug;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.javafx.events.DrugDeletedEvent;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditForm extends VBox {

    private static Logger logger = LoggerFactory.getLogger(EditForm.class);
    private Input input = new Input();
    private CheckBox allFixedCheck = new CheckBox("用量・用法・日数をそのままに");
    private int drugId;
    private int visitId;
    private StringProperty tekiyou = new SimpleStringProperty();

    public EditForm(DrugFullDTO drug, String drugTekiyou, VisitDTO visit) {
        super(4);
        this.drugId = drug.drug.drugId;
        this.visitId = visit.visitId;
        getStyleClass().add("drug-form");
        getStyleClass().add("form");
        input.addRow(allFixedCheck);
        input.tekiyouProperty().bind(tekiyou);
        Search search = new Search(visit.patientId, visit.visitedAt) {
            @Override
            protected void onMasterSelect(IyakuhinMasterDTO master) {
                input.setMaster(master);
            }

            @Override
            protected void onExampleSelect(PrescExampleFullDTO example) {
                input.setMaster(example.master);
                if (!isAllFixed()) {
                    try {
                        input.setAmount(Double.parseDouble(example.prescExample.amount));
                        input.setUsage(example.prescExample.usage);
                        input.setCategory(example.prescExample.category);
                        if (DrugCategory.fromCode(example.prescExample.category) != DrugCategory.Gaiyou) {
                            input.setDays(example.prescExample.days);
                        }
                    } catch (NumberFormatException ex) {
                        logger.error("Invalid presc example amount: {}", example.prescExample.amount);
                    }
                }
            }

            @Override
            protected void onPrevSelect(DrugFullDTO drug) {
                input.setMaster(drug.master);
                if (!isAllFixed()) {
                    input.setAmount(drug.drug.amount);
                    input.setUsage(drug.drug.usage);
                    input.setCategory(drug.drug.category);
                    if (DrugCategory.fromCode(drug.drug.category) != DrugCategory.Gaiyou) {
                        input.setDays(drug.drug.days);
                    }
                }
            }
        };
        getChildren().addAll(createTitle(), input, createCommandBox(), search);
        input.setDrug(drug);
        tekiyou.setValue(drugTekiyou);
    }

    private Node createTitle() {
        Label title = new Label("処方の編集");
        title.setMaxWidth(Double.MAX_VALUE);
        title.getStyleClass().add("title");
        return title;
    }

    private boolean isAllFixed() {
        return allFixedCheck.isSelected();
    }

    private Node createEditTekiyouCommands() {
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        Hyperlink editTekiyouLink = new Hyperlink("摘要編集");
        Hyperlink deleteTekiyouLink = new Hyperlink("摘要削除");
        editTekiyouLink.setOnAction(evt -> doEnterTekiyou());
        deleteTekiyouLink.setOnAction(evt -> doDeleteTekiyou());
        hbox.getChildren().addAll(editTekiyouLink, deleteTekiyouLink);
        return hbox;
    }

    private Node createEnterTekiyouCommands() {
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        Hyperlink enterTekiyouLink = new Hyperlink("摘要入力");
        enterTekiyouLink.setOnAction(evt -> doEnterTekiyou());
        hbox.getChildren().add(enterTekiyouLink);
        return hbox;
    }

    private void doEnterTekiyou() {
        String curr = tekiyou.getValue();
        if (curr == null) {
            curr = "";
        }
        GuiUtil.askForString("摘要の内容", curr).ifPresent(str -> {
            Service.api.setDrugTekiyou(drugId, str)
                    .thenAccept(ok -> {
                        Platform.runLater(() -> {
                            EditForm.this.tekiyou.setValue(str);
                            onTekiyouModified(str);
                        });
                    })
                    .exceptionally(HandlerFX::exceptionally);
        });
    }

    private void doDeleteTekiyou() {
        if (GuiUtil.confirm("現在の摘要を削除しますか？")) {
            Service.api.deleteDrugTekiyou(drugId)
                    .thenAccept(ok -> {
                        Platform.runLater(() -> {
                            EditForm.this.tekiyou.setValue(null);
                            onTekiyouModified(null);
                        });
                    })
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

    private void setNodeVisible(Node node, boolean visible) {
        node.setVisible(visible);
        node.setManaged(visible);
    }

    private Node createCommandBox() {
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getStyleClass().add("commands");
        Button enterButton = new Button("入力");
        Button closeButton = new Button("閉じる");
        Hyperlink clearLink = new Hyperlink("クリア");
        Hyperlink deleteLink = new Hyperlink("削除");
        deleteLink.setOnAction(evt -> doDelete());
        enterButton.setOnAction(event -> doEnter());
        closeButton.setOnAction(event -> onClose());
        clearLink.setOnAction(event -> doClearInput());
        Node editTekiyou = createEditTekiyouCommands();
        Node enterTekiyou = createEnterTekiyouCommands();
        setNodeVisible(editTekiyou, false);
        setNodeVisible(enterTekiyou, true);
        hbox.getChildren().addAll(enterButton, closeButton, clearLink,
                deleteLink, editTekiyou, enterTekiyou);
        tekiyou.addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                setNodeVisible(enterTekiyou, false);
                setNodeVisible(editTekiyou, true);
            } else {
                setNodeVisible(enterTekiyou, true);
                setNodeVisible(editTekiyou, false);
            }
        });
        return hbox;
    }

    private void doEnter() {
        input.convertToDrug(drugId, visitId, 0, (drug, errs) -> {
            if (errs.size() == 0) {
                Service.api.updateDrug(drug)
                        .thenCompose(ok -> Service.api.getDrugFull(drugId))
                        .thenAccept(updated ->
                                Platform.runLater(() -> onUpdated(updated))
                        )
                        .exceptionally(HandlerFX::exceptionally);
            } else {
                GuiUtil.alertError(String.join("\n", errs));
            }
        });
    }

    private void doClearInput() {
        input.clearMaster();
        if (!isAllFixed()) {
            input.clearAmount();
            input.setUsage("");
            input.clearDays();
        }
    }

    private void doDelete() {
        if (GuiUtil.confirm("この処方を削除していいですか？")) {
            class Local {
                private DrugDTO drug;
            }
            Local local = new Local();
            Service.api.getDrug(drugId)
                    .thenCompose(drugDTO -> {
                        local.drug = drugDTO;
                        return Service.api.deleteDrug(drugId);
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

}
