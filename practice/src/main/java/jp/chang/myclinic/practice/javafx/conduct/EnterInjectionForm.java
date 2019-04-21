package jp.chang.myclinic.practice.javafx.conduct;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.javafx.parts.SearchResult;
import jp.chang.myclinic.practice.javafx.parts.SearchTextBox;
import jp.chang.myclinic.practice.javafx.parts.WorkForm;
import jp.chang.myclinic.util.validator.Validated;
import jp.chang.myclinic.utilfx.AlertDialog;
import jp.chang.myclinic.utilfx.HandlerFX;
import jp.chang.myclinic.utilfx.RadioButtonGroup;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

public class EnterInjectionForm extends WorkForm {

    private final int visitId;
    private final LocalDate at;
    private DrugInput drugInput = new DrugInput();
    private RadioButtonGroup<ConductKind> kindGroup;
    private SearchResult<IyakuhinMasterDTO> searchResult;
    private Consumer<ConductFullDTO> onEnteredHandler = c -> {};
    private Runnable onCancelHandler = () -> {};

    EnterInjectionForm(int visitId, LocalDate at) {
        super("処置注射入力");
        this.visitId = visitId;
        this.at = at;
        getStyleClass().add("enter-injection-form");
        getChildren().addAll(
                drugInput,
                createKindInput(),
                createCommands(),
                createSearchTextInput(),
                createSearchResult()
        );
    }

    public void setOnEnteredHandler(Consumer<ConductFullDTO> onEnteredHandler) {
        this.onEnteredHandler = onEnteredHandler;
    }

    public void setOnCancelHandler(Runnable onCancelHandler) {
        this.onCancelHandler = onCancelHandler;
    }

    private Node createKindInput(){
        HBox hbox = new HBox(4);
        kindGroup = new RadioButtonGroup<>();
        kindGroup.createRadioButton("皮下・筋肉", ConductKind.HikaChuusha);
        kindGroup.createRadioButton("静脈 ", ConductKind.JoumyakuChuusha);
        kindGroup.createRadioButton("その他", ConductKind.OtherChuusha);
        kindGroup.setValue(ConductKind.HikaChuusha);
        hbox.getChildren().addAll(kindGroup.getButtons());
        return hbox;
    }

    private Node createCommands(){
        HBox hbox = new HBox(4);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        enterButton.setOnAction(evt -> doEnter());
        cancelButton.setOnAction(evt -> onCancelHandler.run());
        hbox.getChildren().addAll(enterButton, cancelButton);
        return hbox;
    }

    private Node createSearchTextInput(){
        return new SearchTextBox(){
            @Override
            protected void onEnter(String text) {
                Context.frontend.searchIyakuhinMaster(text, at)
                        .thenAccept(result -> Platform.runLater(() -> {
                            searchResult.setList(result);
                        }))
                        .exceptionally(HandlerFX.exceptionally(this));
            }
        };
    }

    private Node createSearchResult(){
        searchResult = new SearchResult<>();
        searchResult.setConverter(m -> m.name);
        searchResult.setOnSelectCallback(drugInput::setMaster);
        return searchResult;
    }

    private void doEnter(){
        BatchEnterRequestDTO request = new BatchEnterRequestDTO();
        ConductEnterRequestDTO conductReq = new ConductEnterRequestDTO();
        conductReq.visitId = visitId;
        conductReq.kind = kindGroup.getValue().getCode();
        Validated<ConductDrugDTO> validatedConductDrug = drugInput.getValidateToEnterWithoutConductId();
        if( validatedConductDrug.isFailure() ){
            AlertDialog.alert(validatedConductDrug.getErrorsAsString(), this);
            return;
        }
        ConductDrugDTO conductDrug = validatedConductDrug.getValue();
        conductReq.drugs = List.of(conductDrug);
        request.conducts = List.of(conductReq);
        Frontend frontend = Context.frontend;
        frontend.batchEnter(request)
                .thenCompose(result -> {
                    int conductId = result.conductIds.get(0);
                    return frontend.getConductFull(conductId);
                })
                .thenAcceptAsync(entered -> onEnteredHandler.accept(entered),
                        Platform::runLater)
                .exceptionally(HandlerFX.exceptionally(this));
    }

}
