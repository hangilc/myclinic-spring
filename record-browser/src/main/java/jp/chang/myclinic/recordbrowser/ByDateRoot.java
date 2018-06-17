package jp.chang.myclinic.recordbrowser;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.VisitFull2PatientDTO;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.utilfx.Nav;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

class ByDateRoot extends VBox {

    //private static Logger logger = LoggerFactory.getLogger(ByDateRoot.class);
    private Label mainLabel = new Label("");
    private RecordListByDate recordList = new RecordListByDate();
    private ByDateNavHandler navHandler = new ByDateNavHandler(LocalDate.now());
    private Nav nav = new Nav();
    private Runnable onRefreshCallback = () -> {};
    private Consumer<Boolean> onSuspendCallback = suspended -> {};

    ByDateRoot() {
        super(2);
        getStylesheets().add("Main.css");
        getStyleClass().add("app-root");
        ScrollPane recordScroll = new ScrollPane(recordList);
        recordScroll.getStyleClass().add("record-scroll");
        recordScroll.setFitToWidth(true);
        getChildren().addAll(
                mainLabel,
                createNavRow(),
                recordScroll
        );
        updateMainLabel();
    }

    void trigger(){
        nav.trigger();
    }

    void setDate(LocalDate date){
        nav.reset();
        navHandler.setDate(date);
        nav.setHandler(navHandler);
        updateMainLabel();
    }

    void setOnRefreshCallback(Runnable cb){
        this.onRefreshCallback = cb;
    }

    void setOnSuspendCallback(Consumer<Boolean> cb){
        this.onSuspendCallback = cb;
    }

    private Node createNavRow(){
        HBox hbox = new HBox(4);
        navHandler.setPageCallback(this::pageCallback);
        nav.setHandler(navHandler);
        mainLabel.setMaxWidth(Double.MAX_VALUE);
        Hyperlink refreshLink = new Hyperlink("更新");
        refreshLink.setOnAction(evt -> {
            onRefreshCallback.run();
            nav.trigger();
        });
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(
                nav,
                refreshLink
        );
        return hbox;
    }

    private void pageCallback(List<VisitFull2PatientDTO> visits){
        recordList.clear();
        visits.forEach(recordList::add);
    }

    private void updateMainLabel(){
        mainLabel.setText(createMainLabelText(navHandler.getDate()));
    }

    private String createMainLabelText(LocalDate at){
        if( LocalDate.now().equals(at) ){
            return "本日の診察";
        } else {
            return DateTimeUtil.toKanji(at, DateTimeUtil.kanjiFormatter7) + "の診察";
        }

    }

}
