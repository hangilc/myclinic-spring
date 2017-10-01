package jp.chang.myclinic.practice.rightpane.disease.addpane;

import jp.chang.myclinic.dto.ByoumeiMasterDTO;
import jp.chang.myclinic.dto.ShuushokugoMasterDTO;
import jp.chang.myclinic.util.DiseaseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

class SearchResultMaster extends SearchResultData {

    private ByoumeiMasterDTO byoumeiMaster;
    private List<ShuushokugoMasterDTO> adjList;

    SearchResultMaster(ByoumeiMasterDTO byoumeiMaster, List<ShuushokugoMasterDTO> adjList){
        this.byoumeiMaster = byoumeiMaster;
        this.adjList = adjList;
    }

    static SearchResultMaster of(ByoumeiMasterDTO byoumeiMaster){
        return new SearchResultMaster(byoumeiMaster, new ArrayList<>());
    }

    static SearchResultMaster of(ShuushokugoMasterDTO adj){
        List<ShuushokugoMasterDTO> adjList = new ArrayList<>();
        adjList.add(adj);
        return new SearchResultMaster(null, adjList);
    }

    @Override
    CompletableFuture<Data> getData() {
        SearchResultData.Data data = new Data();
        data.byoumeiMaster = byoumeiMaster;
        data.adjList = adjList;
        return CompletableFuture.completedFuture(data);
    }

    @Override
    String getRep(){
        return DiseaseUtil.getFullName(byoumeiMaster, adjList);
    }

}
