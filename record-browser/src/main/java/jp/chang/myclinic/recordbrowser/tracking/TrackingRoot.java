package jp.chang.myclinic.recordbrowser.tracking;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.WqueueDTO;
import jp.chang.myclinic.utilfx.HandlerFX;

public class TrackingRoot extends VBox implements DispatchAction {

    //private static Logger logger = LoggerFactory.getLogger(TrackingRoot.class);
    private Label mainLabel = new Label("本日の診察（自動更新）");
    private RecordList recordList = new RecordList();
    private Dispatcher dispatcher;
    private String currentServer;
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
                .thenAccept(practiceLogList -> {
                    this.currentServer = practiceLogList.serverId;
                    dispatcher.dispatch(practiceLogList.logs, this);
                })
                .exceptionally(HandlerFX::exceptionally);
    }

    @Override
    public void onWqueueUpdated(WqueueDTO prev, WqueueDTO updated){
        if( prev.waitState == WqueueWaitState.WaitExam.getCode() &&
                updated.waitState == WqueueWaitState.InExam.getCode() ){
            addVisit(updated.visitId);
        }
    }

    private void addVisit(int visitId){
        registry.getVisit(visitId)
                .thenAccept(visit -> {
                    System.out.println(visit);
                })
                .exceptionally(HandlerFX::exceptionally);
    }

}
