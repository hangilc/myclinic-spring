package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.practice.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

class DrugLib {

    static CompletableFuture<List<Integer>> copyDrugs(int targetVisitId, List<DrugFullDTO> srcFullDrugs) {
        return Service.api.getVisit(targetVisitId)
                .thenCompose(targetVisit -> {
                    String at = targetVisit.visitedAt.substring(0, 10);
                    List<Integer> iyakuhincodes = srcFullDrugs.stream().map(d -> d.drug.iyakuhincode).collect(Collectors.toList());
                    return Service.api.batchResolveIyakuhinMaster(iyakuhincodes, at);
                })
                .thenCompose(resolvedMap -> {
                    List<DrugDTO> dstDrugs = new ArrayList<>();
                    srcFullDrugs.forEach(srcFullDrug -> {
                        IyakuhinMasterDTO master = resolvedMap.getOrDefault(srcFullDrug.drug.iyakuhincode, null);
                        if (master == null) {
                            throw new RuntimeException(srcFullDrug.master.name + "はコピー先で使用できません。");
                        }
                        DrugDTO dstDrug = DrugDTO.copy(srcFullDrug.drug);
                        dstDrug.drugId = 0;
                        dstDrug.iyakuhincode = master.iyakuhincode;
                        dstDrug.visitId = targetVisitId;
                        dstDrug.prescribed = 0;
                        dstDrugs.add(dstDrug);
                    });
                    return Service.api.batchEnterDrugs(dstDrugs);
                });
    }

    static CompletableFuture<DrugFullDTO> enterDrug(DrugDTO drug){
        return Service.api.enterDrug(drug)
                .thenCompose(Service.api::getDrugFull);
    }

}
