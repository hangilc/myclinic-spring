package jp.chang.myclinic.practice.javafx.drug;

import javafx.application.Platform;
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

    public EditForm(DrugFullDTO drug, DrugAttrDTO attr, VisitDTO visit) {
        super(4);
        this.drugId = drug.drug.drugId;
        this.visitId = visit.visitId;
        getStyleClass().add("drug-form");
        getStyleClass().add("form");
        input.addRow(allFixedCheck);
        Search search = new Search(visit.patientId, visit.visitedAt){
            @Override
            protected void onMasterSelect(IyakuhinMasterDTO master) {
                input.setMaster(master);
            }

            @Override
            protected void onExampleSelect(PrescExampleFullDTO example) {
                input.setMaster(example.master);
                if( !isAllFixed() ){
                    try {
                        input.setAmount(Double.parseDouble(example.prescExample.amount));
                        input.setUsage(example.prescExample.usage);
                        input.setCategory(example.prescExample.category);
                        if(DrugCategory.fromCode(example.prescExample.category) != DrugCategory.Gaiyou) {
                            input.setDays(example.prescExample.days);
                        }
                    } catch(NumberFormatException ex){
                        logger.error("Invalid presc example amount: {}", example.prescExample.amount);
                    }
                }
            }

            @Override
            protected void onPrevSelect(DrugFullDTO drug) {
                input.setMaster(drug.master);
                if( !isAllFixed() ){
                    input.setAmount(drug.drug.amount);
                    input.setUsage(drug.drug.usage);
                    input.setCategory(drug.drug.category);
                    if(DrugCategory.fromCode(drug.drug.category) != DrugCategory.Gaiyou) {
                        input.setDays(drug.drug.days);
                    }
                }
            }
        };
        getChildren().addAll(createTitle(), input, createCommandBox(), search);
        input.setDrug(drug, attr);
    }

    private Node createTitle() {
        Label title = new Label("処方の編集");
        title.setMaxWidth(Double.MAX_VALUE);
        title.getStyleClass().add("title");
        return title;
    }

    private boolean isAllFixed(){
        return allFixedCheck.isSelected();
    }

    private Node createCommandBox() {
        HBox hbox = new HBox(4);
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

    private void doEnter(){
        input.convertToDrug(drugId, visitId, 0, (drug, errs) -> {
            if( errs.size() == 0 ){
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

    private void doClearInput(){
        input.clearMaster();
        if( !isAllFixed() ){
            input.clearAmount();
            input.setUsage("");
            input.clearDays();
        }
    }

    protected void onUpdated(DrugFullDTO updated){
        
    }

    protected void onClose(){

    }


}
