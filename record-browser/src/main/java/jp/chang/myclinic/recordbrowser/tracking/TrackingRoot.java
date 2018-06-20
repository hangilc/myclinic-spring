package jp.chang.myclinic.recordbrowser.tracking;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.recordbrowser.tracking.model.*;
import jp.chang.myclinic.recordbrowser.tracking.ui.Record;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.time.LocalDate;

public class TrackingRoot extends VBox implements DispatchAction {

    //private static Logger logger = LoggerFactory.getLogger(TrackingRoot.class);

    private VBox recordList = new VBox();
    private Label paddingPane = new Label();
    private ScrollPane recordScroll;
    private ModelRegistry registry = new ModelRegistry();
    private CheckBox syncToCurrentVisitCheck = new CheckBox("診察に固定");
    private VBox recordListWrapper;
    private String today = LocalDate.now().toString();

    public TrackingRoot() {
        super(2);
        getStylesheets().add("Main.css");
        getStyleClass().add("app-root");
        paddingPane.setPrefHeight(0);
        recordListWrapper = new VBox(0, recordList, paddingPane);
        VBox.setVgrow(recordList, Priority.NEVER);
        VBox.setVgrow(paddingPane, Priority.NEVER);
        recordScroll = new ScrollPane(recordListWrapper);
        recordScroll.getStyleClass().add("record-scroll");
        recordScroll.setFitToWidth(true);
        VBox.setVgrow(recordScroll, Priority.ALWAYS);
        getChildren().addAll(
                mainLabel(),
                recordScroll
        );
    }

    protected void onRefreshRequest() {

    }

