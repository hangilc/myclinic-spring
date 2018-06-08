package jp.chang.myclinic.recordbrowser.tracking;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.utilfx.HandlerFX;

public class TrackingRoot extends VBox {

    //private static Logger logger = LoggerFactory.getLogger(TrackingRoot.class);
    private Label mainLabel = new Label("本日の診察（自動更新）");
    private RecordList recordList = new RecordList();
    
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
    }

    public void reload(){
        Service.api.listAllPracticeLog()
                .thenAccept(practiceLogList -> {
                    System.out.println(practiceLogList);
                })
                .exceptionally(HandlerFX::exceptionally);
    }

}
