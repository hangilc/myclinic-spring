package jp.chang.myclinic.practice.javafx.conduct;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.StringConverter;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.javafx.parts.OptionalWrapper;
import jp.chang.myclinic.practice.javafx.parts.WorkForm;
import jp.chang.myclinic.util.DrugUtil;
import jp.chang.myclinic.util.KizaiUtil;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

public class ConductEditForm extends WorkForm {

    private static Logger logger = LoggerFactory.getLogger(ConductEditForm.class);

    private LocalDate at;
    private ConductFullDTO conduct;
    private OptionalWrapper workarea = new OptionalWrapper();
    private VBox shinryouBox = new VBox(4);
    private VBox drugBox = new VBox(4);
    private VBox kizaiBox = new VBox(4);
    private Consumer<ConductFullDTO> onCloseHandler = c -> {};
    private Runnable onDeletedHandler = () -> {};

    public ConductEditForm(ConductFullDTO origConduct, LocalDate at) {
        super("処置の編集");
        this.at = at;
        this.conduct = ConductFullDTO.deepCopy(origConduct);
        getChildren().addAll(
                createTopMenu(),
                workarea,
                createKindInput(conduct.conduct.kind),
                createLabelInput(conduct.gazouLabel),
                createShinryouList(conduct.conductShinryouList),
                createDrugs(conduct.conductDrugs),
                createKizaiList(conduct.conductKizaiList),
                createCommands(conduct)
        );
    }

    public void setOnCloseHandler(Consumer<ConductFullDTO> onCloseHandler) {
        this.onCloseHandler = onCloseHandler;
    }

    public void setOnDeletedHandler(Runnable onDeletedHandler) {
        this.onDeletedHandler = onDeletedHandler;
    }

    private Node createTopMenu() {
        HBox hbox = new HBox(4);
        Hyperlink enterShinryouLink = new Hyperlink("診療行為追加");
        Hyperlink enterDrugLink = new Hyperlink("薬剤追加");
        Hyperlink enterKizaiLink = new Hyperlink("器材追加");
        enterShinryouLink.setOnAction(evt -> doEnterShinryou());
        enterDrugLink.setOnAction(evt -> doEnterDrug());
        enterKizaiLink.setOnAction(evt -> doEnterKizai());
        hbox.getChildren().addAll(enterShinryouLink, enterDrugLink, enterKizaiLink);
        return hbox;
    }

