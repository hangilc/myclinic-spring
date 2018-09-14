package jp.chang.myclinic.practice.javafx.prescexample;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.PrescExampleDTO;
import jp.chang.myclinic.practice.javafx.drug2.*;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.time.LocalDate;
import java.util.List;

public class NewPrescExampleDialog extends Stage {

    //private static Logger logger = LoggerFactory.getLogger(NewPrescExampleDialog.class);
    private Input input = new Input();
    private SearchInput searchInput = new SearchInput();
    private SearchResult searchResult = new SearchResult();
    private SearchModeChooser searchModeChooser = new SearchModeChooser(
            List.of(DrugSearchMode.Master, DrugSearchMode.Example)
    );
    private TextField commentInput = new TextField();
    private LocalDate at = LocalDate.now();

    public NewPrescExampleDialog() {
        setTitle("処方例の新規入力");
        Parent mainPane = createMainPane();
        mainPane.getStyleClass().add("new-presc-example-dialog");
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

    private PrescExampleDTO createPrescExample() {
        PrescExampleDTO ex = new PrescExampleDTO();
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

    private void doClear(){
        input.clear();
        commentInput.setText("");
    }

    private Node createCommands() {
        HBox hbox = new HBox(4);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        Hyperlink clearLink = new Hyperlink("クリア");
        enterButton.setOnAction(evt -> doEnter());
        cancelButton.setOnAction(evt -> close());
        clearLink.setOnAction(evt -> doClear());
        hbox.getChildren().addAll(
                enterButton,
                cancelButton,
                clearLink
        );
        return hbox;
    }

    private void doEnter() {
        PrescExampleDTO ex = createPrescExample();
        if (ex != null) {
            Service.api.resolveIyakuhinMaster(ex.iyakuhincode, at.toString())
                    .thenCompose(master -> {
                        ex.masterValidFrom = master.validFrom;
                        return Service.api.enterPrescExample(ex);
                    })
                    .thenAccept(prescExampleId -> Platform.runLater(() -> {
                        input.clear();
                        commentInput.setText("");
                        searchInput.clear();
                        searchResult.getItems().clear();
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

}
