package jp.chang.myclinic.recordbrowser;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

class TrackingRoot extends VBox {

    //private static Logger logger = LoggerFactory.getLogger(TrackingRoot.class);
    private Label mainLabel = new Label("本日の診察（自動更新）");
    private RecordListByDate recordList = new RecordListByDate();

    TrackingRoot() {
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

}
