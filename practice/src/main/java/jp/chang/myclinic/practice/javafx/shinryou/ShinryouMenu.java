package jp.chang.myclinic.practice.javafx.shinryou;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.javafx.FunJavaFX;
import jp.chang.myclinic.practice.javafx.events.ConductEnteredEvent;
import jp.chang.myclinic.practice.javafx.events.ShinryouDeletedEvent;
import jp.chang.myclinic.practice.javafx.events.ShinryouEnteredEvent;
import jp.chang.myclinic.practice.lib.CFUtil;
import jp.chang.myclinic.practice.lib.PracticeUtil;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ShinryouMenu extends VBox {

    private int visitId;
    private LocalDate visitDate;
    private StackPane workarea = new StackPane();
    private Hyperlink mainLink;
    private BiConsumer<List<ShinryouFullDTO>, Map<Integer, ShinryouAttrDTO>> onShinryouEnteredHandler = (s, a) -> {
    };
    private Consumer<List<ConductFullDTO>> onConductsEnteredHandler = cc -> {
    };
    private Consumer<List<Integer>> onDeletedHandler = deletedShinryouIds -> {
    };

    public ShinryouMenu(VisitDTO visit) {
        super(4);
        this.visitId = visit.visitId;
        this.visitDate = DateTimeUtil.parseSqlDateTime(visit.visitedAt).toLocalDate();
        getChildren().addAll(
                createMenu()
        );
    }

    public void setOnShinryouEnteredHandler(BiConsumer<List<ShinryouFullDTO>,
            Map<Integer, ShinryouAttrDTO>> onShinryouEnteredHandler) {
        this.onShinryouEnteredHandler = onShinryouEnteredHandler;
    }

    public void setOnConductsEnteredHandler(Consumer<List<ConductFullDTO>> onConductsEnteredHandler) {
        this.onConductsEnteredHandler = onConductsEnteredHandler;
    }

    public void setOnDeletedHandler(Consumer<List<Integer>> onDeletedHandler) {
        this.onDeletedHandler = onDeletedHandler;
    }

    public void simulateEnterRegularChoice() {
        mainLink.fire();
    }

    private Node createMenu() {
        HBox hbox = new HBox(4);
        hbox.getChildren().addAll(
                createMainMenu(),
                createAuxMenu()
        );
        return hbox;
    }

    private Node createMainMenu() {
        this.mainLink = new Hyperlink("[診療行為]");
        mainLink.setOnAction(event -> doMainMenu());
        return mainLink;
    }

    private Node createAuxMenu() {
        Hyperlink auxLink = new Hyperlink("[+]");
        auxLink.setOnMouseClicked(event -> onAuxMenu(auxLink, event));
        return auxLink;
    }

    private void doMainMenu() {
        if (isWorkareaEmpty()) {
            if (PracticeUtil.confirmCurrentVisitAction(visitId, "診療行為を追加しますか？")) {
                AddRegularForm form = new AddRegularForm(visitId);
                form.setOnEnteredCallback((shinryouList, attrMap, conducts) -> {
                    onShinryouEnteredHandler.accept(shinryouList, attrMap);
                    onConductsEnteredHandler.accept(conducts);
                    hideWorkarea();
                });
                form.setOnCancelHandler(this::hideWorkarea);
                showWorkarea(form);
            }
        }
    }

    private void onAuxMenu(Node link, MouseEvent event) {
        if (isWorkareaEmpty()) {
            ContextMenu contextMenu = new ContextMenu();
            {
                MenuItem item = new MenuItem("検査");
                item.setOnAction(evt -> doKensa());
                contextMenu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("診療行為検索");
                item.setOnAction(evt -> doSearch());
                contextMenu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("全部コピー");
                item.setOnAction(evt -> doCopyAll());
                contextMenu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("選択コピー");
                item.setOnAction(evt -> doCopySelected());
                contextMenu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("複数削除");
                item.setOnAction(evt -> doDeleteSelected());
                contextMenu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("重複削除");
                item.setOnAction(evt -> doDeleteDuplicate());
                contextMenu.getItems().add(item);
            }
            contextMenu.show(link, event.getScreenX(), event.getScreenY());
        }
    }

    private void fireShinryouEnteredEvent(ShinryouFullDTO shinryou, ShinryouAttrDTO attr) {
        fireEvent(new ShinryouEnteredEvent(shinryou, attr));
    }

    private void fireShinryouDeletedEvent(ShinryouDTO shinryou) {
        fireEvent(new ShinryouDeletedEvent(shinryou));
    }

    private void fireConductEnteredEvent(ConductFullDTO conduct) {
        fireEvent(new ConductEnteredEvent(conduct));
    }

    private void doKensa() {
        if (PracticeUtil.confirmCurrentVisitAction(visitId, "診療行為を追加しますか？")) {
            AddKensaForm form = new AddKensaForm(visitId);
            form.setOnEnteredCallback((shinryouList, attrMap, conducts) -> {
                onShinryouEnteredHandler.accept(shinryouList, attrMap);
                onConductsEnteredHandler.accept(conducts);
                hideWorkarea();
            });
            showWorkarea(form);
        }
    }

    private void doSearch() {
        if (PracticeUtil.confirmCurrentVisitAction(visitId, "診療行為を追加しますか？")) {
            ShinryouEnterForm form = new ShinryouEnterForm(visitId, visitDate);
            form.setOnEnteredHandler(entered ->
                    onShinryouEnteredHandler.accept(List.of(entered), Collections.emptyMap()));
            form.setOnCloseHandler(this::hideWorkarea);
            showWorkarea(form);
        }
    }

    private void doCopyAll() {
        int targetVisitId = PracticeUtil.findCopyTarget(visitId);
        if (targetVisitId != 0) {
            ShinryouCopier copier = new ShinryouCopier(targetVisitId);
            copier.copyFromVisit(visitId)
                    .thenAcceptAsync(enteredList ->
                                    enteredList.forEach(e ->
                                            Context.integrationService.broadcastNewShinryou(e.shinryou, e.attr))
                            , Platform::runLater)
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

    private void doCopySelected() {
        if (isWorkareaEmpty()) {
            int targetVisitId = PracticeUtil.findCopyTarget(visitId);
            if (targetVisitId != 0) {
                Context.frontend.listShinryouFullWithAttr(visitId)
                        .thenAccept(shinryouList -> {
                            CopySelectedForm form = new CopySelectedForm(targetVisitId, shinryouList);
                            form.setOnEnteredHandler(enteredList -> {
                                enteredList.forEach(e ->
                                        Context.integrationService.broadcastNewShinryou(e.shinryou, e.attr));
                                hideWorkarea();
                            });
                            form.setOnCancelHandler(this::hideWorkarea);
                            Platform.runLater(() -> showWorkarea(form));
                        })
                        .exceptionally(HandlerFX::exceptionally);
            }
        }
    }

    private void doDeleteSelected() {
        if (isWorkareaEmpty()) {
            if (PracticeUtil.confirmCurrentVisitAction(visitId, "診療行為を削除しますか？")) {
                Context.frontend.listShinryouFullWithAttr(visitId)
                        .thenAccept(shinryouList -> {
                            DeleteSelectedForm form = new DeleteSelectedForm(shinryouList);
                            form.setOnCancelHandler(this::hideWorkarea);
                            form.setOnDeletedHandler(deletedShinryouIds -> {
                                onDeletedHandler.accept(deletedShinryouIds);
                                hideWorkarea();
                            });
                            Platform.runLater(() -> showWorkarea(form));
                        })
                        .exceptionally(HandlerFX::exceptionally);
            }
        }
    }

    private void doDeleteDuplicate() {
        Context.frontend.deleteDuplicateShinryou(visitId)
                .thenAcceptAsync(deletedShinryouIds -> onDeletedHandler.accept(deletedShinryouIds),
                        Platform::runLater)
                .exceptionally(HandlerFX::exceptionally);
    }

    private boolean isWorkareaEmpty() {
        return workarea.getChildren().size() == 0;
    }

    private void showWorkarea(Node content) {
        workarea.getChildren().clear();
        workarea.getChildren().add(content);
        getChildren().add(workarea);
    }

    private void hideWorkarea() {
        workarea.getChildren().clear();
        getChildren().remove(workarea);
    }

    public Optional<AddRegularForm> findAddRegularForm() {
        for (Node node : workarea.getChildren()) {
            if (node instanceof AddRegularForm) {
                return Optional.of((AddRegularForm) node);
            }
        }
        return Optional.empty();
    }
}
