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
        }
        cb.run();
    }

    @Override
    public void onConductUpdated(ConductDTO prev, ConductDTO updated, Runnable cb) {
        Conduct conduct = registry.getConduct(updated.conductId);
        if (conduct != null) {
            ConductKind kind = ConductKind.fromCode(updated.kind);
            String kindRep = kind == null ? "??" : kind.getKanjiRep();
            conduct.setKind(kindRep);
            cb.run();
        }
    }

    @Override
    public void onGazouLabelCreated(GazouLabelDTO created, Runnable cb) {
        Conduct conduct = registry.getConduct(created.conductId);
        if (conduct != null) {
            conduct.gazouLabelProperty().setValue(created.label);
        }
        cb.run();
    }

    @Override
    public void onGazouLabelUpdated(GazouLabelDTO prev, GazouLabelDTO updated, Runnable cb) {
        Conduct conduct = registry.getConduct(updated.conductId);
        if (conduct != null) {
            conduct.setGazouLabel(updated.label);
        }
        cb.run();
    }

    @Override
    public void onConductShinryouCreated(ConductShinryouDTO created, Runnable cb) {
        Conduct conduct = registry.getConduct(created.conductId);
        if (conduct != null) {
            registry.getShinryouMaster(created.shinryoucode)
                    .thenAccept(master -> Platform.runLater(() -> {
                        ConductShinryou conductShinryou = new ConductShinryou(created, master);
                        conduct.getShinryouList().add(conductShinryou);
                        cb.run();
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

    @Override
    public void onConductShinryouDeleted(ConductShinryouDTO deleted, Runnable cb) {
        Conduct conduct = registry.getConduct(deleted.conductId);
        if (conduct != null) {
            conduct.removeConductShinryou(deleted.conductShinryouId);
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
                        toNext.run();
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

    @Override
    public void onConductDrugDeleted(ConductDrugDTO deleted, Runnable cb) {
        Conduct conduct = registry.getConduct(deleted.conductId);
        if (conduct != null) {
            conduct.removeConductDrug(deleted.conductDrugId);
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
                        toNext.run();
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

    @Override
    public void onConductKizaiDeleted(ConductKizaiDTO deleted, Runnable cb) {
        Conduct conduct = registry.getConduct(deleted.conductId);
        if (conduct != null) {
            conduct.removeConductKizai(deleted.conductKizaiId);
            cb.run();
        }
    }

    @Override
    public void onChargeCreated(ChargeDTO created, Runnable toNext) {
        Visit visit = registry.getVisit(created.visitId);
        if (visit != null) {
            visit.getCharge().setValue(created.charge);
        }
        toNext.run();
    }

    @Override
    public void onChargeUpdated(ChargeDTO prev, ChargeDTO updated, Runnable toNext) {
        Visit visit = registry.getVisit(updated.visitId);
        if (visit != null) {
            visit.getCharge().setValue(updated.charge);
        }
        toNext.run();
    }

    @Override
    public void onPaymentCreated(PaymentDTO created, Runnable toNext) {
        Visit visit = registry.getVisit(created.visitId);
        if (visit != null) {
            visit.getCharge().setPayment(created.amount);
        }
        toNext.run();
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
                    System.err.println("record height: " + record.getHeight());
                    System.err.println("record visitId: " + record.getVisitId());
                    double contentHeight = recordList.getBoundsInLocal().getHeight();
                    System.err.println("contentHeight: " + contentHeight);
                    double minY = record.getBoundsInParent().getMinY();
                    double maxY = record.getBoundsInParent().getMaxY();
                    for(Node node: recordList.getChildren()){
                        if( node instanceof Record){
                            Record rec = (Record)node;
                            System.err.printf("Record (%d) yMin %g; yMax %g\n",
                                    rec.getVisitId(),
                                    rec.getBoundsInParent().getMinY(),
                                    rec.getBoundsInParent().getMaxY());
                        }
                    }
                    System.err.printf("minY %g; maxY %g\n", minY, maxY);
                    Bounds view = recordScroll.getViewportBounds();
                    System.err.println("viewHeight: " + view.getHeight());
                    double neededPad = view.getHeight() - (maxY - minY);
                    double available = contentHeight - maxY;
                    double h = 0;
                    if (available < neededPad) {
                        h = neededPad - available;
                    }
                    System.err.println("h: " + h);
                    paddingPane.setPrefHeight(h);
                    updateUI(1);
                    System.err.println("padding height: " + paddingPane.getHeight());
                    System.err.println("wrapper height: " + recordListWrapper.getHeight());
                    System.err.println("children: " + recordListWrapper.getChildren().size());
                    for (Node node : recordListWrapper.getChildren()) {
                        if (node instanceof VBox) {
                            System.err.println(((VBox) node).getBoundsInLocal().getHeight());
                        }
                        if (node instanceof Label) {
                            System.err.println(((Label) node).getBoundsInLocal().getHeight());
                        }
                    }
                    double vValue;
                    if( minY == 0 ){
                        vValue = 0;
                    } else {
                        vValue = minY / (contentHeight + h - view.getHeight());
                    }
                    System.err.println("vValue: " + vValue);
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
                scrollToCurrentVisit(0, cb);
            } else {
                cb.run();
            }
        }
    }

    @Override
    public void onVisitCreated(VisitDTO created, Runnable toNext) {
        System.out.println("visit created: " + created.visitId);
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
        cb.run();
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
        cb.run();
    }

    @Override
    public void onDrugCreated(DrugDTO drugDTO, Runnable cb) {
        Visit visit = registry.getVisit(drugDTO.visitId);
        if (visit != null) {
            registry.getIyakuhinMaster(drugDTO.iyakuhincode)
                    .thenAccept(master -> Platform.runLater(() -> {
                        Drug drug = new Drug(drugDTO, master);
                        visit.getDrugs().add(drug);
                        cb.run();
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

    @Override
    public void onDrugUpdated(DrugDTO prev, DrugDTO updated, Runnable cb) {
        Visit visit = registry.getVisit(updated.visitId);
        if (visit != null) {
            Drug drug = visit.getDrug(updated.drugId);
            if (drug != null) {
                registry.getIyakuhinMaster(updated.iyakuhincode)
                        .thenAccept(master -> {
                            drug.updateRep(updated, master);
                            cb.run();
                        })
                        .exceptionally(HandlerFX::exceptionally);
            }
        }
    }

    @Override
    public void onDrugDeleted(DrugDTO deleted, Runnable cb) {
        Visit visit = registry.getVisit(deleted.visitId);
        if (visit != null) {
            Drug drug = visit.getDrug(deleted.drugId);
            if (drug != null) {
                visit.getDrugs().remove(drug);
                cb.run();
            }
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
                        cb.run();
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

    @Override
    public void onShinryouDeleted(ShinryouDTO deleted, Runnable cb) {
        Visit visit = registry.getVisit(deleted.visitId);
        if (visit != null) {
            Shinryou shinryou = visit.getShinryou(deleted.shinryouId);
            if (shinryou != null) {
                visit.getShinryouList().remove(shinryou);
                cb.run();
            }
        }
    }

    @Override
    public void onHokenUpdated(VisitDTO prev, VisitDTO updated, Runnable toNext) {
        Visit visit = registry.getVisit(updated.visitId);
        if (visit != null) {
            registry.updateHoken(visit, updated)
                    .thenAccept(r -> Platform.runLater(() -> {
                        visit.initHokenRep();
                        toNext.run();
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        }
    }
}
