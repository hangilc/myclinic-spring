package jp.chang.myclinic.recordbrowser;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import jp.chang.myclinic.utilfx.TwoColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

class MainRoot extends VBox {

    private static Logger logger = LoggerFactory.getLogger(MainRoot.class);
    private Label titleText = new Label("");
    private VBox recordPane = new VBox(4);

    MainRoot() {
        getStylesheets().add("Main.css");
        getStyleClass().add("app-root");
        ScrollPane recordScroll = new ScrollPane(recordPane);
        recordScroll.getStyleClass().add("record-scroll");
        recordScroll.setFitToWidth(true);
        getChildren().addAll(
                titleText,
                recordScroll
        );
    }

    void loadTodaysVisits(){
        loadVisitsAt(LocalDate.now());
    }

    void loadVisitsAt(LocalDate at){
        Service.api.pageVisitFullWithPatientAt(at.toString(), 0)
                .thenAccept(result -> Platform.runLater(() ->{
                    dispRecordsByDate(result, at);
                }))
                .exceptionally(HandlerFX::exceptionally);
    }

    private void dispRecordsByDate(VisitFull2PatientPageDTO pageContent, LocalDate at){
        String titleString = "";
        if( LocalDate.now().equals(at) ){
            titleString = "本日の診察";
        } else {
            titleString = DateTimeUtil.toKanji(at, DateTimeUtil.kanjiFormatter1) + "の診察";
        }
        titleText.setText(titleString);
        recordPane.getChildren().clear();
        pageContent.visitPatients.forEach(visitPatient ->
                addRecordByDate(visitPatient.patient, visitPatient.visitFull));
    }

    private void addRecordByDate(PatientDTO patient, VisitFull2DTO visit){
        TwoColumn twoColumn = new TwoColumn(4);
        visit.texts.forEach(textDTO -> addText(twoColumn.getLeftBox(), textDTO));
        VBox right = twoColumn.getRightBox();
        addHoken(right, visit.hoken);
        right.getChildren().add(new Label("[処方]"));
        if( visit.drugs.size() > 0 ){
            int index = 1;
            for(DrugFullDTO drug: visit.drugs){
                right.getChildren().add(new RecordDrug(index++, drug));
            }
        }
        right.getChildren().add(new Label("[診療行為]"));
        visit.shinryouList.forEach(shinryou -> addShinryou(right, shinryou));
        right.getChildren().add(new Label("[処置]"));
        visit.conducts.forEach(conduct -> addConduct(right, conduct));
        recordPane.getChildren().addAll(
                new RecordTitle(patient, visit.visit),
                twoColumn
        );
    }

    private void addText(Pane pane, TextDTO textDTO){
        pane.getChildren().add(new RecordText(textDTO));
    }

    private void addHoken(Pane pane, HokenDTO hoken){
        pane.getChildren().add(new RecordHoken(hoken));
    }

    private void addShinryou(Pane pane, ShinryouFullDTO shinryou){
        pane.getChildren().add(new RecordShinryou(shinryou));
    }

    private void addConduct(Pane pane, ConductFullDTO conduct){
        pane.getChildren().add(new RecordConduct(conduct));
    }

}
