package jp.chang.myclinic.practice.javafx.drug;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.DrugAttrDTO;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.lib.PracticeService;
import jp.chang.myclinic.practice.lib.PracticeUtil;
import jp.chang.myclinic.util.DrugUtil;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class DrugMenu extends VBox {

    private StackPane workarea = new StackPane();
    private Hyperlink mainMenu;
    private BiConsumer<DrugFullDTO, DrugAttrDTO> onDrugEnteredHandler = (drug, attr) -> {
    };
    private Consumer<List<Integer>> onDrugDeletedHandler = drugIds -> {
    };
    private BiConsumer<DrugDTO, Integer> onDrugDaysModifiedHandler = (drug, days) -> {};

    public DrugMenu(VisitDTO visit) {
        super(4);
        workarea.setVisible(false);
        workarea.setManaged(false);
        getChildren().addAll(
                createMenu(visit),
                workarea
        );
    }

    public void setOnDrugEnteredHandler(BiConsumer<DrugFullDTO, DrugAttrDTO> handler) {
        this.onDrugEnteredHandler = handler;
    }

    public void setOnDrugDeletedHandler(Consumer<List<Integer>> handler) {
        this.onDrugDeletedHandler = handler;
    }

    public void setOnDrugDaysModifiedHandler(BiConsumer<DrugDTO, Integer> handler){
        this.onDrugDaysModifiedHandler = handler;
    }

    public void simulateNewDrugButtonClick() {
        mainMenu.fire();
    }

    private Node createMenu(VisitDTO visit) {
        HBox hbox = new HBox(4);
        this.mainMenu = new Hyperlink("[処方]");
        Hyperlink auxMenuLink = new Hyperlink("[+]");
        mainMenu.setOnAction(event -> {
            if (isWorkareaEmpty()) {
                if (!PracticeUtil.confirmCurrentVisitAction(visit.visitId, "処方を追加しますか？")) {
                    return;
                }
                DrugEnterForm form = new DrugEnterForm(visit);
                form.setOnCloseHandler(this::hideWorkarea);
                form.setOnDrugEnteredHandler(onDrugEnteredHandler);
                showWorkarea(form);
            }
        });
        auxMenuLink.setOnMouseClicked(event -> {
            if (isWorkareaEmpty()) {
                ContextMenu contextMenu = createAuxMenu(visit.visitId);
                contextMenu.show(auxMenuLink, event.getScreenX(), event.getScreenY());
            }
        });
        hbox.getChildren().addAll(mainMenu, auxMenuLink);
        return hbox;
    }

    private ContextMenu createAuxMenu(int visitId) {
        ContextMenu menu = new ContextMenu();
        menu.getItems().addAll(
                createCopyAllMenuItem(visitId),
                createCopySelectedMenuItem(visitId),
                createModifyDaysMenuItem(visitId),
                createDeleteSelectedMenuItem(visitId),
                createDrugTextMenuItem(visitId)
        );
        return menu;
    }

    private MenuItem createCopyAllMenuItem(int visitId) {
        MenuItem item = new MenuItem("全部コピー");
        item.setOnAction(event -> {
            int targetVisitId = PracticeUtil.findCopyTarget(visitId);
            if (targetVisitId == 0) {
                return;
            }
            PracticeService.listDrugFull(visitId)
                    .thenAccept(drugs -> {
                        new DrugsCopier(targetVisitId, drugs,
                                Context.integrationService::broadcastNewDrug,
                                //(enteredDrug, attr) -> fireEvent(new DrugEnteredEvent(enteredDrug, attr)),
                                () -> {
                                }
                        );
                    });
        });
        return item;
    }

    private MenuItem createCopySelectedMenuItem(int visitId) {
        MenuItem item = new MenuItem("部分コピー");
        class Local {
            private List<DrugFullDTO> fullDrugs;
        }
        Local local = new Local();
        item.setOnAction(evt -> {
            if (!isWorkareaEmpty()) {
                return;
            }
            int targetVisitId = PracticeUtil.findCopyTarget(visitId);
            if (targetVisitId == 0) {
                return;
            }
            Context.frontend.listDrugFull(visitId)
                    .thenCompose(drugs -> {
                        local.fullDrugs = drugs;
                        List<Integer> drugIds = drugs.stream().map(d -> d.drug.drugId).collect(toList());
                        return Context.frontend.batchGetDrugAttr(drugIds);
                    })
                    .thenAccept(attrList -> {
                        Map<Integer, DrugAttrDTO> attrMap = attrList.stream().collect(Collectors.toMap(
                                a -> a.drugId, a -> a
                        ));
                        CopySelectedForm form = new CopySelectedForm(local.fullDrugs, attrMap) {
                            @Override
                            protected void onEnter(List<DrugFullDTO> selected, boolean keepOpen) {
                                new DrugsCopier(targetVisitId, selected,
                                        Context.integrationService::broadcastNewDrug,
//                                        (enteredDrug, attr) ->
//                                                fireEvent(new DrugEnteredEvent(enteredDrug, attr)),
                                        () -> {
                                            if (keepOpen) {
                                                int remain = cleanUpForKeepOpen();
                                                if (remain == 0) {
                                                    hideWorkarea();
                                                }
                                            } else {
                                                hideWorkarea();
                                            }
                                        }
                                );
                            }

                            @Override
                            protected void onClose() {
                                hideWorkarea();
                            }
                        };
                        Platform.runLater(() -> showWorkarea(form));
                    })
                    .exceptionally(HandlerFX::exceptionally);
        });
        return item;
    }

    private MenuItem createModifyDaysMenuItem(int visitId) {
        MenuItem item = new MenuItem("日数変更");
        item.setOnAction(evt -> {
            if (!isWorkareaEmpty()) {
                return;
            }
            if (PracticeUtil.confirmCurrentVisitAction(visitId, "処方に日数を変更しますか？")) {
                PracticeService.listDrugFull(visitId)
                        .thenAccept(drugs -> {
                            ModifyDaysForm form = new ModifyDaysForm(drugs) {
                                @Override
                                protected void onEnter(List<DrugDTO> drugs, int days) {
                                    PracticeService.modifyDrugDays(drugs, days)
                                            .thenAccept(result -> Platform.runLater(() -> {
                                                drugs.forEach(drug -> {
                                                    onDrugDaysModifiedHandler.accept(drug, days);
                                                    //fireEvent(new DrugDaysModifiedEvent(drug, days));
                                                });
                                                hideWorkarea();
                                            }));
                                }

                                @Override
                                protected void onClose() {
                                    hideWorkarea();
                                }
                            };
                            Platform.runLater(() -> showWorkarea(form));
                        });
            }
        });
        return item;
    }

    private MenuItem createDeleteSelectedMenuItem(int visitId) {
        MenuItem item = new MenuItem("複数削除");
        item.setOnAction(evt -> {
            if (!isWorkareaEmpty()) {
                return;
            }
            if (PracticeUtil.confirmCurrentVisitAction(visitId, "複数の処方を削除しますか？")) {
                PracticeService.listDrugFull(visitId)
                        .thenAccept(drugs -> {
                            DeleteSelectedForm form = new DeleteSelectedForm(drugs) {
                                @Override
                                protected void onDelete(List<DrugDTO> drugs) {
                                    PracticeService.batchDeleteDrugs(drugs)
                                            .thenAccept(result -> Platform.runLater(() -> {
                                                List<Integer> drugIds = drugs.stream()
                                                        .map(drug -> drug.drugId).collect(toList());
                                                onDrugDeletedHandler.accept(drugIds);
                                                hideWorkarea();
                                            }));
                                }

                                @Override
                                protected void onClose() {
                                    hideWorkarea();
                                }
                            };
                            Platform.runLater(() -> showWorkarea(form));
                        });
            }
        });
        return item;
    }

    private MenuItem createDrugTextMenuItem(int visitId) {
        MenuItem menuItem = new MenuItem("処方内容を文章コピー");
        menuItem.setOnAction(evt -> {
            Context.frontend.listDrugFull(visitId)
                    .thenAccept(drugs -> {
                        List<String> reps = drugs.stream().map(DrugUtil::drugRep).collect(toList());
                        List<String> lines = new ArrayList<>();
                        int i = 1;
                        for (String rep : reps) {
                            lines.add(String.format("%d) %s\n", i, rep));
                            i += 1;
                        }
                        String text = String.join("", lines);
                        Platform.runLater(() -> GuiUtil.copyToClipboard(text));
                    })
                    .exceptionally(HandlerFX::exceptionally);
        });
        return menuItem;
    }

    private boolean isWorkareaEmpty() {
        return workarea.getChildren().size() == 0;
    }

    private void showWorkarea(Node content) {
        workarea.getChildren().add(content);
        workarea.setManaged(true);
        workarea.setVisible(true);
    }

    private void hideWorkarea() {
        workarea.getChildren().clear();
        workarea.setVisible(false);
        workarea.setManaged(false);
    }

    public Optional<DrugEnterForm> findDrugEnterForm() {
        for (Node node : workarea.getChildren()) {
            if (node instanceof DrugEnterForm) {
                return Optional.of((DrugEnterForm) node);
            }
        }
        return Optional.empty();
    }
}
