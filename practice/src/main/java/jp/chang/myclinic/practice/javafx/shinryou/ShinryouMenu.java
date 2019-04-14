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
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.javafx.FunJavaFX;
import jp.chang.myclinic.utilfx.HandlerFX;
import jp.chang.myclinic.practice.javafx.events.ConductEnteredEvent;
import jp.chang.myclinic.practice.javafx.events.ShinryouDeletedEvent;
import jp.chang.myclinic.practice.javafx.events.ShinryouEnteredEvent;
import jp.chang.myclinic.practice.lib.CFUtil;
import jp.chang.myclinic.practice.lib.PracticeUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ShinryouMenu extends VBox {

    private int visitId;
    private String visitedAt;
    private StackPane workarea = new StackPane();
    private Hyperlink mainLink;
    private BiConsumer<List<ShinryouFullDTO>, Map<Integer, ShinryouAttrDTO>> onShinryouEnteredHandler = (s, a) -> {
    };
    private Consumer<List<ConductFullDTO>> onConductsEnteredHandler = cc -> {
    };

    public ShinryouMenu(VisitDTO visit) {
        super(4);
        this.visitId = visit.visitId;
        this.visitedAt = visit.visitedAt;
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
            AddKensaForm form = new AddKensaForm() {
                @Override
                protected void onEnter(List<String> selected) {
                    FunJavaFX.batchEnterShinryouByNames(visitId, selected, result -> {
                        Platform.runLater(() -> {
                            result.shinryouList.forEach(shinryou ->
                                    fireShinryouEnteredEvent(shinryou, result.attrMap.get(shinryou.shinryou.shinryouId)));
                            result.conducts.forEach(conduct -> fireConductEnteredEvent(conduct));
                            hideWorkarea();
                        });
                    });
                }

                @Override
                protected void onCancel(AddKensaForm form) {
                    hideWorkarea();
                }
            };
            showWorkarea(form);
        }
    }

    private void doSearch() {
        if (PracticeUtil.confirmCurrentVisitAction(visitId, "診療行為を追加しますか？")) {
            ShinryouEnterForm form = new ShinryouEnterForm(visitedAt, visitId) {

                @Override
                protected void onClose(ShinryouForm form) {
                    hideWorkarea();
                }
            };
            showWorkarea(form);
        }
    }

    private void doCopyAll() {
        int targetVisitId = PracticeUtil.findCopyTarget(visitId);
        if (targetVisitId != 0) {
            Context.frontend.listShinryouFull(visitId)
                    .thenAccept(srcList -> {
                        FunJavaFX.batchCopyShinryou(targetVisitId, srcList,
                                (entered, attr) -> {
                                    Platform.runLater(() -> fireShinryouEnteredEvent(entered, attr));
                                },
                                () -> {
                                });
                    });
        }
    }

    private void doCopySelected() {
        if (isWorkareaEmpty()) {
            int targetVisitId = PracticeUtil.findCopyTarget(visitId);
            if (targetVisitId != 0) {
                Context.frontend.listShinryouFull(visitId)
                        .thenAccept(shinryouList -> {
                            CopySelectedForm form = new CopySelectedForm(shinryouList) {
                                @Override
                                protected void onEnter(HandleSelectedForm form, List<ShinryouFullDTO> selection) {
                                    FunJavaFX.batchCopyShinryou(targetVisitId, selection,
                                            (entered, attr) -> {
                                                Platform.runLater(() -> fireShinryouEnteredEvent(entered, attr));
                                            }, () -> hideWorkarea());
                                }

                                @Override
                                protected void onCancel(HandleSelectedForm form) {
                                    hideWorkarea();
                                }
                            };
                            Platform.runLater(() ->

                                    showWorkarea(form));
                        })
                        .exceptionally(HandlerFX::exceptionally);
            }
        }
    }

    private void doDeleteSelected() {
        if (isWorkareaEmpty()) {
            if (PracticeUtil.confirmCurrentVisitAction(visitId, "診療行為を削除しますか？")) {
                Context.frontend.listShinryouFull(visitId)
                        .thenAccept(shinryouList -> {
                            DeleteSelectedForm form = new DeleteSelectedForm(shinryouList) {
                                private CompletableFuture<Void> deleteShinryou(ShinryouFullDTO shinryou) {
                                    return Context.frontend.deleteShinryou(shinryou.shinryou.shinryouId)
                                            .thenAccept(result -> Platform.runLater(() ->
                                                    fireShinryouDeletedEvent(shinryou.shinryou)));
                                }

                                @Override
                                protected void onEnter(HandleSelectedForm form, List<ShinryouFullDTO> selection) {
                                    CFUtil.forEach(selection, this::deleteShinryou)
                                            .thenAccept(result -> Platform.runLater(() -> hideWorkarea()))
                                            .exceptionally(HandlerFX::exceptionally);
                                }

                                @Override
                                protected void onCancel(HandleSelectedForm form) {
                                    hideWorkarea();
                                }
                            };
                            Platform.runLater(() -> showWorkarea(form));
                        })
                        .exceptionally(HandlerFX::exceptionally);
            }
        }
    }

    private void doDeleteDuplicate() {
        Context.frontend.deleteDuplicateShinryou(visitId)
                .thenAccept(shinryouIds -> Platform.runLater(() -> {
                    shinryouIds.forEach(shinryouId -> {
                        ShinryouDeletedEvent e = new ShinryouDeletedEvent(visitId, shinryouId);
                        ShinryouMenu.this.fireEvent(e);
                    });
                }))
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
