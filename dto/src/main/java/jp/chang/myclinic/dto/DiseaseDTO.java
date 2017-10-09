package jp.chang.myclinic.dto;

public class DiseaseDTO {

    public int diseaseId;
    public int patientId;
    public int shoubyoumeicode;
    public String startDate;
    public String endDate;
    public char endReason;

    public static DiseaseDTO copy(DiseaseDTO src){
        DiseaseDTO dst = new DiseaseDTO();
        dst.diseaseId = src.diseaseId;
        dst.patientId = src.patientId;
        dst.shoubyoumeicode = src.shoubyoumeicode;
        dst.startDate = src.startDate;
        dst.endDate = src.endDate;
        dst.endReason = src.endReason;
        return dst;
    }

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
