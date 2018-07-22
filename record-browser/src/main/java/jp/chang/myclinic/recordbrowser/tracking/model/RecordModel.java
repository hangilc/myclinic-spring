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
import jp.chang.myclinic.util.HokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class RecordModel {

    private static Logger logger = LoggerFactory.getLogger(RecordModel.class);
    private int visitId;
    private LocalDateTime visitedAt;
    private ObjectProperty<WqueueWaitState> waitState = new SimpleObjectProperty<>(WqueueWaitState.WaitExam);
    private PatientModel patient;
    private ObservableList<TextModel> texts = FXCollections.observableArrayList();
    private ObservableList<DrugModel> drugs = FXCollections.observableArrayList();
    private ObservableList<ShinryouModel> shinryouList = FXCollections.observableArrayList();
    private ObservableList<ConductModel> conducts = FXCollections.observableArrayList();
    private ObjectProperty<Integer> shahokokuhoHokenshaBangou = new SimpleObjectProperty<>(null);
    private ObjectProperty<Integer> shahokokuhoKoureiFutanWari = new SimpleObjectProperty<>(null);
    private ObjectProperty<Integer> koukikoureiFutanWari = new SimpleObjectProperty<>(null);
    private ObjectProperty<Integer> kouhi1FutanshaBangou = new SimpleObjectProperty<>(null);
    private ObjectProperty<Integer> kouhi2FutanshaBangou = new SimpleObjectProperty<>(null);
    private ObjectProperty<Integer> kouhi3FutanshaBangou = new SimpleObjectProperty<>(null);
    private StringProperty hokenRep = new SimpleStringProperty();

    public RecordModel(VisitDTO visitDTO, PatientModel patient, HokenModel hokenModel) {
        this.visitId = visitDTO.visitId;
        this.visitedAt = LocalDateTime.parse(visitDTO.visitedAt, DateTimeUtil.sqlDateTimeFormatter);
        this.patient = patient;
        bindHoken(hokenModel);
        updateHokenRep();
        this.shahokokuhoHokenshaBangou.addListener((obs, oldValue, newValue) -> updateHokenRep());
        this.shahokokuhoKoureiFutanWari.addListener((obs, oldValue, newValue) -> updateHokenRep());
        this.koukikoureiFutanWari.addListener((obs, oldValue, newValue) -> updateHokenRep());
        this.kouhi1FutanshaBangou.addListener((obs, oldValue, newValue) -> updateHokenRep());
        this.kouhi2FutanshaBangou.addListener((obs, oldValue, newValue) -> updateHokenRep());
        this.kouhi3FutanshaBangou.addListener((obs, oldValue, newValue) -> updateHokenRep());
    }

    private void bindHoken(HokenModel hokenModel){
        ShahokokuhoModel shahokokuhoModel = hokenModel.getShahokokuhoModel();
        if( shahokokuhoModel != null ) {
            this.shahokokuhoHokenshaBangou.bind(shahokokuhoModel.hokenshaBangouProperty());
            this.shahokokuhoKoureiFutanWari.bind(shahokokuhoModel.koureiFutanWariProperty());
        } else {
            this.shahokokuhoHokenshaBangou.setValue(null);
            this.shahokokuhoKoureiFutanWari.setValue(null);
        }
        KoukikoureiModel koukikoureiModel = hokenModel.getKoukikoureiModel();
        if( koukikoureiModel != null ) {
            this.koukikoureiFutanWari.bind(koukikoureiModel.futanWariProperty());
        } else {
            this.koukikoureiFutanWari.setValue(null);
        }
        KouhiModel kouhi1Model = hokenModel.getKouhi1Model();
        if( kouhi1Model != null ) {
            this.kouhi1FutanshaBangou.bind(kouhi1Model.futanshaBangouProperty());
        } else {
            this.kouhi1FutanshaBangou.setValue(null);
        }
        KouhiModel kouhi2Model = hokenModel.getKouhi2Model();
        if( kouhi2Model != null ) {
            this.kouhi2FutanshaBangou.bind(kouhi2Model.futanshaBangouProperty());
        } else {
            this.kouhi2FutanshaBangou.setValue(null);
        }
        KouhiModel kouhi3Model = hokenModel.getKouhi3Model();
        if( kouhi3Model != null ) {
            this.kouhi3FutanshaBangou.bind(kouhi3Model.futanshaBangouProperty());
        } else {
            this.kouhi3FutanshaBangou.setValue(null);
        }
    }

    private void unbindHoken(){
        this.shahokokuhoHokenshaBangou.unbind();
        this.shahokokuhoKoureiFutanWari.unbind();
        this.koukikoureiFutanWari.unbind();
        this.kouhi1FutanshaBangou.unbind();
        this.kouhi2FutanshaBangou.unbind();
        this.kouhi3FutanshaBangou.unbind();
    }

    public void updateHoken(HokenModel hokenModel){
        unbindHoken();
        bindHoken(hokenModel);
    }

    private void updateHokenRep(){
        String rep = HokenUtil.hokenRep(shahokokuhoHokenshaBangou.getValue(),
                shahokokuhoKoureiFutanWari.getValue(),
                koukikoureiFutanWari.getValue(),
                null, // roujinFutanWari
                kouhi1FutanshaBangou.getValue(),
                kouhi2FutanshaBangou.getValue(),
                kouhi3FutanshaBangou.getValue());
        if( rep.isEmpty() ) {
            rep = "(保険なし)";
        }
        hokenRep.setValue(rep);
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

    public ObservableList<ConductModel> getConducts() {
        return conducts;
    }

}
