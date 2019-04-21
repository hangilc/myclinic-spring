package jp.chang.myclinic.practice.javafx.conduct;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.dto.ConductKizaiDTO;
import jp.chang.myclinic.dto.ConductKizaiFullDTO;
import jp.chang.myclinic.dto.KizaiMasterDTO;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.util.validator.Validated;
import jp.chang.myclinic.utilfx.AlertDialog;
import jp.chang.myclinic.utilfx.HandlerFX;
import jp.chang.myclinic.practice.javafx.parts.SearchBox;
import jp.chang.myclinic.practice.javafx.parts.WorkForm;

import java.time.LocalDate;
import java.util.function.Consumer;

class ConductKizaiForm extends WorkForm {

    private int conductId;
    private KizaiInput kizaiInput = new KizaiInput();
    private Consumer<ConductKizaiFullDTO> onEnteredHandler = s -> {};
    private Runnable onCancelHandler = () -> {};

    ConductKizaiForm(LocalDate at, int conductId) {
        super("器材入力");
        this.conductId = conductId;
        SearchBox<KizaiMasterDTO> searchBox = new SearchBox<>(
                t -> Context.frontend.searchKizaiMaster(t, at),
                m -> m.name
        );
        searchBox.setOnSelectCallback(kizaiInput::setMaster);
        getChildren().addAll(
                kizaiInput,
                createCommandBox(),
                searchBox
        );
    }

    void setOnEnteredHandler(Consumer<ConductKizaiFullDTO> onEnteredHandler) {
        this.onEnteredHandler = onEnteredHandler;
    }

    void setOnCancelHandler(Runnable onCancelHandler) {
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
        Validated<ConductKizaiDTO> validated = kizaiInput.getValidatedToEnter(conductId);
        if( validated.isFailure() ){
            AlertDialog.alert(validated.getErrorsAsString(), this);
            return;
        }
        ConductKizaiDTO kizai = validated.getValue();
        Frontend frontend = Context.frontend;
        frontend.enterConductKizai(kizai)
                .thenCompose(frontend::getConductKizaiFull)
                .thenAcceptAsync(entered -> onEnteredHandler.accept(entered),
                        Platform::runLater)
                .exceptionally(HandlerFX.exceptionally(this));
    }

}
