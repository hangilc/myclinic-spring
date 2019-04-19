package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.dto.ShoukiDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.CurrentPatientService;
import jp.chang.myclinic.practice.PracticeEnv;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.kanjidate.KanjiDateRepBuilder;
import jp.chang.myclinic.utilfx.AlertDialog;
import jp.chang.myclinic.utilfx.ConfirmDialog;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.time.LocalDateTime;

public class RecordTitle extends TextFlow {

    private int visitId;
    private ObjectProperty<ShoukiDTO> shoukiProperty;
    private Runnable onDeletedHandler = () -> {};

    RecordTitle(VisitDTO visit, ObjectProperty<ShoukiDTO> shoukiProperty) {
        this.visitId = visit.visitId;
        this.shoukiProperty = shoukiProperty;
        getStyleClass().add("record-title-text");
        getChildren().addAll(new Text(createText(visit.visitedAt)));
        addContextMenu();
        adaptToEnv();
    }

    public void setOnDeletedHandler(Runnable onDeletedHandler) {
        this.onDeletedHandler = onDeletedHandler;
    }

    void styleAsCurrentVisit(){
        removeStyles();
        getStyleClass().add("current-visit");
    }

    void styleAsTempVisit(){
        removeStyles();
        getStyleClass().add("temp-visit");
    }

    void styleAsRegular(){
        removeStyles();
    }

    private void removeStyles(){
        getStyleClass().removeAll("current-visit", "temp-visit");
    }

    private void adaptToEnv() {
        getStyleClass().removeAll("current-visit", "temp-visit");
        if (PracticeEnv.INSTANCE.getCurrentVisitId() == visitId) {
            getStyleClass().add("current-visit");
        } else if (PracticeEnv.INSTANCE.getTempVisitId() == visitId) {
            getStyleClass().add("temp-visit");
        }
    }

    private String createText(String at) {
        LocalDateTime dateTime = DateTimeUtil.parseSqlDateTime(at);
        return new KanjiDateRepBuilder(dateTime).format3().str(" ").format4().build();
    }

    private void addContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        {
            MenuItem item = new MenuItem("この診察を削除");
            item.setOnAction(event -> {
                if (!ConfirmDialog.confirm("この診察を削除しますか？", this)) {
                    return;
                }
                Frontend frontend = Context.frontend;
                frontend.deleteVisitSafely(visitId)
                        .thenAcceptAsync(v -> onDeletedHandler.run(),
                                Platform::runLater)
                        .exceptionally(HandlerFX::exceptionally);
            });
            contextMenu.getItems().add(item);
        }
        {
            MenuItem item = new MenuItem("暫定診察設定");
            item.setOnAction(event -> {
                doSetTempVisit();
            });
            contextMenu.getItems().add(item);
        }
        {
            MenuItem item = new MenuItem("暫定診察解除");
            item.setOnAction(event -> doUnsetTempVisit());
            contextMenu.getItems().add(item);
        }
        {
            MenuItem item = new MenuItem("診療明細");
            item.setOnAction(event -> doRcptDetail());
            contextMenu.getItems().add(item);
        }
        {
            MenuItem item = new MenuItem();
            item.textProperty().bind(Bindings.when(Bindings.isNull(shoukiProperty))
                    .then("症状詳記の追加").otherwise("症状詳記の編集"));
            item.setOnAction(evt -> doModifyShouki());
            contextMenu.getItems().add(item);
        }
        setOnContextMenuRequested(event -> {
            contextMenu.show(this, event.getScreenX(), event.getScreenY());
        });
    }

    private void doSetTempVisit() {
        if (Context.currentPatientService.getCurrentVisitId() > 0) {
            AlertDialog.alert("現在診察中なので、暫定診察を設定できません。", this);
        } else {
            Context.currentPatientService.setTempVisitId(visitId);
        }
    }

    private void doUnsetTempVisit() {
        CurrentPatientService curr = Context.currentPatientService;
        if (curr.getTempVisitId() == visitId) {
            curr.setTempVisitId(0);
        }
    }

    private void doRcptDetail() {
        Context.frontend.getMeisai(visitId)
                .thenAccept(meisai -> Platform.runLater(() -> {
                    RcptDetailDialog dialog = new RcptDetailDialog(meisai);
                    dialog.showAndWait();
                }))
                .exceptionally(HandlerFX::exceptionally);
    }

    private void doModifyShouki(){
        ShoukiForm current = PracticeEnv.INSTANCE.getShoukiForm(visitId);
        if( current != null ){
            current.toFront();
        } else {
            ShoukiForm form = new ShoukiForm(visitId, shoukiProperty.getValue());
            PracticeEnv.INSTANCE.registerShoukiForm(form);
            form.showingProperty().addListener((obs, oldValue, newValue) -> {
                if( !newValue ){
                    PracticeEnv.INSTANCE.unregisterShoukiForm(form);
                }
            });
            form.show();
        }
    }
}
