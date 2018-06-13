package jp.chang.myclinic.recordbrowser.tracking;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.recordbrowser.tracking.model.*;
import jp.chang.myclinic.recordbrowser.tracking.ui.Record;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.time.LocalDate;

public class TrackingRoot extends VBox implements DispatchAction {

    //private static Logger logger = LoggerFactory.getLogger(TrackingRoot.class);

    private VBox recordList = new VBox();
    private Dispatcher dispatcher;
    private ModelRegistry registry = new ModelRegistry();
    private String today = LocalDate.now().toString();

    public TrackingRoot() {
        super(2);
        getStylesheets().add("Main.css");
        getStyleClass().add("app-root");
        ScrollPane recordScroll = new ScrollPane(recordList);
        recordScroll.getStyleClass().add("record-scroll");
        recordScroll.setFitToWidth(true);
        Label mainLabel = new Label("本日の診察（自動更新）");
        getChildren().addAll(
                mainLabel,
                recordScroll
        );
        dispatcher = new Dispatcher();
    }

    public void reload(){
        Service.api.listAllPracticeLog(today)
                .thenAccept(logs -> Platform.runLater(() -> {
                    dispatcher.dispatch(logs, this, this::onReloaded);
                }))
                .exceptionally(HandlerFX::exceptionally);
    }

    private void onReloaded(){
        System.out.println("reloaded");
    }

    @Override
    public void onConductCreated(ConductDTO created, Runnable cb){
        Visit visit = registry.getVisit(created.visitId);
        if( visit != null ){
            Conduct conduct = new Conduct(created);
            visit.getConducts().add(conduct);
        }
        cb.run();
    }

    @Override
    public void onConductUpdated(ConductDTO prev, ConductDTO updated, Runnable cb) {
        Conduct conduct = registry.getConduct(updated.conductId);
        if( conduct != null ){
            ConductKind kind = ConductKind.fromCode(updated.kind);
            String kindRep = kind == null ? "??" : kind.getKanjiRep();
            conduct.setKind(kindRep);
            cb.run();
        }
    }

    @Override
    public void onGazouLabelCreated(GazouLabelDTO created, Runnable cb) {
        Conduct conduct = registry.getConduct(created.conductId);
        if( conduct != null ){
            conduct.gazouLabelProperty().setValue(created.label);
        }
        cb.run();
    }

    @Override
    public void onGazouLabelUpdated(GazouLabelDTO prev, GazouLabelDTO updated, Runnable cb) {
        Conduct conduct = registry.getConduct(updated.conductId);
        if( conduct != null ){
            conduct.setGazouLabel(updated.label);
        }
        cb.run();
    }

    @Override
    public void onConductShinryouCreated(ConductShinryouDTO created, Runnable cb) {
        Conduct conduct = registry.getConduct(created.conductId);
        if( conduct != null ) {
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
        if( conduct != null ) {
            conduct.removeConductShinryou(deleted.conductShinryouId);
            cb.run();
        }
    }

    @Override
    public void onConductDrugCreated(ConductDrugDTO created, Runnable toNext) {
        Conduct conduct = registry.getConduct(created.conductId);
        if( conduct != null ) {
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
        if( conduct != null ) {
            conduct.removeConductDrug(deleted.conductDrugId);
            cb.run();
        }
    }

    @Override
    public void onConductKizaiCreated(ConductKizaiDTO created, Runnable toNext) {
        Conduct conduct = registry.getConduct(created.conductId);
        if( conduct != null ) {
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
        if( conduct != null ) {
            conduct.removeConductKizai(deleted.conductKizaiId);
            cb.run();
        }
    }

    @Override
    public void onChargeCreated(ChargeDTO created, Runnable toNext) {
        Visit visit = registry.getVisit(created.visitId);
        if( visit != null ){
            visit.getCharge().setValue(created.charge);
        }
        toNext.run();
    }

    @Override
    public void onChargeUpdated(ChargeDTO prev, ChargeDTO updated, Runnable toNext) {
        Visit visit = registry.getVisit(updated.visitId);
        if( visit != null ){
            visit.getCharge().setValue(updated.charge);
        }
        toNext.run();
    }

    @Override
    public void onPaymentCreated(PaymentDTO created, Runnable toNext) {
        Visit visit = registry.getVisit(created.visitId);
        if( visit != null ){
            visit.getCharge().setPayment(created.amount);
        }
        toNext.run();
    }

    @Override
    public void onWqueueUpdated(WqueueDTO prev, WqueueDTO updated, Runnable cb){
        cb.run();
    }

    @Override
    public void onVisitCreated(VisitDTO created, Runnable toNext) {
        registry.createVisit(created)
                .thenAccept(visit -> Platform.runLater(() -> {
                    Record record = new Record(visit);
                    recordList.getChildren().add(0, record);
                    toNext.run();
                }))
                .exceptionally(HandlerFX::exceptionally);
    }

    @Override
    public void onTextCreated(TextDTO textDTO, Runnable cb){
        Visit visit = registry.getVisit(textDTO.visitId);
        if( visit != null ){
            visit.getTexts().add(new Text(textDTO));
        }
        cb.run();
    }

    @Override
    public void onTextUpdated(TextDTO prev, TextDTO updated, Runnable cb) {
        Visit visit = registry.getVisit(updated.visitId);
        if( visit != null ){
            Text text = visit.getText(updated.textId);
            if( text != null ){
                text.setContent(updated.content);
            }
        }
        cb.run();
    }

    @Override
    public void onTextDeleted(TextDTO deleted, Runnable cb) {
        Visit visit = registry.getVisit(deleted.visitId);
        if( visit != null ){
            Text text = visit.getText(deleted.textId);
            if( text != null ){
                visit.getTexts().remove(text);
            }
        }
        cb.run();
    }

    @Override
    public void onDrugCreated(DrugDTO drugDTO, Runnable cb){
        Visit visit = registry.getVisit(drugDTO.visitId);
        if( visit != null ){
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
        if( visit != null ){
            Drug drug = visit.getDrug(updated.drugId);
            if( drug != null ){
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
        if( visit != null ){
            Drug drug = visit.getDrug(deleted.drugId);
            if( drug != null ){
                visit.getDrugs().remove(drug);
                cb.run();
            }
        }
    }

    @Override
    public void onShinryouCreated(ShinryouDTO created, Runnable cb) {
        Visit visit = registry.getVisit(created.visitId);
        if( visit != null ){
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
        if( visit != null ){
            Shinryou shinryou = visit.getShinryou(deleted.shinryouId);
            if( shinryou != null ){
                visit.getShinryouList().remove(shinryou);
                cb.run();
            }
        }
    }

    @Override
    public void onHokenUpdated(VisitDTO prev, VisitDTO updated, Runnable toNext) {
        Visit visit = registry.getVisit(updated.visitId);
        if( visit != null ) {
            registry.updateHoken(visit, updated)
                    .thenAccept(r -> Platform.runLater(() -> {
                        visit.initHokenRep();
                        toNext.run();
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        }
    }
}
