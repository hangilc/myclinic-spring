package jp.chang.myclinic.practice.lib;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PracticeAPI {

    //private static Logger logger = LoggerFactory.getLogger(PracticeAPI.class);

//    private static class IgnoreException extends RuntimeException {
//
//    }

//    public static CompletableFuture<ShinryouFullDTO> copyShinryou(VisitDTO target, ShinryouFullDTO src){
//        return Service.api.resolveShinryoucode(src.shinryou.shinryoucode, target.visitedAt)
//                .thenCompose(shinryoucode -> {
//                    if( shinryoucode == 0 ){
//                        throw new IgnoreException();
//                    } else {
//                        ShinryouDTO shinryou = ShinryouDTO.copy(src.shinryou);
//                        shinryou.shinryouId = 0;
//                        shinryou.visitId = target.visitId;
//                        shinryou.shinryoucode = shinryoucode;
//                        return Service.api.enterShinryou(shinryou);
//                    }
//                })
//                .thenCompose(Service.api::getShinryouFull)
//                .exceptionally(ex -> {
//                    if( ex instanceof CompletionException ){
//                        CompletionException ce = (CompletionException)ex;
//                        if( ce.getCause() instanceof IgnoreException ){
//                            return null;
//                        } else {
//                            throw ce;
//                        }
//                    } else {
//                        throw new CompletionException(ex);
//                    }
//                });
//    }

//    public static CompletableFuture<List<ShinryouFullDTO>> batchCopyShinryou(int visitId, List<ShinryouFullDTO> srcList){
//        return Service.api.getVisit(visitId)
//                .thenCompose(visit -> CFUtil.map(srcList, s -> copyShinryou(visit, s)))
//                .thenApply(entered -> entered.stream().filter(Objects::nonNull).collect(Collectors.toList()));
//    }

    public static CompletableFuture<ConductFullDTO> enterInjection(int visitId, ConductKind kind, ConductDrugDTO drug){
        List<ConductShinryouDTO> shinryouList = new ArrayList<>();
        return Service.api.getVisit(visitId)
                .thenCompose(result -> {
                    String at = result.visitedAt;
                    if( kind == ConductKind.HikaChuusha ){
                        return Service.api.resolveShinryouMasterByName("皮下筋注", at);
                    } else if( kind == ConductKind.JoumyakuChuusha ){
                        return Service.api.resolveShinryouMasterByName("静注", at);
                    } else {
                        return CompletableFuture.completedFuture(null);
                    }
                })
                .thenCompose(master -> {
                    if( master != null ){
                        ConductShinryouDTO shinryou = new ConductShinryouDTO();
                        shinryou.shinryoucode = master.shinryoucode;
                        shinryouList.add(shinryou);
                    }
                    ConductEnterRequestDTO req = new ConductEnterRequestDTO();
                    req.visitId = visitId;
                    req.kind = kind.getCode();
                    req.shinryouList = shinryouList;
                    req.drugs = Collections.singletonList(drug);
                    return Service.api.enterConductFull(req);
                });
    }

    public static CompletableFuture<ShuushokugoMasterDTO> findDiseaseSusp(){
        return Service.api.findShuushokugoMasterByName("の疑い");

    }

}
