package jp.chang.myclinic.practice.leftpane.shinryou;

import jp.chang.myclinic.dto.BatchEnterResultDTO;
import jp.chang.myclinic.dto.ConductFullDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.practice.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

class ShinryouLib {

    static class BatchEnterShinryouResult {
        List<ShinryouFullDTO> shinryouList;
        List<ConductFullDTO> conducts;
    }

    static CompletableFuture<BatchEnterShinryouResult> batchEnterShinryouByNames(List<String> names, int visitId){
        BatchEnterResultDTO resultDTO = new BatchEnterResultDTO();
        BatchEnterShinryouResult result = new BatchEnterShinryouResult();
        return Service.api.batchEnterShinryouByName(names, visitId)
                .thenCompose(serviceResultDTO -> {
                    resultDTO.shinryouIds = serviceResultDTO.shinryouIds;
                    resultDTO.conductIds = serviceResultDTO.conductIds;
                    return Service.api.listShinryouFullByIds(resultDTO.shinryouIds);
                })
                .thenCompose(shinryouList -> {
                    result.shinryouList = shinryouList;
                    if( resultDTO.conductIds.size() == 0 ){
                        return CompletableFuture.completedFuture(Collections.emptyList());
                    }
                    return Service.api.listConductFullByIds(resultDTO.conductIds);
                })
                .thenApply(conducts -> {
                    result.conducts = conducts;
                    return result;
                });
    }

}
