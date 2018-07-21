package jp.chang.myclinic.recordbrowser.tracking.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
    private StringProperty hokenRep = new SimpleStringProperty();
    private ObservableList<TextModel> texts = FXCollections.observableArrayList();
    private ObservableList<DrugModel> drugs = FXCollections.observableArrayList();
    private ObservableList<ShinryouModel> shinryouList = FXCollections.observableArrayList();
    private ObservableList<ConductModel> conducts = FXCollections.observableArrayList();

    public RecordModel(VisitDTO visitDTO, PatientModel patient, String hokenRep) {
        this.visitId = visitDTO.visitId;
        this.visitedAt = LocalDateTime.parse(visitDTO.visitedAt, DateTimeUtil.sqlDateTimeFormatter);
        this.patient = patient;
        this.hokenRep.setValue(hokenRep);
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

    public String getHokenRep() {
        return hokenRep.get();
    }

    public StringProperty hokenRepProperty() {
        return hokenRep;
    }

    public void setHokenRep(String hokenRep) {
        this.hokenRep.set(hokenRep);
    }

    public TextModel findTextModel(int textId){
        for(TextModel textModel: texts){
            if( textModel.getTextId() == textId ){
                return textModel;
            }
        }
        return null;
    }

    public ObservableList<DrugModel> getDrugs() {
        return drugs;
    }

    public DrugModel findDrugModel(int drugId){
        for(DrugModel drugModel: drugs){
            if( drugModel.getDrugId() == drugId ){
                return drugModel;
            }
        }
        return null;
    }

    public ObservableList<ShinryouModel> getShinryouList() {
        return shinryouList;
    }

    public ShinryouModel findShinryouModel(int shinryouId){
        for(ShinryouModel shinryouModel: shinryouList){
            if( shinryouModel.getShinryouId() == shinryouId ){
                return shinryouModel;
            }
        }
        return null;
    }

    public ObservableList<ConductModel> getConducts() {
        return conducts;
    }

    public ConductModel findConductModel(int conductId){
        for(ConductModel conductModel: conducts){
            if( conductModel.getConductId() == conductId ){
                return conductModel;
            }
        }
        return null;
    }
}
