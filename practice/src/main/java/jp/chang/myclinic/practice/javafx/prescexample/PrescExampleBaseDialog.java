package jp.chang.myclinic.practice.javafx.prescexample;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.PrescExampleDTO;
import jp.chang.myclinic.practice.javafx.drug2.*;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

abstract class PrescExampleBaseDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(PrescExampleBaseDialog.class);
    private Input input = new Input();
    private SearchInput searchInput = new SearchInput();
    private SearchResult searchResult = new SearchResult();
    private SearchModeChooser searchModeChooser;
    private TextField commentInput = new TextField();
    private LocalDate at = LocalDate.now();

    PrescExampleBaseDialog(SearchModeChooser searchModeChooser) {
        this.searchModeChooser = searchModeChooser;
        Parent mainPane = createMainPane();
        mainPane.getStyleClass().add("presc-example-dialog");
        mainPane.getStylesheets().add("css/Practice.css");
        setScene(new Scene(mainPane));
    }

    private Parent createMainPane() {
        VBox vbox = new VBox(4);
        {
            input.addRow(new Label("注釈："), commentInput);
        }
        {
            HBox hbox = new HBox(4);
            searchModeChooser.setValue(DrugSearchMode.Master);
            hbox.getChildren().addAll(searchModeChooser.getButtons());
            searchInput.getChildren().add(hbox);
        }
        searchInput.setOnSearchHandler(() -> {
            String text = searchInput.getSearchText().trim();
            if (!text.isEmpty()) {
                DrugSearchMode mode = searchModeChooser.getValue();
                DrugSearcher.search(text, mode, at)
                        .thenAccept(result -> Platform.runLater(() -> {
                            searchResult.getItems().clear();
                            searchResult.getItems().addAll(result);
                        }))
                        .exceptionally(HandlerFX::exceptionally);
            }
        });
        searchResult.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                input.setData(newValue, Input.SetOption.IgnoreNull);
                String comment = newValue.getComment();
                if( comment != null && !comment.isEmpty()) {
                    commentInput.setText(comment);
                }
            }
        });
        vbox.getChildren().addAll(
                input,
                new Separator(),
                createCommands(),
                new Separator(),
                searchInput,
                searchResult
        );
        return vbox;
    }

    abstract Node createCommands();

    void setSearchMode(DrugSearchMode mode){
        searchModeChooser.setValue(mode);
    }

    PrescExampleDTO createPrescExample() {
        PrescExampleDTO ex = new PrescExampleDTO();
        ex.prescExampleId = input.getPrescExampleId();
        ex.iyakuhincode = input.getIyakuhincode();
        if (ex.iyakuhincode == 0) {
            GuiUtil.alertError("医薬品が設定されていません。");
            return null;
        }
        ex.amount = input.getAmount();
        try {
            double value = Double.parseDouble(ex.amount);
            if (!(value > 0)) {
                GuiUtil.alertError("用量の値が正でありません。");
                return null;
            }
        } catch (NumberFormatException e) {
            GuiUtil.alertError("用量の入力が不適切です。");
            return null;
        }
        ex.usage = input.getUsage();
        DrugCategory category = input.getCategory();
        ex.category = category.getCode();
        if (category == DrugCategory.Gaiyou) {
            ex.days = 1;
        } else {
            try {
                ex.days = Integer.parseInt(input.getDays());
                if (!(ex.days > 0)) {
                    GuiUtil.alertError("日数の値が正の整数でありません。");
                    return null;
                }
            } catch (NumberFormatException e) {
                GuiUtil.alertError("日数の入力が不敵津です。");
                return null;
            }
        }
        ex.comment = commentInput.getText();
        return ex;
    }

    void doClear(){
        input.clear();
        commentInput.setText("");
        searchInput.clear();
        searchResult.getItems().clear();
    }

    LocalDate getLocalDate(){
        return at;
    }

}
