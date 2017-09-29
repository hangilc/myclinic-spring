package jp.chang.myclinic.dto;

import java.util.List;

public class DiseaseExampleDTO {
    public ByoumeiMasterDTO byoumeiMaster;
    public List<ShuushokugoMasterDTO> adjList;

    @Override
    public String toString() {
        return "DiseaseExampleDTO{" +
                "byoumeiMaster=" + byoumeiMaster +
                ", adjList=" + adjList +
                '}';
    }
}
