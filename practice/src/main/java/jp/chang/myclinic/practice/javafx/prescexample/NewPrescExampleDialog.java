package jp.chang.myclinic.practice.javafx.prescexample;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.dto.PrescExampleFullDTO;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.dto.PrescExampleDTO;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugSearchMode;
import jp.chang.myclinic.practice.javafx.drug.lib.SearchModeChooser;
import jp.chang.myclinic.util.validator.Validated;
import jp.chang.myclinic.utilfx.AlertDialog;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.time.LocalDate;

public class NewPrescExampleDialog extends PrescExampleBaseDialog {

    public NewPrescExampleDialog() {
        super(new SearchModeChooser(DrugSearchMode.Master, DrugSearchMode.Example));
        setTitle("処方例の新規入力");
        setSearchMode(DrugSearchMode.Master);
    }

    Node createCommands() {
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        Button enterButton = new Button("入力");
        Button closeButton = new Button("閉じる");
        Hyperlink clearLink = new Hyperlink("クリア");
        Hyperlink currentLink = new Hyperlink("現行");
        enterButton.setOnAction(evt -> doEnter());
        closeButton.setOnAction(evt -> close());
        clearLink.setOnAction(evt -> doClear());
        currentLink.setOnAction(evt -> doCurrent());
        hbox.getChildren().addAll(
                enterButton,
                closeButton,
                clearLink,
                currentLink
        );
        return hbox;
    }

    private void doCurrent() {
        int iyakuhincode = getInput().getIyakuhincode();
        if (iyakuhincode == 0) {
            GuiUtil.alertError("医薬品が選択されていません。");
            return;
        }
        Context.frontend.resolveStockDrug(iyakuhincode, LocalDate.now())
                .thenAcceptAsync(master -> {
                    getInput().updateMaster(master);
                }, Platform::runLater)
                .exceptionally(HandlerFX.exceptionally(this));
    }

    private void doEnter() {
        Validated<PrescExampleDTO> validated = getInput().getValidatedToEnter();
        if (validated.isFailure()) {
            AlertDialog.alert(validated.getErrorsAsString(), this);
            return;
        }
        PrescExampleDTO example = validated.getValue();
        Context.frontend.enterPrescExample(example)
                .thenAccept(prescExampleId -> Platform.runLater(this::doClear))
                .exceptionally(HandlerFX.exceptionally(this));
    }

}
