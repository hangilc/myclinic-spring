package jp.chang.myclinic.practice.javafx.prescexample;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.dto.PrescExampleDTO;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugSearchMode;
import jp.chang.myclinic.practice.javafx.drug.lib.SearchModeChooser;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugSearcher;
import jp.chang.myclinic.util.validator.Validated;
import jp.chang.myclinic.utilfx.AlertDialog;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;

public class EditPrescExampleDialog extends PrescExampleBaseDialog {

    public EditPrescExampleDialog() {
        super(new SearchModeChooser(DrugSearchMode.Example));
        setTitle("処方例の編集");
        setSearchMode(DrugSearchMode.Example);
        addToSearchTextInputBox(createListAllLink());
    }

    @Override
    Node createCommands() {
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        Button enterButton = new Button("適用");
        Button closeButton = new Button("閉じる");
        Hyperlink clearLink = new Hyperlink("クリア");
        Hyperlink deleteLink = new Hyperlink("削除");
        enterButton.setOnAction(evt -> doUpdate());
        closeButton.setOnAction(evt -> close());
        clearLink.setOnAction(evt -> doClear());
        deleteLink.setOnAction(evt -> doDelete());
        hbox.getChildren().addAll(
                enterButton,
                closeButton,
                clearLink,
                deleteLink
        );
        return hbox;
    }

    private Node createListAllLink() {
        Hyperlink listAllLink = new Hyperlink("全リスト");
        listAllLink.setOnAction(evt -> doListAll());
        return listAllLink;
    }

    private void doListAll() {
        DrugSearcher.listAllExamples(ex -> getInput().setPrescExample(ex))
                .thenAcceptAsync(result -> getSearchResult().setItems(result), Platform::runLater)
                .exceptionally(HandlerFX.exceptionally(this));
    }

    private void doDelete() {
        int prescExampleId = getPrescExampleId();
        if (prescExampleId == 0) {
            GuiUtil.alertError("削除する処方例が選択されていません。");
            return;
        }
        if (!GuiUtil.confirm("この処方例を削除しますか？")) {
            return;
        }
        Context.frontend.deletePrescExample(prescExampleId)
                .thenAcceptAsync(result -> doClear(), Platform::runLater)
                .exceptionally(HandlerFX.exceptionally(this));
    }

    private void doUpdate() {
        Validated<PrescExampleDTO> validated = getInput().getValidatedToUpdate();
        if (validated.isFailure()) {
            AlertDialog.alert(validated.getErrorsAsString(), this);
            return;
        }
        PrescExampleDTO ex = validated.getValue();
        Context.frontend.updatePrescExample(ex)
                .thenAcceptAsync(result -> doClear(), Platform::runLater)
                .exceptionally(HandlerFX.exceptionally(this));
    }

}
