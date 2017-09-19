package jp.chang.myclinic.dto;

public class DiseaseDTO {

    public int diseaseId;
    public int patientId;
    public int shoubyoumeicode;
    public String startDate;
    public String endDate;
    public char endReason;

    @Override
    public String toString() {
        return "DiseaseDTO{" +
                "diseaseId=" + diseaseId +
                ", patientId=" + patientId +
                ", shoubyoumeicode=" + shoubyoumeicode +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", endReason=" + endReason +
                '}';
    }
}