    private Node mainLabel() {
        HBox hbox = new HBox(4);
        syncToCurrentVisitCheck.setSelected(true);
        syncToCurrentVisitCheck.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                scrollToCurrentVisit(0, () -> {
                });
            }
        });
        Hyperlink refreshLink = new Hyperlink("手動更新");
        refreshLink.setOnAction(evt -> onRefreshRequest());
        hbox.getChildren().addAll(
                new Label("本日の診察（自動更新）"),
                syncToCurrentVisitCheck,
                refreshLink
        );
        return hbox;
    }

    @Override
    public void onConductCreated(ConductDTO created, Runnable cb) {
        Visit visit = registry.getVisit(created.visitId);
        if (visit != null) {
            Conduct conduct = new Conduct(created);
            visit.getConducts().add(conduct);
            scrollToCurrentVisit(2, cb);
        } else {
            cb.run();
        }
    }

    @Override
    public void onConductUpdated(ConductDTO prev, ConductDTO updated, Runnable cb) {
        Conduct conduct = registry.getConduct(updated.conductId);
        if (conduct != null) {
            ConductKind kind = ConductKind.fromCode(updated.kind);
            String kindRep = kind == null ? "??" : kind.getKanjiRep();
            conduct.setKind(kindRep);
            scrollToCurrentVisit(2, cb);
        } else {
            cb.run();
        }
    }

    @Override
    public void onGazouLabelCreated(GazouLabelDTO created, Runnable cb) {
        Conduct conduct = registry.getConduct(created.conductId);
        if (conduct != null) {
            conduct.gazouLabelProperty().setValue(created.label);
            scrollToCurrentVisit(2, cb);
        } else {
            cb.run();
        }
    }

    @Override
    public void onGazouLabelUpdated(GazouLabelDTO prev, GazouLabelDTO updated, Runnable cb) {
        Conduct conduct = registry.getConduct(updated.conductId);
        if (conduct != null) {
            conduct.setGazouLabel(updated.label);
            scrollToCurrentVisit(2, cb);
        } else {
            cb.run();
        }
    }

    @Override
    public void onConductShinryouCreated(ConductShinryouDTO created, Runnable cb) {
        Conduct conduct = registry.getConduct(created.conductId);
        if (conduct != null) {
            registry.getShinryouMaster(created.shinryoucode)
                    .thenAccept(master -> Platform.runLater(() -> {
                        ConductShinryou conductShinryou = new ConductShinryou(created, master);
                        conduct.getShinryouList().add(conductShinryou);
                        scrollToCurrentVisit(2, cb);
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        } else {
            cb.run();
        }
    }

    @Override
    public void onConductShinryouDeleted(ConductShinryouDTO deleted, Runnable cb) {
        Conduct conduct = registry.getConduct(deleted.conductId);
        if (conduct != null) {
            conduct.removeConductShinryou(deleted.conductShinryouId);
            scrollToCurrentVisit(1, cb);
        } else {
            cb.run();
        }
    }

    @Override
    public void onConductDrugCreated(ConductDrugDTO created, Runnable toNext) {
        Conduct conduct = registry.getConduct(created.conductId);
        if (conduct != null) {
            registry.getIyakuhinMaster(created.iyakuhincode)
                    .thenAccept(master -> Platform.runLater(() -> {
                        ConductDrug conductDrug = new ConductDrug(created, master);
                        conduct.getDrugs().add(conductDrug);
                        scrollToCurrentVisit(2, toNext);
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        } else {
            toNext.run();
        }
    }

    @Override
    public void onConductDrugDeleted(ConductDrugDTO deleted, Runnable cb) {
        Conduct conduct = registry.getConduct(deleted.conductId);
        if (conduct != null) {
            conduct.removeConductDrug(deleted.conductDrugId);
            scrollToCurrentVisit(1, cb);
        } else {
            cb.run();
        }
    }

    @Override
    public void onConductKizaiCreated(ConductKizaiDTO created, Runnable toNext) {
        Conduct conduct = registry.getConduct(created.conductId);
        if (conduct != null) {
            registry.getKizaiMaster(created.kizaicode)
                    .thenAccept(master -> Platform.runLater(() -> {
                        ConductKizai conductKizai = new ConductKizai(created, master);
                        conduct.getKizaiList().add(conductKizai);
                        scrollToCurrentVisit(2, toNext);
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        } else {
            toNext.run();
        }
    }

    @Override
    public void onConductKizaiDeleted(ConductKizaiDTO deleted, Runnable cb) {
        Conduct conduct = registry.getConduct(deleted.conductId);
        if (conduct != null) {
            conduct.removeConductKizai(deleted.conductKizaiId);
            scrollToCurrentVisit(1, cb);
        } else {
            cb.run();
        }
    }

    @Override
    public void onChargeCreated(ChargeDTO created, Runnable toNext) {
        Visit visit = registry.getVisit(created.visitId);
        if (visit != null) {
            visit.getCharge().setValue(created.charge);
            scrollToCurrentVisit(2, toNext);
        } else {
            toNext.run();
        }
    }

    @Override
    public void onChargeUpdated(ChargeDTO prev, ChargeDTO updated, Runnable toNext) {
        Visit visit = registry.getVisit(updated.visitId);
        if (visit != null) {
            visit.getCharge().setValue(updated.charge);
            scrollToCurrentVisit(2, toNext);
        } else {
            toNext.run();
        }
    }

    @Override
    public void onPaymentCreated(PaymentDTO created, Runnable toNext) {
        Visit visit = registry.getVisit(created.visitId);
        if (visit != null) {
            visit.getCharge().setPayment(created.amount);
            scrollToCurrentVisit(2, toNext);
        } else {
            toNext.run();
        }
    }

    private Record findRecord(int visitId) {
        for (Node node : recordList.getChildren()) {
            if (node instanceof Record) {
                Record rec = (Record) node;
                if (rec.getVisitId() == visitId) {
                    return rec;
                }
            }
        }
        return null;
    }

    private void updateUI(int n) {
        for (int i = 0; i < n; i++) {
            applyCss();
            layout();
        }
    }

    private void scrollToCurrentVisit(int nUpdate, Runnable cb) {
        updateUI(nUpdate);
        if (syncToCurrentVisitCheck.isSelected()) {
            Visit visit = registry.getCurrentVisit();
            if (visit != null) {
                Record record = findRecord(visit.getVisitId());
                if (record != null) {
                    double contentHeight = recordList.getBoundsInLocal().getHeight();
                    double minY = record.getBoundsInParent().getMinY();
                    double maxY = record.getBoundsInParent().getMaxY();
                    Bounds view = recordScroll.getViewportBounds();
                    double neededPad = view.getHeight() - (maxY - minY);
                    double available = contentHeight - maxY;
                    double h = 0;
                    if (available < neededPad) {
                        h = neededPad - available;
                    }
                    paddingPane.setMinHeight(0);
                    paddingPane.setMaxHeight(Double.MAX_VALUE);
                    paddingPane.setPrefHeight(h);
                    paddingPane.setMinHeight(h);
                    paddingPane.setMaxHeight(h);
                    updateUI(1);
                    double vValue;
                    if( minY == 0 ){
                        vValue = 0;
                    } else {
                        vValue = minY / (contentHeight + h - view.getHeight());
                    }
                    recordScroll.setVvalue(vValue);
                    cb.run();
                }
            } else {
                cb.run();
            }
        } else {
            cb.run();
        }
    }

    @Override
    public void onWqueueUpdated(WqueueDTO prev, WqueueDTO updated, Runnable cb) {
        Visit visit = registry.getVisit(updated.visitId);
        if (visit != null) {
            visit.setWqueueState(updated.waitState);
            if (updated.waitState == WqueueWaitState.InExam.getCode()) {
                scrollToCurrentVisit(2, cb);
            } else if( prev.waitState == WqueueWaitState.InExam.getCode() ){
                recordScroll.setVvalue(0);
                cb.run();
            } else {
                cb.run();
            }
        } else {
            cb.run();
        }
    }

    @Override
    public void onVisitCreated(VisitDTO created, Runnable toNext) {
        if (created.visitedAt.substring(0, 10).equals(today)) {
            registry.createVisit(created)
                    .thenAccept(visit -> Platform.runLater(() -> {
                        Record record = new Record(visit);
                        recordList.getChildren().add(0, record);
                        scrollToCurrentVisit(2, toNext);
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        } else {
            toNext.run();
        }
    }

    @Override
    public void onVisitDeleted(VisitDTO deleted, Runnable toNext) {
        int visitId = deleted.visitId;
        registry.deleteVisit(visitId);
        Record rec = null;
        for(Node node: recordList.getChildren()){
            if( node instanceof Record ){
                Record r = (Record)node;
                if( r.getVisitId() == visitId ){
                    rec = r;
                    break;
                }
            }
        }
        if( rec != null ){
            recordList.getChildren().remove(rec);
            scrollToCurrentVisit(1, toNext);
        } else {
            toNext.run();
        }
    }

    @Override
    public void onTextCreated(TextDTO textDTO, Runnable cb) {
        Visit visit = registry.getVisit(textDTO.visitId);
        if (visit != null) {
            visit.getTexts().add(new Text(textDTO));
            scrollToCurrentVisit(2, cb);
        } else {
            cb.run();
        }
    }

    @Override
    public void onTextUpdated(TextDTO prev, TextDTO updated, Runnable cb) {
        Visit visit = registry.getVisit(updated.visitId);
        if (visit != null) {
            Text text = visit.getText(updated.textId);
            if (text != null) {
                text.setContent(updated.content);
            }
        }
        scrollToCurrentVisit(2, cb);
    }

    @Override
    public void onTextDeleted(TextDTO deleted, Runnable cb) {
        Visit visit = registry.getVisit(deleted.visitId);
        if (visit != null) {
            Text text = visit.getText(deleted.textId);
            if (text != null) {
                visit.getTexts().remove(text);
            }
        }
        scrollToCurrentVisit(1, cb);
    }

    @Override
    public void onDrugCreated(DrugDTO drugDTO, Runnable cb) {
        Visit visit = registry.getVisit(drugDTO.visitId);
        if (visit != null) {
            registry.getIyakuhinMaster(drugDTO.iyakuhincode)
                    .thenAccept(master -> Platform.runLater(() -> {
                        Drug drug = new Drug(drugDTO, master);
                        visit.getDrugs().add(drug);
                        scrollToCurrentVisit(2, cb);
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        } else {
            cb.run();
        }
    }

    @Override
    public void onDrugUpdated(DrugDTO prev, DrugDTO updated, Runnable cb) {
        Visit visit = registry.getVisit(updated.visitId);
        if (visit != null) {
            Drug drug = visit.getDrug(updated.drugId);
            if (drug != null) {
                registry.getIyakuhinMaster(updated.iyakuhincode)
                        .thenAccept(master -> Platform.runLater(() -> {
                            drug.updateRep(updated, master);
                            scrollToCurrentVisit(2, cb);
                        }))
                        .exceptionally(HandlerFX::exceptionally);
            }
        } else {
            cb.run();
        }
    }

    @Override
    public void onDrugDeleted(DrugDTO deleted, Runnable cb) {
        Visit visit = registry.getVisit(deleted.visitId);
        if (visit != null) {
            Drug drug = visit.getDrug(deleted.drugId);
            if (drug != null) {
                visit.getDrugs().remove(drug);
                scrollToCurrentVisit(1, cb);
            }
        } else {
            cb.run();
        }
    }

    @Override
    public void onShinryouCreated(ShinryouDTO created, Runnable cb) {
        Visit visit = registry.getVisit(created.visitId);
        if (visit != null) {
            registry.getShinryouMaster(created.shinryoucode)
                    .thenAccept(master -> Platform.runLater(() -> {
                        Shinryou shinryou = new Shinryou(created, master);
                        visit.getShinryouList().add(shinryou);
                        scrollToCurrentVisit(2, cb);
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        } else {
            cb.run();
        }
    }

    @Override
    public void onShinryouDeleted(ShinryouDTO deleted, Runnable cb) {
        Visit visit = registry.getVisit(deleted.visitId);
        if (visit != null) {
            Shinryou shinryou = visit.getShinryou(deleted.shinryouId);
            if (shinryou != null) {
                visit.getShinryouList().remove(shinryou);
                scrollToCurrentVisit(1, cb);
            }
        } else {
            cb.run();
        }
    }

    @Override
    public void onHokenUpdated(VisitDTO prev, VisitDTO updated, Runnable toNext) {
        Visit visit = registry.getVisit(updated.visitId);
        if (visit != null) {
            registry.updateHoken(visit, updated)
                    .thenAccept(r -> Platform.runLater(() -> {
                        visit.initHokenRep();
                        scrollToCurrentVisit(2, toNext);
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        } else {
            toNext.run();
        }
    }
}
