package jp.chang.myclinic.dto;

public class DiseaseAdjFullDTO {
    public DiseaseAdjDTO diseaseAdj;
    public ShuushokugoMasterDTO master;

    @Override
    public String toString() {
        return "DiseaseAdjFullDTO{" +
                "diseaseAdj=" + diseaseAdj +
                ", master=" + master +
                '}';
    }
}
