package jp.chang.myclinic.recordbrowser.tracking.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class Visit {

    private int visitId;
    private LocalDateTime visitedAt;
    private Patient patient;
    private ObjectProperty<Shahokokuho> shahokokuho = new SimpleObjectProperty<>(null);
    private ObjectProperty<Koukikourei> koukikourei = new SimpleObjectProperty<>(null);
    private ObjectProperty<Kouhi> kouhi1 = new SimpleObjectProperty<>(null);
    private ObjectProperty<Kouhi> kouhi2 = new SimpleObjectProperty<>(null);
    private ObjectProperty<Kouhi> kouhi3 = new SimpleObjectProperty<>(null);
    private StringProperty hokenRep = new SimpleStringProperty();
    private Charge charge = new Charge();

    public Visit(VisitDTO visitDTO) {
        this.visitId = visitDTO.visitId;
        try {
            this.visitedAt = DateTimeUtil.parseSqlDateTime(visitDTO.visitedAt);
        } catch (DateTimeParseException ex) {
            this.visitedAt = null;
        }
    }

    public int getVisitId() {
        return visitId;
    }

    public LocalDateTime getVisitedAt() {
        return visitedAt;
    }

    public LocalDate getVisitDate(){
        return visitedAt.toLocalDate();
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Shahokokuho getShahokokuho() {
        return shahokokuho.get();
    }

    public ObjectProperty<Shahokokuho> shahokokuhoProperty() {
        return shahokokuho;
    }

    public void setShahokokuho(Shahokokuho shahokokuho) {
        this.shahokokuho.set(shahokokuho);
    }

    public Koukikourei getKoukikourei() {
        return koukikourei.get();
    }

    public ObjectProperty<Koukikourei> koukikoureiProperty() {
        return koukikourei;
    }

    public void setKoukikourei(Koukikourei koukikourei) {
        this.koukikourei.set(koukikourei);
    }

    public Kouhi getKouhi1() {
        return kouhi1.get();
    }

    public ObjectProperty<Kouhi> kouhi1Property() {
        return kouhi1;
    }

    public void setKouhi1(Kouhi kouhi1) {
        this.kouhi1.set(kouhi1);
    }

    public Kouhi getKouhi2() {
        return kouhi2.get();
    }

    public ObjectProperty<Kouhi> kouhi2Property() {
        return kouhi2;
    }

    public void setKouhi2(Kouhi kouhi2) {
        this.kouhi2.set(kouhi2);
    }

    public Kouhi getKouhi3() {
        return kouhi3.get();
    }

    public ObjectProperty<Kouhi> kouhi3Property() {
        return kouhi3;
    }

    public void setKouhi3(Kouhi kouhi3) {
        this.kouhi3.set(kouhi3);
    }

    public Charge getCharge() {
        return charge;
    }

    private String composeHokenRep(){
        List<String> parts = new ArrayList<>();
        if( shahokokuho.getValue() != null ){
            parts.add(shahokokuho.getValue().getRep());
        }
        if( koukikourei.getValue() != null ){
            parts.add(koukikourei.getValue().getRep());
        }
        if( kouhi1.getValue() != null ){
            parts.add(kouhi1.getValue().getRep());
        }
        if( kouhi2.getValue() != null ){
            parts.add(kouhi2.getValue().getRep());
        }
        if( kouhi3.getValue() != null ){
            parts.add(kouhi3.getValue().getRep());
        }
        if( parts.size() == 0 ){
            return "保険なし";
        } else {
            return String.join("・", parts);
        }
    }

    public void initHokenRep(){
        updateHokenRepValue();
        shahokokuho.addListener((obs, oldValue, newValue) -> updateHokenRepValue());
        koukikourei.addListener((obs, oldValue, newValue) -> updateHokenRepValue());
        kouhi1.addListener((obs, oldValue, newValue) -> updateHokenRepValue());
        kouhi2.addListener((obs, oldValue, newValue) -> updateHokenRepValue());
        kouhi3.addListener((obs, oldValue, newValue) -> updateHokenRepValue());
    }

    private void updateHokenRepValue(){
        hokenRep.setValue(composeHokenRep());
    }

    public String getHokenRep() {
        return hokenRep.get();
    }

    public StringProperty hokenRepProperty() {
        return hokenRep;
    }

    @Override
    public String toString() {
        return "Visit{" +
                "visitId=" + visitId +
                ", visitedAt=" + visitedAt +
                '}';
    }
}
