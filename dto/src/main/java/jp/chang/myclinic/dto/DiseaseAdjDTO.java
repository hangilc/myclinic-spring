package jp.chang.myclinic.dto;

public class DiseaseAdjDTO {

    public int diseaseAdjId;
    public int diseaseId;
    public int shuushokugocode;

    @Override
    public String toString() {
        return "DiseaseAdjDTO{" +
                "diseaseAdjId=" + diseaseAdjId +
                ", diseaseId=" + diseaseId +
                ", shuushokugocode=" + shuushokugocode +
                '}';
    }
}
