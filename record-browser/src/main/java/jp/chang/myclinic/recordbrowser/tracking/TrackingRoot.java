package jp.chang.myclinic.recordbrowser.tracking;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.recordbrowser.tracking.model.Conduct;
import jp.chang.myclinic.recordbrowser.tracking.model.ConductDrug;
import jp.chang.myclinic.recordbrowser.tracking.model.ConductKizai;
import jp.chang.myclinic.recordbrowser.tracking.model.Text;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.time.LocalDate;

public class TrackingRoot extends VBox implements DispatchAction {

    //private static Logger logger = LoggerFactory.getLogger(TrackingRoot.class);
    private Label mainLabel = new Label("本日の診察（自動更新）");
    private RecordList recordList = new RecordList();
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
        Conduct conduct = registry.createConduct(created);
        recordList.addConduct(conduct);
        cb.run();
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
    public void onConductShinryouCreated(ConductShinryouDTO created, Runnable cb) {
        Conduct conduct = registry.getConduct(created.conductId);
        if( conduct != null ) {
            registry.getShinryouMaster(created.shinryoucode)
                    .thenAccept(master -> Platform.runLater(() -> {
                        conduct.addConductShinryhou(created.conductShinryouId, master.name);
                        cb.run();
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        } else {
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
                        conduct.addConductDrug(conductDrug);
                        toNext.run();
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        } else {
            toNext.run();
        }
    }

    @Override
    public void onConductKizaiCreated(ConductKizaiDTO created, Runnable toNext) {
        Conduct conduct = registry.getConduct(created.conductId);
        if( conduct != null ) {
            registry.getKizaiMaster(created.kizaicode)
                    .thenAccept(master -> Platform.runLater(() -> {
                        ConductKizai conductKizai = new ConductKizai(created, master);
                        conduct.addConductKizai(conductKizai);
                        toNext.run();
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        } else {
            toNext.run();
        }
    }

    @Override
    public void onWqueueUpdated(WqueueDTO prev, WqueueDTO updated, Runnable cb){
        if( prev.waitState == WqueueWaitState.WaitExam.getCode() &&
                updated.waitState == WqueueWaitState.InExam.getCode() ){
            addVisit(updated.visitId, cb);
        } else {
            cb.run();
        }
    }

    private void addVisit(int visitId, Runnable cb){
        registry.getVisit(visitId)
                .thenAccept(visit -> Platform.runLater(() -> {
                    recordList.addVisit(visit, cb);
                }))
                .exceptionally(HandlerFX::exceptionally);
    }

    @Override
    public void onTextCreated(TextDTO textDTO, Runnable cb){
        Text text = registry.addText(textDTO);
        Platform.runLater(() -> recordList.addText(text, cb));
    }

    @Override
    public void onDrugCreated(DrugDTO drugDTO, Runnable cb){
        registry.getDrug(drugDTO)
                .thenAccept(drug -> Platform.runLater(() -> {
                    recordList.addDrug(drug);
                    cb.run();
                }))
                .exceptionally(HandlerFX::exceptionally);
    }

    @Override
    public void onShinryouCreated(ShinryouDTO created, Runnable cb) {
        registry.getShinryou(created)
                .thenAccept(shinryou -> Platform.runLater(() -> {
                    recordList.addShinryou(shinryou);
                    cb.run();
                }))
                .exceptionally(HandlerFX::exceptionally);
    }
}
