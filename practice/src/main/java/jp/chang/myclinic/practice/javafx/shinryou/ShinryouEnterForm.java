package jp.chang.myclinic.practice.javafx.shinryou;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.dto.ShinryouDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.ShinryouMasterDTO;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.javafx.FunJavaFX;
import jp.chang.myclinic.practice.javafx.parts.SearchInputBox;
import jp.chang.myclinic.practice.javafx.parts.SearchResult;
import jp.chang.myclinic.practice.javafx.parts.WorkForm;
import jp.chang.myclinic.util.validator.Validated;
import jp.chang.myclinic.utilfx.AlertDialog;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.time.LocalDate;
import java.util.function.Consumer;

class ShinryouEnterForm extends WorkForm {

    private int visitId;
    private String at;
    private ShinryouInput shinryouInput;
    private SearchResult<ShinryouMasterDTO> searchResult;
    private Consumer<ShinryouFullDTO> onEnteredHandler = s -> {
    };
    private Runnable onCloseHandler = () -> {
    };

    ShinryouEnterForm(int visitId, LocalDate atDate) {
        super("診療行為検索");
        this.at = atDate.toString();
        this.visitId = visitId;
        getStyleClass().add("shinryou-form");
        getChildren().addAll(
                createInput(),
                createCommands(),
                createSearchInput(),
                createSearchResult()
        );
    }

    public void setOnEnteredHandler(Consumer<ShinryouFullDTO> onEnteredHandler) {
        this.onEnteredHandler = onEnteredHandler;
    }

    public void setOnCloseHandler(Runnable handler) {
        this.onCloseHandler = handler;
    }

    protected void doEnter() {
        Validated<ShinryouDTO> validatedShinryou = shinryouInput.getShinryouToEnter(visitId);
        if (validatedShinryou.isFailure()) {
            AlertDialog.alert(validatedShinryou.getErrorsAsString(), ShinryouEnterForm.this);
            return;
        }
        ShinryouDTO shinryou = validatedShinryou.getValue();
        Context.frontend.enterShinryou(shinryou)
                .thenCompose(Context.frontend::getShinryouFull)
                .thenAcceptAsync(entered -> {
                    onEnteredHandler.accept(entered);
                    shinryouInput.clear();
                }, Platform::runLater)
                .exceptionally(HandlerFX.exceptionally(this));
    }

    private Node createInput() {
        shinryouInput = new ShinryouInput();
        return shinryouInput;
    }

    private Node createCommands() {
        HBox hbox = new HBox(4);
        Button enterButton = new Button("入力");
        Button closeButton = new Button("閉じる");
        enterButton.setOnAction(evt -> doEnter());
        closeButton.setOnAction(evt -> onCloseHandler.run());
        hbox.getChildren().addAll(enterButton, closeButton);
        return hbox;
    }

    private Node createSearchInput() {
        SearchInputBox box = new SearchInputBox();
        box.setOnTextCallback(text -> {
            if (!text.isEmpty()) {
                Context.frontend.searchShinryouMaster(text, LocalDate.parse(at))
                        .thenAccept(result -> {
                            Platform.runLater(() -> searchResult.setList(result));
                        })
                        .exceptionally(ex -> {
                            FunJavaFX.createErrorHandler().accept(ex);
                            return null;
                        });
            }
        });
        return box;
    }

    private Node createSearchResult() {
        searchResult = new SearchResult<>();
        searchResult.setConverter(m -> m.name);
        searchResult.setOnSelectCallback(shinryouInput::setMaster);
        return searchResult;
    }

}
