package jp.chang.myclinic.serverpostgresql.db.myclinic;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="disease")
public class Disease {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="disease_id")
    private Integer diseaseId;

    @Column(name="patient_id")
    private Integer patientId;

    private Integer shoubyoumeicode;

    @Column(name="start_date")
    private LocalDate startDate;

    @Column(name="end_date")
    private LocalDate endDate;

    @Column(name="end_reason")
    private Character endReason;

    public Integer getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(Integer diseaseId) {
        this.diseaseId = diseaseId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getShoubyoumeicode() {
        return shoubyoumeicode;
    }

    public void setShoubyoumeicode(Integer shoubyoumeicode) {
        this.shoubyoumeicode = shoubyoumeicode;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Character getEndReason() {
        return endReason;
    }

    public void setEndReason(Character endReason) {
        this.endReason = endReason;
    }

    @Override
    public String toString() {
        return "Disease{" +
                "diseaseId=" + diseaseId +
                ", patientId=" + patientId +
                ", shoubyoumeicode=" + shoubyoumeicode +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", endReason=" + endReason +
                '}';
    }
}
