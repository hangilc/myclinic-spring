package jp.chang.myclinic.practice.javafx.conduct;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.dto.ConductShinryouDTO;
import jp.chang.myclinic.dto.ConductShinryouFullDTO;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.util.validator.Validated;
import jp.chang.myclinic.utilfx.AlertDialog;
import jp.chang.myclinic.utilfx.HandlerFX;
import jp.chang.myclinic.practice.javafx.parts.EnterCancelBox;
import jp.chang.myclinic.practice.javafx.parts.ShinryouSearchBox;
import jp.chang.myclinic.practice.javafx.parts.WorkForm;

import java.time.LocalDate;
import java.util.function.Consumer;

public class ConductShinryouForm extends WorkForm {

    private int conductId;
    private ShinryouInput shinryouInput = new ShinryouInput();
    private Consumer<ConductShinryouFullDTO> onEnteredHandler = s -> {};
    private Runnable onCancelHandler = () -> {};

    public ConductShinryouForm(LocalDate at, int conductId){
        super("診療行為追加");
        this.conductId = conductId;
        ShinryouSearchBox searchBox = new ShinryouSearchBox(at);
        searchBox.setOnSelectCallback(shinryouInput::setMaster);
        getChildren().addAll(
                shinryouInput,
                createCommandBox(),
                searchBox
        );
    }

    public void setOnEnteredHandler(Consumer<ConductShinryouFullDTO> onEnteredHandler) {
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
        Validated<ConductShinryouDTO> validated = shinryouInput.getValidatedToEnter(conductId);
        if( validated.isFailure() ){
            AlertDialog.alert(validated.getErrorsAsString(), this);
            return;
        }
        ConductShinryouDTO shinryou = validated.getValue();
        Frontend frontend = Context.frontend;
        frontend.enterConductShinryou(shinryou)
                .thenCompose(frontend::getConductShinryouFull)
                .thenAcceptAsync(entered -> onEnteredHandler.accept(entered),
                        Platform::runLater)
                .exceptionally(HandlerFX::exceptionally);
    }

}
