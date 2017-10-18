package jp.chang.myclinic.dto;

import java.util.List;

public class DiseaseNewDTO {
    public DiseaseDTO disease;
    public List<DiseaseAdjDTO> adjList;

    @Override
    public String toString() {
        return "DiseaseNewDTO{" +
                "disease=" + disease +
                ", adjList=" + adjList +
                '}';
    }
}