    private Node createKindInput(int kind) {
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        ChoiceBox<ConductKind> choiceBox = new ChoiceBox<>();
        choiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(ConductKind kind) {
                return kind.getKanjiRep();
            }

            @Override
            public ConductKind fromString(String string) {
                return null;
            }
        });
        choiceBox.getItems().addAll(ConductKind.values());
        choiceBox.getSelectionModel().select(ConductKind.fromCode(kind));
        choiceBox.valueProperty().addListener((obs, oldValue, newValue) ->
                onKindChange(choiceBox.getValue())
        );
        hbox.getChildren().addAll(new Label("種類："), choiceBox);
        return hbox;
    }

    private void onKindChange(ConductKind value) {
        Context.frontend.modifyConductKind(getConductId(), value.getCode())
                .thenAccept(result -> {
                    conduct.conduct.kind = value.getCode();
                })
                .exceptionally(HandlerFX.exceptionally(this));
    }

    private Node createLabelInput(GazouLabelDTO gazouLabelDTO) {
        return new GazouLabel(gazouLabelDTO, getConductId()){
            @Override
            protected void onModified(String value) {
                if( value == null ){
                    conduct.gazouLabel = null;
                } else {
                    if (conduct.gazouLabel != null) {
                        conduct.gazouLabel.label = value;
                    } else {
                        GazouLabelDTO dto = new GazouLabelDTO();
                        dto.label = value;
                        dto.conductId = getConductId();
                        conduct.gazouLabel = dto;
                    }
                }
            }
        };
    }

    private Node createShinryouList(List<ConductShinryouFullDTO> shinryouList) {
        shinryouList.forEach(this::addShinryou);
        return shinryouBox;
    }

    private void addShinryou(ConductShinryouFullDTO shinryou) {
        Text label = new Text(shinryou.master.name);
        Hyperlink deleteLink = new Hyperlink("削除");
        TextFlow textFlow = new TextFlow(label, new Text(" "), deleteLink);
        deleteLink.setOnAction(evt -> doDeleteShinryou(shinryou, textFlow));
        shinryouBox.getChildren().add(textFlow);
    }

    private void doDeleteShinryou(ConductShinryouFullDTO shinryou, Node disp){
        if( GuiUtil.confirm("この診療行為を削除しますか？") ){
            Context.frontend.deleteConductShinryou(shinryou.conductShinryou.conductShinryouId)
                    .thenAccept(result -> Platform.runLater(() -> {
                        shinryouBox.getChildren().remove(disp);
                        conduct.conductShinryouList.remove(shinryou);
                    }))
                    .exceptionally(HandlerFX.exceptionally(this));
        }
    }

    private Node createDrugs(List<ConductDrugFullDTO> drugs) {
        drugs.forEach(this::addDrug);
        return drugBox;
    }

    private void addDrug(ConductDrugFullDTO drug) {
        Text label = new Text(DrugUtil.conductDrugRep(drug));
        Hyperlink deleteLink = new Hyperlink("削除");
        TextFlow textFlow = new TextFlow(label, new Text(" "), deleteLink);
        deleteLink.setOnAction(evt -> doDeleteDrug(drug, textFlow));
        drugBox.getChildren().add(textFlow);
    }

    private void doDeleteDrug(ConductDrugFullDTO drug, TextFlow disp) {
        if( GuiUtil.confirm("この薬剤を削除しますか？") ){
            Context.frontend.deleteConductDrug(drug.conductDrug.conductDrugId)
                    .thenAccept(result -> Platform.runLater(() -> {
                        drugBox.getChildren().remove(disp);
                        conduct.conductDrugs.remove(drug);
                    }))
                    .exceptionally(HandlerFX.exceptionally(this));
        }
    }

    private Node createKizaiList(List<ConductKizaiFullDTO> kizaiList) {
        kizaiList.forEach(this::addKizai);
        return kizaiBox;
    }

    private void addKizai(ConductKizaiFullDTO kizai) {
        Text label = new Text(KizaiUtil.kizaiRep(kizai));
        Hyperlink deleteLink = new Hyperlink("削除");
        TextFlow textFlow = new TextFlow(label, new Text(" "), deleteLink);
        deleteLink.setOnAction(evt -> doDeleteKizai(kizai, textFlow));
        kizaiBox.getChildren().add(textFlow);
    }

    private void doDeleteKizai(ConductKizaiFullDTO kizai, TextFlow disp) {
        if( GuiUtil.confirm("この薬剤を削除しますか？") ){
            Context.frontend.deleteConductKizai(kizai.conductKizai.conductKizaiId)
                    .thenAccept(result -> Platform.runLater(() -> {
                        kizaiBox.getChildren().remove(disp);
                        conduct.conductKizaiList.remove(kizai);
                    }))
                    .exceptionally(HandlerFX.exceptionally(this));
        }
    }

    private Node createCommands(ConductFullDTO conduct) {
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        Button closeButton = new Button("閉じる");
        Hyperlink deleteLink = new Hyperlink("削除");
        closeButton.setOnAction(evt -> onCloseHandler.accept(conduct));
        deleteLink.setOnAction(evt -> doDelete());
        hbox.getChildren().addAll(closeButton, deleteLink);
        return hbox;
    }

    private void doDelete() {
        if( GuiUtil.confirm("この処置を削除しますか？") ) {
            Context.frontend.deleteConductCascading(getConductId())
                    .thenAccept(result -> Platform.runLater(() -> onDeletedHandler.run()))
                    .exceptionally(HandlerFX.exceptionally(this));
        }
    }

    private int getConductId() {
        return conduct.conduct.conductId;
    }

    private void doEnterShinryou() {
        if (workarea.isVisible()) {
            return;
        }
        ConductShinryouForm form = new ConductShinryouForm(at, getConductId());
        form.setOnCancelHandler(() -> workarea.hide());
        form.setOnEnteredHandler(entered -> {
            workarea.hide();
            conduct.conductShinryouList.add(entered);
            addShinryou(entered);
        });
        workarea.show(form);
    }

    private void doEnterDrug() {
        if (workarea.isVisible()) {
            return;
        }
        ConductDrugForm form = new ConductDrugForm(at, getConductId());
        form.setOnCancelHandler(workarea::hide);
        form.setOnEnteredHandler(entered -> {
            workarea.hide();
            conduct.conductDrugs.add(entered);
            addDrug(entered);
        });
        workarea.show(form);
    }

    private void doEnterKizai(){
        if( workarea.isVisible() ){
            return;
        }
        ConductKizaiForm form = new ConductKizaiForm(at, getConductId());
        form.setOnCancelHandler(workarea::hide);
        form.setOnEnteredHandler(entered -> {
            workarea.hide();
            conduct.conductKizaiList.add(entered);
            addKizai(entered);
        });
        workarea.show(form);
    }

}
