package jp.chang.myclinic.recordbrowser;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.VisitFull2PatientPageDTO;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

class MainRoot extends VBox {

    private static Logger logger = LoggerFactory.getLogger(MainRoot.class);
    private Label titleText = new Label("");

    MainRoot() {
        getStylesheets().add("Main.css");
        getStyleClass().add("app-root");
        getChildren().add(new Label("Hello, world"));
    }

    void loadTodaysVisits(){
        LocalDate date = LocalDate.now();
        Service.api.pageVisitFullWithPatientAt(date.toString(), 0)
                .thenAccept(result -> Platform.runLater(() ->{
                    dispRecordsByDate(result, date);
                }))
                .exceptionally(HandlerFX::exceptionally);
    }

    private void dispRecordsByDate(VisitFull2PatientPageDTO pageContent, LocalDate at){
        String titleString = "";
        if( LocalDate.now().equals(at) ){
            titleString = "本日の診察";
        } else {
            DateTimeUtil.toKanji(at, )
        }
    }
}
