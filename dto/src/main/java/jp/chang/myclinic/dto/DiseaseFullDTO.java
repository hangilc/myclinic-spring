package jp.chang.myclinic.dto;

import java.util.List;

public class DiseaseFullDTO {
    public DiseaseDTO disease;
    public ByoumeiMasterDTO master;
    public List<DiseaseAdjFullDTO> adjList;

    @Override
    public String toString() {
        return "DiseaseFullDTO{" +
                "disease=" + disease +
                ", master=" + master +
                ", adjList=" + adjList +
                '}';
    }
}
