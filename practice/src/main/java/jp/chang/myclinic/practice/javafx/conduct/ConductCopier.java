package jp.chang.myclinic.practice.javafx.conduct;

import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;

class ConductCopier {

    private int dstVisitId;

    ConductCopier(int dstVisitId) {
        this.dstVisitId = dstVisitId;
    }

    CompletableFuture<List<ConductFullDTO>> copyFromVisit(int srcVisitId){
        class Local {
            private LocalDate at;
            private List<ConductFullDTO> srcConducts;
            private Map<Integer, Integer> iyakuhinMap = new HashMap<>();
        }
        final Local local = new Local();
        Frontend frontend = Context.frontend;
        return frontend.getVisit(dstVisitId)
                .thenCompose(visit -> {
                    local.at = DateTimeUtil.parseSqlDateTime(visit.visitedAt).toLocalDate();
                    return frontend.listConductFull(srcVisitId);
                })
                .thenCompose(srcConducts -> {
                    local.srcConducts = srcConducts;
                    List<Integer> iyakuhincodes = srcConducts.stream()
                            .flatMap(conduct -> conduct.conductDrugs.stream())
                            .map(conductDrug -> conductDrug.conductDrug.iyakuhincode)
                            .collect(toList());
                    return frontend.batchResolveStockDrug(iyakuhincodes, local.at);
                })
                .thenCompose(resolved -> {
                    resolved.forEach(r -> local.iyakuhinMap.put(r.queryIyakuhincode, r.resolvedIyakuhincode));
                    BatchEnterRequestDTO req = composeRequest(local.srcConducts, local.iyakuhinMap);
                    return frontend.batchEnter(req);
                })
                .thenCompose(result -> frontend.listConductFullByIds(result.conductIds));
    }

    private BatchEnterRequestDTO composeRequest(List<ConductFullDTO> srcConducts,
                                                Map<Integer, Integer> iyakuhinMap){
        BatchEnterRequestDTO request = new BatchEnterRequestDTO();
        request.conducts = srcConducts.stream()
                .map(src -> toRequest(src, iyakuhinMap))
                .collect(toList());
        return request;
    }

    private ConductEnterRequestDTO toRequest(ConductFullDTO src, Map<Integer, Integer> iyakuhinMap){
        ConductEnterRequestDTO req = new ConductEnterRequestDTO();
        req.gazouLabel = src.gazouLabel == null ? null : src.gazouLabel.label;
        req.kind = src.conduct.kind;
        req.visitId = dstVisitId;
        req.shinryouList = src.conductShinryouList.stream()
                .map(s -> toShinryouRequest(s.conductShinryou))
                .collect(toList());
        req.drugs = src.conductDrugs.stream()
                .map(d -> toDrugRequest(d.conductDrug, iyakuhinMap))
                .collect(toList());
        req.kizaiList = src.conductKizaiList.stream()
                .map(s -> toKizaiRequest(s.conductKizai))
                .collect(toList());
        return req;
    }

    private ConductShinryouDTO toShinryouRequest(ConductShinryouDTO src){
        ConductShinryouDTO dst = ConductShinryouDTO.copy(src);
        dst.conductShinryouId = 0;
        dst.conductId = 0;
        return dst;
    }

    private ConductDrugDTO toDrugRequest(ConductDrugDTO src, Map<Integer, Integer> iyakuhinMap){
        ConductDrugDTO dst = ConductDrugDTO.copy(src);
        dst.conductDrugId = 0;
        dst.conductId = 0;
        dst.iyakuhincode = iyakuhinMap.get(src.iyakuhincode);
        return dst;
    }

    private ConductKizaiDTO toKizaiRequest(ConductKizaiDTO src){
        ConductKizaiDTO dst = ConductKizaiDTO.copy(src);
        dst.conductKizaiId = 0;
        dst.conductId = 0;
        return dst;
    }


}
