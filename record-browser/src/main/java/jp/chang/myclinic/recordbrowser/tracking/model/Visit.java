package jp.chang.myclinic.recordbrowser.tracking.model;

import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class Visit {

    private int visitId;
    private LocalDateTime visitedAt;
    private Patient patient;

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

    @Override
    public String toString() {
        return "Visit{" +
                "visitId=" + visitId +
                ", visitedAt=" + visitedAt +
                '}';
    }
}
