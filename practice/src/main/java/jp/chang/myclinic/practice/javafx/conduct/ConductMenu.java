package jp.chang.myclinic.practice.javafx.conduct;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.lib.PracticeUtil;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.time.LocalDate;
import java.util.function.Consumer;

public class ConductMenu extends VBox {

    private int visitId;
    private LocalDate at;
    private StackPane workarea = new StackPane();
    private Consumer<ConductFullDTO> onEnteredHandler = c -> {
    };

    public ConductMenu(int visitId, LocalDate at) {
        super(4);
        this.visitId = visitId;
        this.at = at;
        getChildren().addAll(
                createLink()
        );
    }

    public void setOnEnteredHandler(Consumer<ConductFullDTO> onEnteredHandler) {
        this.onEnteredHandler = onEnteredHandler;
    }

    private Node createLink() {
        Hyperlink link = new Hyperlink("[処置]");
        link.setOnMouseClicked(this::onClick);
        return link;
    }

    private void onClick(MouseEvent event) {
        if (!isWorkareaShown()) {
            ContextMenu contextMenu = new ContextMenu();
            {
                MenuItem item = new MenuItem("Ｘ線検査追加");
                item.setOnAction(evt -> doEnterXp());
                contextMenu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("注射追加");
                item.setOnAction(evt -> doEnterInjection());
                contextMenu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("全部コピー");
                item.setOnAction(evt -> doCopyAll());
                contextMenu.getItems().add(item);
            }
            contextMenu.show(this, event.getScreenX(), event.getScreenY());
        }
    }

    private void doEnterXp() {
        if (PracticeUtil.confirmCurrentVisitAction(visitId, "Ｘ線検査を入力しますか？")) {
            EnterXpForm form = new EnterXpForm(visitId);
            form.setOnCancelHandler(this::hideWorkarea);
            form.setOnEnteredHandler(entered -> {
                onEnteredHandler.accept(entered);
                hideWorkarea();
            });
            showWorkarea(form);
        }
    }

    private void doEnterInjection() {
        if (PracticeUtil.confirmCurrentVisitAction(visitId, "処置注射を入力しますか？")) {
            EnterInjectionForm form = new EnterInjectionForm(visitId, at);
            form.setOnCancelHandler(this::hideWorkarea);
            form.setOnEnteredHandler(entered -> {
                onEnteredHandler.accept(entered);
                hideWorkarea();
            });
            showWorkarea(form);
        }
    }

    private void doCopyAll() {
        int targetVisitId = PracticeUtil.findCopyTarget(visitId);
        if (targetVisitId > 0) {
            new ConductCopier(targetVisitId).copyFromVisit(visitId)
                    .thenAcceptAsync(entered ->
                                    entered.forEach(c -> Context.integrationService.broadcastNewConduct(c)),
                            Platform::runLater)
                    .exceptionally(HandlerFX::exceptionally);
//            Context.frontend.copyAllConducts(targetVisitId, visitId)
//                    .thenCompose(Context.frontend::listConductFullByIds)
//                    .thenAccept(entered -> Platform.runLater(() -> {
//                        entered.forEach(this::fireConductEntered);
//                    }))
//                    .exceptionally(HandlerFX::exceptionally);
        }
    }

    private void showWorkarea(Node content) {
        workarea.getChildren().clear();
        workarea.getChildren().add(content);
        getChildren().add(workarea);
    }

    private void hideWorkarea() {
        getChildren().remove(workarea);
    }

    private boolean isWorkareaShown() {
        return getChildren().contains(workarea);
    }

}
