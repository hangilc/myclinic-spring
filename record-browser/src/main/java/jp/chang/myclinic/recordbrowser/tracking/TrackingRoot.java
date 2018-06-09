package jp.chang.myclinic.recordbrowser.tracking;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.dto.WqueueDTO;
import jp.chang.myclinic.recordbrowser.tracking.model.Text;
import jp.chang.myclinic.utilfx.HandlerFX;

public class TrackingRoot extends VBox implements DispatchAction {

    //private static Logger logger = LoggerFactory.getLogger(TrackingRoot.class);
    private Label mainLabel = new Label("本日の診察（自動更新）");
    private RecordList recordList = new RecordList();
    private Dispatcher dispatcher;
    private ModelRegistry registry = new ModelRegistry();

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
        Service.api.listAllPracticeLog()
                .thenAccept(practiceLogList -> Platform.runLater(() -> {
                    dispatcher.dispatch(practiceLogList.logs, this, this::onReloaded);
                }))
                .exceptionally(HandlerFX::exceptionally);
    }

    private void onReloaded(){
        System.out.println("reloaded");
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

}
