package jp.chang.myclinic.recordbrowser.tracking.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class RecordModel {

    private static Logger logger = LoggerFactory.getLogger(RecordModel.class);
    private int visitId;
    private LocalDateTime visitedAt;
    private ObjectProperty<WqueueWaitState> waitState = new SimpleObjectProperty(WqueueWaitState.WaitExam);
    private PatientModel patient;
    private ObservableList<TextModel> texts = FXCollections.observableArrayList();

    public RecordModel(VisitDTO visitDTO, PatientModel patient) {
        this.visitId = visitDTO.visitId;
        this.visitedAt = LocalDateTime.parse(visitDTO.visitedAt, DateTimeUtil.sqlDateTimeFormatter);
        this.patient = patient;
    }

    public int getVisitId() {
        return visitId;
    }

    public LocalDateTime getVisitedAt() {
        return visitedAt;
    }

    public WqueueWaitState getWaitState() {
        return waitState.get();
    }

    public ObjectProperty<WqueueWaitState> waitStateProperty() {
        return waitState;
    }

    public void setWaitState(WqueueWaitState waitState) {
        this.waitState.set(waitState);
    }

    public PatientModel getPatient() {
        return patient;
    }

    public ObservableList<TextModel> getTexts() {
        return texts;
    }

}
