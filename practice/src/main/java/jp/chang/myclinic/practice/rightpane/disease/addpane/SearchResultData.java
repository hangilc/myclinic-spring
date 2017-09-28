package jp.chang.myclinic.practice.rightpane.disease.addpane;

import jp.chang.myclinic.dto.ByoumeiMasterDTO;
import jp.chang.myclinic.dto.ShuushokugoMasterDTO;
import jp.chang.myclinic.util.DiseaseUtil;

import java.util.ArrayList;
import java.util.List;

class SearchResultData {

    private ByoumeiMasterDTO byoumeiMaster;
    private List<ShuushokugoMasterDTO> adjList;

    SearchResultData(ByoumeiMasterDTO byoumeiMaster, List<ShuushokugoMasterDTO> adjList){
        this.byoumeiMaster = byoumeiMaster;
        this.adjList = adjList;
    }

    static SearchResultData of(ByoumeiMasterDTO byoumeiMaster){
        return new SearchResultData(byoumeiMaster, new ArrayList<>());
    }

    static SearchResultData of(ShuushokugoMasterDTO adj){
        List<ShuushokugoMasterDTO> adjList = new ArrayList<>();
        adjList.add(adj);
        return new SearchResultData(null, adjList);
    }

    ByoumeiMasterDTO getByoumeiMaster() {
        return byoumeiMaster;
    }

    List<ShuushokugoMasterDTO> getAdjList() {
        return adjList;
    }

    String getRep(){
        return DiseaseUtil.getFullName(byoumeiMaster, adjList);
    }

}
