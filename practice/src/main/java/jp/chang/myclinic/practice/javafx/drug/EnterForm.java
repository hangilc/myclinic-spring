package jp.chang.myclinic.practice.javafx.drug;

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
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.dto.PrescExampleFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.javafx.drug2.SearchModeChooser;
import jp.chang.myclinic.practice.javafx.drug2.DrugSearchMode;
import jp.chang.myclinic.practice.javafx.events.DrugEnteredEvent;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class EnterForm extends VBox {

    private static Logger logger = LoggerFactory.getLogger(EnterForm.class);
    private Input input = new Input();
    private CheckBox daysFixedCheck = new CheckBox("固定");
    private int visitId;
    private Search search;

    EnterForm(VisitDTO visit) {
        this.visitId = visit.visitId;
        getStyleClass().add("drug-form");
        getStyleClass().add("form");
        daysFixedCheck.setSelected(true);
        input.addToDaysContent(daysFixedCheck);
        search = new Search(visit.patientId, visit.visitedAt) {
            @Override
            protected void onMasterSelect(IyakuhinMasterDTO master) {
                input.setMaster(master);
            }

            @Override
            protected void onExampleSelect(PrescExampleFullDTO example) {
                input.setMaster(example.master);
                try {
                    input.setAmount(Double.parseDouble(example.prescExample.amount));
                } catch (NumberFormatException ex) {
                    logger.error("Invalid presc example amount: {}", example.prescExample.amount);
                }
                input.setUsage(example.prescExample.usage);
                input.setCategory(example.prescExample.category);
                if (DrugCategory.fromCode(example.prescExample.category) != DrugCategory.Gaiyou) {
                    if (input.isDaysEmpty() || !isDaysFixed()) {
                        input.setDays(example.prescExample.days);
                    }
                }
            }

            @Override
            protected void onPrevSelect(DrugFullDTO drug) {
                input.setMaster(drug.master);
                input.setAmount(drug.drug.amount);
                input.setUsage(drug.drug.usage);
                input.setCategory(drug.drug.category);
                if (DrugCategory.fromCode(drug.drug.category) != DrugCategory.Gaiyou) {
                    if (input.isDaysEmpty() || !isDaysFixed()) {
                        input.setDays(drug.drug.days);
                    }
                }
            }
        };
        getChildren().addAll(createTitle(), input, createCommandBox(), search);

    }

    private Node createTitle() {
        Label title = new Label("新規処方の入力");
        title.setMaxWidth(Double.MAX_VALUE);
        title.getStyleClass().add("title");
        return title;
    }

    private Node createCommandBox() {
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

    protected void onClose() {

    }

    private boolean isDaysFixed() {
        return daysFixedCheck.isSelected();
    }

    private void doEnter() {
        input.convertToDrug(0, visitId, 0, (drug, errs) -> {
            if (errs.size() == 0) {
                Service.api.enterDrug(drug)
                        .thenCompose(Service.api::getDrugFull)
                        .thenAccept(enteredDrug -> Platform.runLater(() -> {
                            EnterForm.this.fireEvent(new DrugEnteredEvent(enteredDrug, null));
                            EnterForm.this.doClearInput();
                            search.clear();
                        }))
                        .exceptionally(HandlerFX::exceptionally);
            } else {
                GuiUtil.alertError(String.join("\n", errs));
            }
        });
    }

    private void doClearInput() {
        input.clearMaster();
        input.clearAmount();
        input.setUsage("");
        if (!isDaysFixed()) {
            input.clearDays();
        }
    }

}
