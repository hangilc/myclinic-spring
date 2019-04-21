package jp.chang.myclinic.practice.javafx.conduct;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.dto.ConductDrugDTO;
import jp.chang.myclinic.dto.ConductDrugFullDTO;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.javafx.parts.SearchBox;
import jp.chang.myclinic.practice.javafx.parts.WorkForm;
import jp.chang.myclinic.util.validator.Validated;
import jp.chang.myclinic.utilfx.AlertDialog;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.time.LocalDate;
import java.util.function.Consumer;

class ConductDrugForm extends WorkForm {

    private int conductId;
    private DrugInput drugInput = new DrugInput();
    private Consumer<ConductDrugFullDTO> onEnteredHandler = s -> {};
    private Runnable onCancelHandler = () -> {};

    ConductDrugForm(LocalDate at, int conductId) {
        super("薬剤追加");
        this.conductId = conductId;
        SearchBox<IyakuhinMasterDTO> searchBox = new SearchBox<>(
                t -> Context.frontend.searchIyakuhinMaster(t, at),
                m -> m.name
        );
        searchBox.setOnSelectCallback(drugInput::setMaster);
        getChildren().addAll(
                drugInput,
                createCommandBox(),
                searchBox
        );
    }

    public void setOnEnteredHandler(Consumer<ConductDrugFullDTO> onEnteredHandler) {
        this.onEnteredHandler = onEnteredHandler;
    }

    public void setOnCancelHandler(Runnable onCancelHandler) {
        this.onCancelHandler = onCancelHandler;
    }

    private Node createCommandBox(){
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        enterButton.setOnAction(evt ->doEnter());
        cancelButton.setOnAction(evt -> onCancelHandler.run());
        hbox.getChildren().addAll(enterButton, cancelButton);
        return hbox;
    }

    private void doEnter(){
        Validated<ConductDrugDTO> v = drugInput.getValidateToEnter(conductId);
        if( v.isFailure() ){
            AlertDialog.alert(v.getErrorsAsString(), this);
            return;
        }
        ConductDrugDTO drug = v.getValue();
        Frontend frontend = Context.frontend;
        frontend.enterConductDrug(drug)
                .thenCompose(frontend::getConductDrugFull)
                .thenAcceptAsync(entered -> onEnteredHandler.accept(entered),
                        Platform::runLater)
                .exceptionally(HandlerFX.exceptionally(this));
    }

}
