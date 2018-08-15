package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.ShoukiDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.PracticeEnv;
import jp.chang.myclinic.practice.javafx.events.VisitDeletedEvent;
import jp.chang.myclinic.practice.lib.PracticeService;
import jp.chang.myclinic.util.DateTimeUtil;

public class RecordTitle extends TextFlow {

    private int visitId;
    private ObjectProperty<ShoukiDTO> shoukiProperty;

    public RecordTitle(VisitDTO visit, ObjectProperty<ShoukiDTO> shoukiProperty) {
        this.visitId = visit.visitId;
        this.shoukiProperty = shoukiProperty;
        getStyleClass().add("record-title-text");
        getChildren().addAll(new Text(createText(visit.visitedAt)));
        addContextMenu();
        adaptToEnv();
        PracticeEnv.INSTANCE.currentVisitIdProperty().addListener((obs, oldValue, newValue) -> adaptToEnv());
        PracticeEnv.INSTANCE.tempVisitIdProperty().addListener((obs, oldValue, newValue) -> adaptToEnv());
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
        return DateTimeUtil.sqlDateTimeToKanji(at,
                DateTimeUtil.kanjiFormatter3, DateTimeUtil.kanjiFormatter4);
    }

    private void addContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        {
            MenuItem item = new MenuItem("この診察を削除");
            item.setOnAction(event -> {
                if (!GuiUtil.confirm("この診察を削除しますか？")) {
                    return;
                }
                PracticeService.doDeleteVisit(visitId, result -> {
                    this.fireEvent(new VisitDeletedEvent(visitId));
                });
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
        PracticeEnv env = PracticeEnv.INSTANCE;
        if (env.getCurrentVisitId() > 0) {
            GuiUtil.alertError("現在診察中なので、暫定診察を設定できません。");
        } else {
            env.setTempVisitId(visitId);
        }
    }

    private void doUnsetTempVisit() {
        PracticeEnv env = PracticeEnv.INSTANCE;
        if (env.getTempVisitId() == visitId) {
            env.setTempVisitId(0);
        }
    }

    private void doRcptDetail() {
        Service.api.getMeisai(visitId)
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
