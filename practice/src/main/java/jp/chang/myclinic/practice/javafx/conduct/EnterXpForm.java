package jp.chang.myclinic.practice.javafx.conduct;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.BatchEnterByNamesRequestDTO;
import jp.chang.myclinic.dto.ConductFullDTO;
import jp.chang.myclinic.dto.EnterConductByNamesRequestDTO;
import jp.chang.myclinic.dto.EnterConductKizaiByNamesRequestDTO;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.javafx.parts.WorkForm;
import jp.chang.myclinic.practice.lib.PracticeUtil;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EnterXpForm extends WorkForm {

    private final int visitId;
    private Supplier<String> gazouLabelSupplier;
    private Supplier<String> filmSupplier;
    private Consumer<ConductFullDTO> onEnteredHandler = c -> {};
    private Runnable onCancelHandler = () -> {};

    public EnterXpForm(int visitId) {
        super("X線入力");
        this.visitId = visitId;
        getChildren().addAll(
                createLabelInput(),
                createFilmInput(),
                createCommands()
        );
    }

    public void setOnEnteredHandler(Consumer<ConductFullDTO> onEnteredHandler) {
        this.onEnteredHandler = onEnteredHandler;
    }

    public void setOnCancelHandler(Runnable onCancelHandler) {
        this.onCancelHandler = onCancelHandler;
    }

    private Node createLabelInput(){
        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll(PracticeUtil.gazouLabelExamples);
        combo.getSelectionModel().select(0);
        combo.setEditable(true);
        gazouLabelSupplier = () -> combo.getSelectionModel().getSelectedItem();
        return combo;
    }

    private Node createFilmInput(){
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("半切", "大角", "四ツ切");
        choiceBox.getSelectionModel().select("大角");
        filmSupplier = () -> choiceBox.getSelectionModel().getSelectedItem();
        return choiceBox;
    }

    private Node createCommands(){
        HBox hbox = new HBox(4);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        enterButton.setOnAction(evt -> doEnter(gazouLabelSupplier.get(), filmSupplier.get()));
        cancelButton.setOnAction(evt -> onCancelHandler.run());
        hbox.getChildren().addAll(enterButton, cancelButton);
        return hbox;
    }

    private void doEnter(String gazouLabel, String film){
        EnterConductByNamesRequestDTO conductReq = new EnterConductByNamesRequestDTO();
        conductReq.kind = ConductKind.Gazou.getCode();
        conductReq.gazouLabel = gazouLabel;
        conductReq.shinryouNames = List.of("単純撮影", "単純撮影診断");
        conductReq.kizaiList = List.of(EnterConductKizaiByNamesRequestDTO.create(film, 1));
        BatchEnterByNamesRequestDTO request = new BatchEnterByNamesRequestDTO();
        request.conducts = List.of(conductReq);
        Frontend frontend = Context.frontend;
        frontend.batchEnterByNames(visitId, request)
                .thenCompose(result -> {
                    int conductId = result.conductIds.get(0);
                    return frontend.getConductFull(conductId);
                })
                .thenAcceptAsync(entered -> onEnteredHandler.accept(entered), Platform::runLater)
                .exceptionally(HandlerFX::exceptionally);
    }

}
