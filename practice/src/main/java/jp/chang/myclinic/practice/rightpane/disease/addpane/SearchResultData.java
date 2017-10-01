package jp.chang.myclinic.practice.rightpane.disease.addpane;

import jp.chang.myclinic.dto.ByoumeiMasterDTO;
import jp.chang.myclinic.dto.ShuushokugoMasterDTO;

import java.util.List;
import java.util.concurrent.CompletableFuture;

abstract class SearchResultData {

    static class Data {
        ByoumeiMasterDTO byoumeiMaster;
        List<ShuushokugoMasterDTO> adjList;
    }

    abstract CompletableFuture<Data> getData();

    abstract String getRep();
}
